package com.example.gia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class PracticeActivity extends AppCompatActivity {

    Button var1, var2, var3, var4, var5, va6;
    Integer var;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        int randomNumber = generateRandomNumber(1, 5);

        Button var1 = findViewById(R.id.var1);
        Button var2 = findViewById(R.id.var2);
        Button var3 = findViewById(R.id.var3);
        Button var4 = findViewById(R.id.var4);
        Button var5 = findViewById(R.id.var5);
        Button var6 = findViewById(R.id.var6);


        LinearLayout backk_linear = findViewById(R.id.backk_linear);


        backk_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PracticeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        var1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PracticeActivity.this, TaskActivity.class);
                intent.putExtra("var_id", 1);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        var2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PracticeActivity.this, TaskActivity.class);
                intent.putExtra("var_id", 2);
                intent.putExtra("id", 21);
                startActivity(intent);
            }
        });

        var3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PracticeActivity.this, TaskActivity.class);
                intent.putExtra("var_id", 3);
                intent.putExtra("id", 41);
                startActivity(intent);
            }
        });

        var4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PracticeActivity.this, TaskActivity.class);
                intent.putExtra("var_id", 4);
                intent.putExtra("id", 61);
                startActivity(intent);
            }
        });

        var5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PracticeActivity.this, TaskActivity.class);
                intent.putExtra("var_id", 5);
                intent.putExtra("id", 81);
                startActivity(intent);
            }
        });

        var6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PracticeActivity.this, TaskActivity.class);

                switch (randomNumber) {
                    case 1:
                        intent.putExtra("var_id", 1);
                        intent.putExtra("id", 1);
                        break;
                    case 2:
                        intent.putExtra("var_id", 2);
                        intent.putExtra("id", 21);
                        break;
                    case 3:
                        intent.putExtra("var_id", 3);
                        intent.putExtra("id", 41);
                        break;
                    case 4:
                        intent.putExtra("var_id", 4);
                        intent.putExtra("id", 61);
                        break;
                    case 5:
                        intent.putExtra("var_id", 5);
                        intent.putExtra("id", 81);
                        break;
                    default:
                        intent.putExtra("var_id", 0);
                        intent.putExtra("id", 0);
                        break;
                }
                startActivity(intent);
            }
        });

    }

    private int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

}





