package com.edumentor.edumentor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.edumentor.edumentor.databinding.ActivityFrontPageBinding;
import com.edumentor.edumentor.databinding.ActivityRegisterBinding;

public class FrontPageActivity extends AppCompatActivity {

    ActivityFrontPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding= ActivityFrontPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.converseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(FrontPageActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        binding.botBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(FrontPageActivity.this, BotActivity.class);
                startActivity(i);
            }
        });

        binding.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(FrontPageActivity.this, VideoActivity.class);
                startActivity(i);
            }
        });


        binding.toDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(FrontPageActivity.this, TodoActivity.class);
                startActivity(i);
            }
        });


        binding.profileEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(FrontPageActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

