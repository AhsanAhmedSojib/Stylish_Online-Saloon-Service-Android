package com.example.stylish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    TextView textView;
    ProgressBar progressBar;
    int progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        textView=findViewById(R.id.stylishTV);
        progressBar=findViewById(R.id.PB);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                goNext();
            }
        }
        );
        thread.start();
    }
    public void doWork()
    {
        for (progress=20;progress<=100;progress=progress+20)
        {
            try {
                Thread.sleep(500);
                progressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void goNext()
    {
        Intent intent=new Intent(this, AuthenticationOTP.class);
        startActivity(intent);
        finish();
    }

    }

