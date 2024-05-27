package com.example.gia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MaterialActivity extends AppCompatActivity {

    TextView name, body;
    private MaterialAdapter materialAdapter;
    private RecyclerView recyclerView;
    Button next_btn;

    Boolean check = true;

    Integer material_id_int;

    Integer otslezh_id;
    Integer id_teory_int;
    Integer id_peredat;
    private ProgressBar progressBar_3;
    private String material_id;
    private String id_teory;

    private List<Material> materialList = new ArrayList<>();
    private String ApiKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVlbXFzbW5jenZhZ2R3Y3JqZGpnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTM4OTY4MjgsImV4cCI6MjAyOTQ3MjgyOH0.LE4A68rcTGo8IfeVTlYFEyw_xCr16K0aetLG0UZbmzI";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_material);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button next_btn = findViewById(R.id.next_btn);
        LinearLayout back_linear = findViewById(R.id.baacckkk_linear);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        materialAdapter = new MaterialAdapter(materialList);
        recyclerView.setAdapter(materialAdapter);

        progressBar_3 = findViewById(R.id.progressBar_3);

        recyclerView.setVisibility(View.GONE);






        material_id = getIntent().getStringExtra("id");
        id_teory = getIntent().getStringExtra("id_te");
        material_id_int = Integer.parseInt(material_id);
        id_teory_int = Integer.parseInt(id_teory);
        id_peredat = id_teory_int;


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                material_id_int++;
                materialList.clear();
                materialAdapter.notifyDataSetChanged();
                fetchDataFromSupabase(material_id_int);
            }
        });

        back_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MaterialActivity.this, TheoryActivity.class);
                intent.putExtra("id", id_peredat);
                startActivity(intent);
            }
        });


        fetchDataFromSupabase(material_id_int);
    }

    private void showResults() {
        String message = "Вы прочитали все темы в этом разделе";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.GONE);
                check=true;
                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialActivity.this);
                builder.setTitle("Ой-ой!")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(MaterialActivity.this, TheoryActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }


    private void fetchDataFromSupabase(int material_id) {
        progressBar_3.setVisibility(View.VISIBLE);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://eemqsmnczvagdwcrjdjg.supabase.co/rest/v1/pod_theory?id=eq." + material_id)
                        .header("apikey", ApiKey)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar_3.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
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
                                        progressBar_3.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        } else {
                            Log.e("TAG", "Error: " + response.code() + " " + response.message());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar_3.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
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
                Material material = new Material();
                material.setId(jsonObject.getString("id"));
                material.setName(jsonObject.getString("name"));
                material.setBody(jsonObject.getString("body"));
                material.setId_te(jsonObject.getString("id_te"));
                if(check==true){
                    otslezh_id = Integer.valueOf(material.getId_te());
                    check=false;
                }

                materialList.add(material);
                if(otslezh_id!=Integer.valueOf(material.getId_te())){
                    Log.e("TAG", "Error: Пусто");
                    showResults();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    materialAdapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}