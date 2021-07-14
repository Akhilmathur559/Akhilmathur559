package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class EditnotesActivity extends AppCompatActivity {
    EditText updatecontent, updatetitle;
    ImageView updatebtn;
    FirebaseAuth fbAuth;
    ProgressBar pbar;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnotes);
        updatecontent=findViewById(R.id.updatecontent);
        updatetitle=findViewById(R.id.updateTitle);
        updatebtn=findViewById(R.id.updatebtn);
        fbAuth=FirebaseAuth.getInstance();
        pbar=findViewById(R.id.progresbarsave);
        firebaseFirestore=FirebaseFirestore.getInstance();
        User=fbAuth.getCurrentUser();
        Intent data=getIntent();
        updatetitle.setText(data.getStringExtra("title"));
        updatecontent.setText(data.getStringExtra("content"));
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                updatebtn.setVisibility(View.INVISIBLE);
                String note=updatecontent.getText().toString();
                String title=updatetitle.getText().toString();

                if (title.isEmpty()){
                    Toast.makeText(EditnotesActivity.this, "Dont leave Title empty", Toast.LENGTH_SHORT).show();
                    updatetitle.requestFocus();
                    pbar.setVisibility(View.INVISIBLE);
                    updatebtn.setVisibility(View.VISIBLE);
                }
                else if (note.isEmpty()){
                    Toast.makeText(EditnotesActivity.this, "Dont leave Text empty", Toast.LENGTH_SHORT).show();
                    updatecontent.requestFocus();
                    pbar.setVisibility(View.INVISIBLE);
                    updatebtn.setVisibility(View.VISIBLE);
                }else{
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(User.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String, Object> notedata=new HashMap<>();
                    notedata.put("title", title);
                    notedata.put("Content",note);
                    notedata.put("timestamp",new Timestamp(new Date()));
                    documentReference.set(notedata).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditnotesActivity.this, "Note Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(EditnotesActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditnotesActivity.this, "Failed to Update your note", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditnotesActivity.this,HomeActivity.class));
                        }
                    });
                }
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        Intent data=getIntent();
//        pbar.setVisibility(View.VISIBLE);
//        updatebtn.setVisibility(View.INVISIBLE);
//        String note=updatecontent.getText().toString();
//        String title=updatetitle.getText().toString();
//
////        if (title.isEmpty()){
////            Toast.makeText(EditnotesActivity.this, "Dont leave Title empty", Toast.LENGTH_SHORT).show();
////            updatetitle.requestFocus();
////            pbar.setVisibility(View.INVISIBLE);
////            updatebtn.setVisibility(View.VISIBLE);
////        }
//        if (note.isEmpty()){
//            Toast.makeText(EditnotesActivity.this, "Dont leave Text empty", Toast.LENGTH_SHORT).show();
//            updatecontent.requestFocus();
//            pbar.setVisibility(View.INVISIBLE);
//            updatebtn.setVisibility(View.VISIBLE);
//        }else{
//            DocumentReference documentReference=firebaseFirestore.collection("notes").document(User.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
//            Map<String, Object> notedata=new HashMap<>();
//            notedata.put("title", title);
//            notedata.put("Content",note);
//            documentReference.set(notedata).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Toast.makeText(EditnotesActivity.this, "Note Updated Successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(EditnotesActivity.this,HomeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(EditnotesActivity.this, "Failed to Update your note", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(EditnotesActivity.this,HomeActivity.class));
//                }
//            });
//        }
//    }
}