package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {
TextView Enterbtn;
EditText phone_input;
ProgressBar entrotpprogress;
PhoneAuthProvider phAuth;
String verificationid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        phAuth=PhoneAuthProvider.getInstance();
        Enterbtn=findViewById(R.id.Enterbtn);
        entrotpprogress=findViewById(R.id.getotpProgressbar);
        phone_input=findViewById(R.id.phone_input);
        Enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enterbtn.setText("");
                entrotpprogress.setVisibility(View.VISIBLE);
                String phone=phone_input.getText().toString();
                String pattern="[0-9]{10,10}";
                if (phone.isEmpty()){
                    entrotpprogress.setVisibility(View.INVISIBLE);
                    Enterbtn.setText("Get OTP");
                    Toast.makeText(PhoneActivity.this, "Enter Number", Toast.LENGTH_SHORT).show();
                }
                else if (!phone.matches(pattern)){
                    entrotpprogress.setVisibility(View.INVISIBLE);
                    Enterbtn.setText("Get OTP");
                    Toast.makeText(PhoneActivity.this, "Enter only 10 digit", Toast.LENGTH_SHORT).show();
                }
                else {
                    phAuth.verifyPhoneNumber("+91" + phone, 60, TimeUnit.SECONDS, PhoneActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            Toast.makeText(PhoneActivity.this, "Otp verified", Toast.LENGTH_SHORT).show();
                            String otp=phoneAuthCredential.getSmsCode();
                            PhoneAuthCredential Credential= PhoneAuthProvider.getCredential(verificationid,otp);
                            FirebaseAuth.getInstance().signInWithCredential(Credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull  Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(PhoneActivity.this,HomeActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    }
                                }
                            });

                        }

                        @Override
                        public void onVerificationFailed(@NonNull  FirebaseException e) {
                            entrotpprogress.setVisibility(View.INVISIBLE);
                            Enterbtn.setText("Get OTP");
                            Toast.makeText(PhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCodeSent(@NonNull  String backendotp, @NonNull  PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            Toast.makeText(PhoneActivity.this, "OTP has been sent", Toast.LENGTH_SHORT).show();
                            entrotpprogress.setVisibility(View.INVISIBLE);
                            Enterbtn.setText("Get OTP");
                            verificationid=backendotp;
                            Intent intent=new Intent(PhoneActivity.this,OtpActivity.class);
                            intent.putExtra("mobile",phone);
                            intent.putExtra("backendotp",backendotp);
                            startActivity(intent);
                        }
                    });



                }
            }
        });
    }
}