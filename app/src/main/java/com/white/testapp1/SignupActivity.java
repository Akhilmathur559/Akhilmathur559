package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class SignupActivity extends AppCompatActivity {
    EditText username_input, password_input, cpassword_input;
    TextView signupbtn;
    FirebaseAuth fbAuth;
    FirebaseFirestore db;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fbAuth=FirebaseAuth.getInstance();
        username_input=findViewById(R.id.username_input);
        password_input=findViewById(R.id.password_input);
        cpassword_input=findViewById(R.id.cpassword_input);
        signupbtn=findViewById(R.id.signupbtn);
        progressBar=findViewById(R.id.Progressbar);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupbtn.setText("");
                progressBar.setVisibility(View.VISIBLE);
                String email= username_input.getText().toString();
                String password= password_input.getText().toString();
                String cpassword= cpassword_input.getText().toString();
                String passwordPattern="[[0-9][a-z][A-z]]{8,15}";
                if(email.isEmpty())
                {progressBar.setVisibility(View.INVISIBLE);
                    signupbtn.setText("Signup");

                    Toast.makeText(SignupActivity.this, "Enter email", Toast.LENGTH_SHORT).show();

                } else if(password.isEmpty()){progressBar.setVisibility(View.INVISIBLE);
                    signupbtn.setText("Signup");

                    Toast.makeText(SignupActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else if (!password.matches(passwordPattern)){
                    progressBar.setVisibility(View.INVISIBLE);
                    signupbtn.setText("Signup");

                    Toast.makeText(SignupActivity.this, "Password should be 8 character long and atleast one capital and one small letter in it ", Toast.LENGTH_SHORT).show();
                }

                else if (cpassword.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    signupbtn.setText("Signup");

                    Toast.makeText(SignupActivity.this, "Enter confirm password", Toast.LENGTH_SHORT).show();
                }
                else if (!cpassword.matches(password)){
                    progressBar.setVisibility(View.INVISIBLE);
                    signupbtn.setText("Signup");

                    Toast.makeText(SignupActivity.this, "Enter same password", Toast.LENGTH_SHORT).show();
                    }
                else{ fbAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, UserRegisterActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                updateUI(user);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                signupbtn.setText("Signup");

                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignupActivity.this, "Registration failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });


    }
    public void updateUI(FirebaseUser user){

    }
}