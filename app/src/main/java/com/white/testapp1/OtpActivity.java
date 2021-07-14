package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class OtpActivity extends AppCompatActivity {
    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    TextView Enterbtn, otptxt, resendotpbtn;
    ProgressBar verifyotpprogress;
    String backend;
    PhoneAuthProvider phAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        phAuth=PhoneAuthProvider.getInstance();
        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        otp5=findViewById(R.id.otp5);
        otp6=findViewById(R.id.otp6);
        Enterbtn=findViewById(R.id.Enterbtn);
        verifyotpprogress=findViewById(R.id.verifyotpProgressbar);
        resendotpbtn=findViewById(R.id.resendotpbtn);
        otptxt=findViewById(R.id.otptxt);
        otptxt.setText(String.format("+91-%s", getIntent().getStringExtra("mobile")));
        backend=getIntent().getStringExtra("backendotp");
        resendotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog=new ProgressDialog(OtpActivity.this);
                dialog.setTitle("OTP Verification");
                dialog.setMessage("verifying otp ");
                dialog.show();
                phAuth.verifyPhoneNumber("+91" + getIntent().getStringExtra("mobile"), 60, TimeUnit.SECONDS, OtpActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }

                    @Override
                    public void onCodeSent(@NonNull  String newbackendotp, @NonNull  PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        backend=newbackendotp;
                        Toast.makeText(OtpActivity.this, "OTP sent again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VerifyOtp();

            }
        });
        moveNextOtpNumber();

    }

    private void moveNextOtpNumber() {
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    otp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    otp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()){
                    otp1.requestFocus();
                }

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    otp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()){
                    otp2.requestFocus();
                }

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    otp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()){
                    otp3.requestFocus();
                }

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    otp6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()){
                    otp4.requestFocus();
                }

            }
        });
        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    VerifyOtp();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()){
                    otp5.requestFocus();
                }


            }
        });





    }
    public void VerifyOtp(){
        ProgressDialog dialog=new ProgressDialog(OtpActivity.this);
        dialog.setTitle("OTP Verification");
        dialog.setMessage("verifying otp ");
        dialog.show();

        String sotp1=otp1.getText().toString();
        String sotp2=otp2.getText().toString();
        String sotp3=otp3.getText().toString();
        String sotp4=otp4.getText().toString();
        String sotp5=otp5.getText().toString();
        String sotp6=otp6.getText().toString();
        if (!sotp1.isEmpty() && !sotp2.isEmpty() && !sotp3.isEmpty() && !sotp4.isEmpty() && !sotp5.isEmpty() && !sotp6.isEmpty())
        {String otp=sotp1+sotp2+sotp3+sotp4+sotp5+sotp6;
            if (backend!=null){
                PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(backend,otp);
                Log.i("backend",backend);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(OtpActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(OtpActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{

                            dialog.dismiss();

                            Toast.makeText(OtpActivity.this, "You have Entered wrong OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {

                dialog.dismiss();
                Toast.makeText(OtpActivity.this, "Check your Internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        }
        else{

            dialog.dismiss();
            Toast.makeText(OtpActivity.this, "Enter all fields", Toast.LENGTH_SHORT).show();

        }
    }
}