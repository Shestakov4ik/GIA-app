package com.example.gia;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TheoryActivity extends AppCompatActivity implements TheoryAdapter.OnTheoryClickListener {

    private String ApiKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVlbXFzbW5jenZhZ2R3Y3JqZGpnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTM4OTY4MjgsImV4cCI6MjAyOTQ3MjgyOH0.LE4A68rcTGo8IfeVTlYFEyw_xCr16K0aetLG0UZbmzI";
    private RecyclerView recyclerView;
    private TheoryAdapter theoryAdapter;

    private Button back_btn;

    private String material_id_str;

    private Integer material_id;

    private ScrollView podteom;
    private FrameLayout fragment_container;

    private LinearLayout rec_lin;

    private ProgressBar progressBar_2;

    private List<Theory> theoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        theoryAdapter = new TheoryAdapter(theoryList, this);
        recyclerView.setAdapter(theoryAdapter);
        progressBar_2 = findViewById(R.id.progressBar_2);
        back_btn = findViewById(R.id.back_btn);
        fragment_container = findViewById(R.id.fragment_container);
        rec_lin = findViewById(R.id.rec_lin);
        back_btn.setVisibility(View.GONE);


        material_id = getIntent().getIntExtra("id", 0);
        material_id_str = String.valueOf((material_id));
        if (material_id!=0){
            onTheoryClick(material_id_str);
        } else {
            rec_lin.setVisibility(View.GONE);
            fragment_container.setVisibility(View.GONE);
            fetchDataFromSupabase();
            Log.e("TAG", "Error: неут");
        }



        LinearLayout back_linear = findViewById(R.id.bacckkk_linear);

        back_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TheoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_container.setVisibility(View.GONE);
                back_btn.setVisibility(View.GONE);
                material_id = 0;
                rec_lin.setVisibility(View.VISIBLE);
                theoryList.clear();
                fetchDataFromSupabase();

            }
        });
    }

    @Override
    public void onTheoryClick(String theoryId) {
               back_btn.setVisibility(View.VISIBLE);
        rec_lin.setVisibility(View.GONE);
        fragment_container.setVisibility(View.VISIBLE);
        PodtemaFragment fragment = PodtemaFragment.newInstance(theoryId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

    }



    private void fetchDataFromSupabase() {
        progressBar_2.setVisibility(View.VISIBLE);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://eemqsmnczvagdwcrjdjg.supabase.co/rest/v1/Theory")
                        .header("apikey", ApiKey)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar_2.setVisibility(View.GONE);
                                rec_lin.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                String jsonData = responseBody.string();
                                parseJsonData(jsonData);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar_2.setVisibility(View.GONE);
                                        rec_lin.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        } else {
                            Log.e("TAG", "Error: " + response.code() + " " + response.message());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar_2.setVisibility(View.GONE);
                                    rec_lin.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void parseJsonData(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Theory theory = new Theory();
                theory.setId(jsonObject.getString("id"));
                theory.setName(jsonObject.getString("name"));
                theory.setShort_body(jsonObject.getString("short_body"));

                theoryList.add(theory);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    theoryAdapter.sortDataById();
                    theoryAdapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

