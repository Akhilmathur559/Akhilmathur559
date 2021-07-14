package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity {
EditText editnote, edittitle;
ImageView savebtn;
FirebaseAuth fbAuth;
    ProgressBar pbar;
FirebaseFirestore firebaseFirestore;
FirebaseUser User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        editnote=findViewById(R.id.editnotetxt);
        edittitle=findViewById(R.id.editTitle);
        savebtn=findViewById(R.id.savebtn);
        fbAuth=FirebaseAuth.getInstance();
        pbar=findViewById(R.id.progresbarsave);
        firebaseFirestore=FirebaseFirestore.getInstance();
        User=fbAuth.getCurrentUser();
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                savebtn.setVisibility(View.INVISIBLE);
                String note=editnote.getText().toString();
                String title=edittitle.getText().toString();

//                if (title.isEmpty()){
//                    Toast.makeText(CreateNoteActivity.this, "Dont leave Title empty", Toast.LENGTH_SHORT).show();
//                    edittitle.requestFocus();
//                    pbar.setVisibility(View.INVISIBLE);
//                    savebtn.setVisibility(View.VISIBLE);
//                }
                if (note.isEmpty()){
                    Toast.makeText(CreateNoteActivity.this, "Dont leave Text empty", Toast.LENGTH_SHORT).show();
                    editnote.requestFocus();
                    pbar.setVisibility(View.INVISIBLE);
                    savebtn.setVisibility(View.VISIBLE);
                }else{
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(User.getUid()).collection("myNotes").document();
                    Map<String, Object> data=new HashMap<>();
                    data.put("title", title);
                    data.put("timestamp",new Timestamp(new Date()));
                    data.put("Content",note);
                    documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateNoteActivity.this, "Note Created Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(CreateNoteActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(CreateNoteActivity.this, "Failed to create your note", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNoteActivity.this,HomeActivity.class));
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Dialog dialog =new Dialog(CreateNoteActivity.this);
        dialog.setContentView(R.layout.progressbar_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=android.R.style.Animation_Dialog;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        String note=editnote.getText().toString();
        String title=edittitle.getText().toString();


//        if (title.isEmpty()){
//            Toast.makeText(CreateNoteActivity.this, "Dont leave Title empty", Toast.LENGTH_SHORT).show();
//            edittitle.requestFocus();
//            pbar.setVisibility(View.INVISIBLE);
//            savebtn.setVisibility(View.VISIBLE);
//        }
        if (note.isEmpty()){
            Toast.makeText(CreateNoteActivity.this, "Dont leave Text empty", Toast.LENGTH_SHORT).show();
            editnote.requestFocus();
            dialog.hide();
        }else{
            DocumentReference documentReference=firebaseFirestore.collection("notes").document(User.getUid()).collection("myNotes").document();
            Map<String, Object> data=new HashMap<>();
            data.put("title", title);
            data.put("Content",note);
            data.put("timestamp",new Timestamp(new Date()));
            documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreateNoteActivity.this, "Note Created Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(CreateNoteActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Toast.makeText(CreateNoteActivity.this, "Failed to create your note", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateNoteActivity.this,HomeActivity.class));
                }
            });
        }

    }
}