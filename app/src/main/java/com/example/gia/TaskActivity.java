package com.example.gia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.gia.PracticeActivity;
import com.example.gia.R;

public class TaskActivity extends AppCompatActivity {

    QuestionFragment questionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


         questionFragment= new QuestionFragment();

        getAndput();



        // Замена фрагмента
        nextFrag();

        LinearLayout back_linear = findViewById(R.id.back_linear);

        back_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity.this, PracticeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getAndput(){
        int varId = getIntent().getIntExtra("var_id", 0);
        int currentQuestionId = getIntent().getIntExtra("id", 0);

        // Создание Bundle и установка var_id в него
        Bundle bundle = new Bundle();
        bundle.putInt("var_id", varId);
        bundle.putInt("id", currentQuestionId);


        questionFragment.setArguments(bundle);
    }

    public void nextFrag(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, questionFragment);
        transaction.commit();
    }

}
