package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {
EditText username_input, password_input;
TextView loginbtn, forgetbtn, signupbtn;
ImageView googleAuthbtn, phoneAuthbtn;
FirebaseAuth fbAuth;
FirebaseFirestore firestore;
FirebaseUser firebaseUser;
ProgressBar progressBar;
private static final int RC_SIGN_IN = 123;
private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbAuth=FirebaseAuth.getInstance();
        firebaseUser=fbAuth.getCurrentUser();
        username_input=findViewById(R.id.username_input);
        password_input=findViewById(R.id.password_input);
        loginbtn=findViewById(R.id.loginbtn);
        signupbtn=findViewById(R.id.signupbtn);
        progressBar=findViewById(R.id.Progressbar);
        forgetbtn=findViewById(R.id.forgetbtn);
        googleAuthbtn=findViewById(R.id.googleAuthbtn);
        phoneAuthbtn=findViewById(R.id.phoneAuthbtn);
        if (firebaseUser!=null){
            Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginbtn.setText("");
                progressBar.setVisibility(View.VISIBLE);
                String email= username_input.getText().toString();
                String password= password_input.getText().toString();
                if(email.isEmpty())
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    loginbtn.setText("Login");
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    loginbtn.setText("Login");
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else{
                    fbAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
//                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        updateUI(user);
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        loginbtn.setText("Login");
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                                    }
                                }
                            });

                }
            }
        });
        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgetpasswordActivity.class);
                startActivity(intent);
            }
        });
        googleAuthbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog=new ProgressDialog(LoginActivity.this);
                dialog.setTitle("Google login");
                dialog.setMessage("Logging you in ");
                dialog.show();
                SignInWithGoogle();

            }
        });

        phoneAuthbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,PhoneActivity.class);
                startActivity(intent);
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
    public void updateUI(FirebaseUser user){

    }
    public void SignInWithGoogle() {
        if (mGoogleSignInClient==null)
        {// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed: Reason - " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
        CommonVariables.Email = acct.getEmail();
        CommonVariables.UserName = acct.getDisplayName();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fbAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Login with your google account", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //  updateUI(user);
                        } else {
                            Exception ex = task.getException();
                            // If sign in fails, display a message to the user.
                            Log.w("google exception", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }



}