package com.example.stylish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email=findViewById(R.id.EmailET);
        password=findViewById(R.id.PasswordET);
        login=findViewById(R.id.LoginBTN);
        register=findViewById(R.id.registerTV);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogIn.this, Register.class);
                startActivity(intent);
            }
        });
        mAuth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mail=email.getText().toString();
                final String pass=password.getText().toString();

                if (mail.isEmpty() || pass.isEmpty())
                {
                    showMessage("Please verify all the fields");
                }
                else
                {
                    signIn(mail,pass);
                }
            }
        });
    }

    private void signIn(String mail, String pass)
    {
       mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful())
               {
                   login.setVisibility(View.VISIBLE);
                   updateUi();
               }
               else
               {
                   showMessage(task.getException().getMessage());
               }
           }
       });
    }

    private void updateUi()
    {
        Toast.makeText(LogIn.this, "Logging in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LogIn.this, WeightSum.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

}
