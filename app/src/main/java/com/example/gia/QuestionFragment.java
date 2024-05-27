package com.example.gia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.gia.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuestionFragment extends Fragment {

    private int correctAnswers = 0;
    private int incorrectAnswers = 0;

    private int question = 0;
    private static final int TOTAL_QUESTIONS = 20;

    private TextView Body, right_answer, Number;
    private EditText answerEditText;
    private Button nextButton;
    private LinearLayout task_linear, otvet_linear;
    private int varId;
    private String answerr;

    private String decision;

    private ProgressBar progressBar;

    private String ApiKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVzcXJoY2dmYWh6d29qeWR4cmxlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTI0OTY0MDEsImV4cCI6MjAyODA3MjQwMX0.RDUvVE0pc3u2UDfbLN7D88O_MxgPBR0W9LnimiI4mPk";

    private int currentQuestionId;

    private void resetUI() {
        otvet_linear.setVisibility(View.GONE);
        task_linear.setBackgroundResource(R.drawable.border_bg); // Сброс фона
        nextButton.setBackgroundResource(R.drawable.border_bg_full);
        answerEditText.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.diamond));
        right_answer.setText(""); // Сброс текста результата
        answerEditText.setText(""); // Сброс введенного ответа
        nextButton.setText("Ответить"); // Вернуть текст кнопки к изначальному
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        Body = view.findViewById(R.id.Body);
        Number = view.findViewById(R.id.Number);
        right_answer = view.findViewById(R.id.right_answer);
        answerEditText = view.findViewById(R.id.answer);
        nextButton = view.findViewById(R.id.otvet);
        task_linear = view.findViewById(R.id.task_linear);
        otvet_linear = view.findViewById(R.id.otvet_linear);
        progressBar = view.findViewById(R.id.progressBar);
        otvet_linear.setVisibility(View.GONE);

        Bundle args = getArguments();
        if (args != null) {
            varId = args.getInt("var_id", 0);
            currentQuestionId = args.getInt("id", 0);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextButton.getText().equals("Ответить")) {
                    checkAnswer();
                } else {
                    if (question == TOTAL_QUESTIONS) {
                        // Показываем результаты
                        question=0;
                        showResults();
                    } else {
                        // Сброс интерфейса и загрузка следующего вопроса
                        resetUI();
                        currentQuestionId++;
                        fetchQuestionFromSupabase(currentQuestionId);
                    }
                }
            }
        });


        fetchQuestionFromSupabase(currentQuestionId);

        return view;
    }

    private void showResults() {

        String message = "Правильных ответов: " + correctAnswers + "\n" +
                "Неправильных ответов: " + incorrectAnswers;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Результаты")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), PracticeActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }



    private void checkAnswer() {
        String userAnswer = answerEditText.getText().toString();
        if (userAnswer.equals(answerr)) {
            task_linear.setBackgroundResource(R.drawable.border_true);
            nextButton.setBackgroundResource(R.drawable.border_bg_full_true);
            answerEditText.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.truee));
            correctAnswers++;
        } else {
            task_linear.setBackgroundResource(R.drawable.border_false);
            nextButton.setBackgroundResource(R.drawable.border_bg_full_false);
            answerEditText.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.falsee));
            otvet_linear.setVisibility(View.VISIBLE);
            right_answer.setText(decision);
            incorrectAnswers++;
        }
        nextButton.setText("Далее");
    }


    private void fetchQuestionFromSupabase(int currentQuestionId) {
        progressBar.setVisibility(View.VISIBLE);
        Number.setVisibility(View.GONE);
        Body.setVisibility(View.GONE);
        question++;



        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://usqrhcgfahzwojydxrle.supabase.co/rest/v1/Question?id=eq." + currentQuestionId)
                        .header("apikey", ApiKey)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        // Log error
                        Log.e("FetchError", "Failed to fetch question: " + e.getMessage());
                        progressBar.setVisibility(View.GONE);
                        Number.setVisibility(View.VISIBLE);
                        Body.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                String jsonData = responseBody.string();
                                // Log JSON data
                                Log.d("JsonData", "JSON: " + jsonData);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        parseJsonDataAndSetTextView(jsonData);
                                        progressBar.setVisibility(View.GONE);
                                        Number.setVisibility(View.VISIBLE);
                                        Body.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        } else {
                            // Log error
                            Log.e("FetchError", "Failed to fetch question: " + response.code());
                            progressBar.setVisibility(View.GONE);
                            Number.setVisibility(View.VISIBLE);
                            Body.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }


    private void parseJsonDataAndSetTextView(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);

            if (jsonArray.length() > 0) {
                JSONObject questionObject = jsonArray.getJSONObject(0);
                String id = questionObject.getString("id");
                String body = questionObject.getString("body");
                answerr = questionObject.getString("answer");
                decision = questionObject.getString("decision");

                Number.setText("Вопрос "+question);
                Body.setText(body);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
