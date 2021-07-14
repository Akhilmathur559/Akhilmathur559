package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Activity extends AppCompatActivity {
    TextView txtnotes, txtapp;
    RelativeLayout relsplash;
    Animation txtanim, layoutanim;
    FirebaseAuth fbAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fbAuth=FirebaseAuth.getInstance();
        firebaseUser=fbAuth.getCurrentUser();
        //txtanim= AnimationUtils.loadAnimation(Splash_Activity.this,R.anim.up_animatiom);
        //layoutanim= AnimationUtils.loadAnimation(Splash_Activity.this,R.anim.bottom_animatiom);
        txtnotes=findViewById(R.id.txtnotes);
        txtapp=findViewById(R.id.txtapp);
        relsplash=findViewById(R.id.relsplash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                relsplash.setVisibility(View.VISIBLE);
                relsplash.animate().translationY(0).setDuration(9200).setStartDelay(2000);;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtnotes.setVisibility(View.VISIBLE);
                        txtapp.setVisibility(View.VISIBLE);
                        txtnotes.animate().translationX(2500).setDuration(4000).setStartDelay(2000);
                        txtapp.animate().translationX(-2500).setDuration(5000).setStartDelay(2000);;
                    }
                },0);
            }
        },0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash_Activity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        },2000);

    }


}