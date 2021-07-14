package com.white.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetpasswordActivity extends AppCompatActivity {
EditText username_input;
TextView submitbtn;
FirebaseAuth fbAuth;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        fbAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.Progressbar);
        username_input=findViewById(R.id.username_input);
        submitbtn=findViewById(R.id.submitbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitbtn.setText("");
                progressBar.setVisibility(View.VISIBLE);
                String email=username_input.getText().toString();
                if(email.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    submitbtn.setText("Reset Password");

                    Toast.makeText(ForgetpasswordActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                }
                else{fbAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(ForgetpasswordActivity.this,task ->  {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgetpasswordActivity.this, "Recovery password has been set to your registered email id ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetpasswordActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                submitbtn.setText("Reset Password");

                                Toast.makeText(ForgetpasswordActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });
    }
}