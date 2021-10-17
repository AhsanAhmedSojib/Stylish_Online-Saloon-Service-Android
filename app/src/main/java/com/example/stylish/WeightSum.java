package com.example.stylish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WeightSum extends AppCompatActivity {
    Button btn1, btn2, btn3, btn4, btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_sum);

        btn1=findViewById(R.id.saloonBTN);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeightSum.this, "Hair Saloon", Toast.LENGTH_SHORT).show();
            }
        });
        btn2=findViewById(R.id.spaBTN);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeightSum.this, "Spa", Toast.LENGTH_SHORT).show();
            }
        });
        btn3=findViewById(R.id.NailBTN);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeightSum.this, "Nail Saloon", Toast.LENGTH_SHORT).show();
            }
        });
        btn4=findViewById(R.id.TattooBTN);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeightSum.this, "Tattoo Shop", Toast.LENGTH_SHORT).show();
            }
        });
        btn5=findViewById(R.id.ParlourBTN);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeightSum.this, "Beauty Parlour", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
