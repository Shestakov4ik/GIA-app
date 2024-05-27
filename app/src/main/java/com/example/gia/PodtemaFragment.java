package com.example.gia;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class PodtemaFragment extends Fragment implements PodTheoryAdapter.OnTheoryClickListener {

    private String ApiKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVlbXFzbW5jenZhZ2R3Y3JqZGpnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTM4OTY4MjgsImV4cCI6MjAyOTQ3MjgyOH0.LE4A68rcTGo8IfeVTlYFEyw_xCr16K0aetLG0UZbmzI";

    String theoryId;
    String pered_Id;
    Integer id_teory;
    private PodTheoryAdapter podtheoryAdapter;

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private List<PodTheory> podtheoryList = new ArrayList<>();

    public static PodtemaFragment newInstance(String theoryId) {
        PodtemaFragment fragment = new PodtemaFragment();
        Bundle args = new Bundle();
        args.putString("theoryId", theoryId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            theoryId = getArguments().getString("theoryId");
            pered_Id = theoryId;
            id_teory = Integer.parseInt(theoryId);

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_podtema, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Создаем новый адаптер и сохраняем ссылку на него
        podtheoryAdapter = new PodTheoryAdapter(podtheoryList, this);
        recyclerView.setAdapter(podtheoryAdapter);


        progressBar.setVisibility(View.GONE);

        fetchDataFromSupabase();
        return view;
    }

    @Override
    public void onTheoryClick(String theoryId) {
        Intent intent = new Intent(requireActivity(), MaterialActivity.class);
        intent.putExtra("id", theoryId);
        intent.putExtra("id_te", pered_Id);
        recyclerView.setVisibility(View.GONE);
        startActivity(intent);
    }

    private void fetchDataFromSupabase() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://eemqsmnczvagdwcrjdjg.supabase.co/rest/v1/pod_theory?id_te=eq."+id_teory)
                        .header("apikey", ApiKey)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                String jsonData = responseBody.string();
                                parseJsonData(jsonData);
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        } else {
                            Log.e("TAG", "Error: " + response.code() + " " + response.message());
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
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
                PodTheory podTheory = new PodTheory();
                podTheory.setId(jsonObject.getString("id"));
                podTheory.setName(jsonObject.getString("name"));
                podTheory.setId_te(jsonObject.getString("id_te"));


                podtheoryList.add(podTheory);
            }

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    podtheoryAdapter.sortDataById();
                    podtheoryAdapter.notifyDataSetChanged();
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}