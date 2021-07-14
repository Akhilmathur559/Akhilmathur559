package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserdetailActivity extends AppCompatActivity {
    EditText editName, editAge,editMobile;
    CircleImageView profilebtn;
    ImageView savebtn;
    TextView usertxtcick;
    Uri imageUri;
    String imguri;
    ProgressBar progressbar1;
    Bitmap bitmap;
    public String Updatetxt;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth fbAuth=FirebaseAuth.getInstance();
    FirebaseFirestore firebaseDatabase =FirebaseFirestore.getInstance();
    FirebaseUser User=FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetail);
        editAge=findViewById(R.id.editAge);
        editMobile=findViewById(R.id.editMobile);
        editName=findViewById(R.id.editName);
        usertxtcick=findViewById(R.id.userprofileclick);
        savebtn=findViewById(R.id.savebtn);
        fbAuth=FirebaseAuth.getInstance();
        User=fbAuth.getCurrentUser();
        progressbar1=findViewById(R.id.progressbar1);
        profilebtn=findViewById(R.id.profilebtn);
        firebaseDatabase=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        retrieveData();
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=editName.getText().toString();
                String age=editAge.getText().toString();
                String mobile=editMobile.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(UserdetailActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }
                else if (age.isEmpty()){
                    Toast.makeText(UserdetailActivity.this, "Enter age", Toast.LENGTH_SHORT).show();
                }
                else if (mobile.isEmpty()){
                    Toast.makeText(UserdetailActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
                else {
                    Updatetofirebase();
                }
            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
    }

    private void Updatetofirebase() {
        ProgressDialog dialog=new ProgressDialog(UserdetailActivity.this);
        dialog.setTitle("Regester User");
        dialog.setMessage("Please Wait ");
        dialog.show();
        String name=editName.getText().toString();
        String age=editAge.getText().toString();
        String mobile=editMobile.getText().toString();
        String docId=firebaseDatabase.collection("notes").document(User.getUid()).getId();
        DocumentReference documentReference=firebaseDatabase.collection("notes").document(User.getUid()).collection("UserDetails").document(docId);
        Map<String, Object> data=new HashMap<>();
        data.put("Name",name);
        data.put("Age",age);
        data.put("Mobile",mobile);
        Updatetxt=name;
        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UserdetailActivity.this, "Detail saved success", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserdetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }
    public  void retrieveData(){
        editAge=findViewById(R.id.editAge);
        editMobile=findViewById(R.id.editMobile);
        editName=findViewById(R.id.editName);
        usertxtcick=findViewById(R.id.userprofileclick);
        String docId=firebaseDatabase.collection("notes").document(User.getUid()).getId();
        DocumentReference documentReference=firebaseDatabase.collection("notes").document(User.getUid()).collection("UserDetails").document(docId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable  DocumentSnapshot value, @Nullable  FirebaseFirestoreException error) {
                editName.setText(value.getString("Name"));
                usertxtcick.setText(value.getString("Name"));
                editAge.setText(value.getString("Age"));
                editMobile.setText(value.getString("Mobile"));

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(imageUri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                profilebtn.setImageBitmap(bitmap);
            }catch (Exception e){

            }
            Uploadimage(imageUri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Uploadimage(Uri imageUri) {
        progressbar1 = findViewById(R.id.progressbar1);
        progressbar1.setVisibility(View.VISIBLE);

        StorageReference reference = firebaseStorage.getReference();
        StorageReference imagesfolder = reference.child("Images");
        StorageReference imageref = imagesfolder.child(User.getUid());
        imageref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressbar1.setVisibility(View.INVISIBLE);
                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String docId=firebaseDatabase.collection("notes").document(User.getUid()).getId();
                        DocumentReference documentReference=firebaseDatabase.collection("notes").document(User.getUid()).collection("UserDetails").document(docId);
                        Map<String, Object> data=new HashMap<>();
                        data.put("imgUrl",uri);
                        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UserdetailActivity.this, "uri received", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserdetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(UserdetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(UserdetailActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressbar1.setVisibility(View.INVISIBLE);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                Log.d("photobug", auth.getUid());
                Log.e("photobug", e.getMessage());
                Toast.makeText(UserdetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
       Intent intent=new Intent(UserdetailActivity.this,HomeActivity.class);
       intent.putExtra("name",Updatetxt);
       startActivity(intent);
       finish();
    }
}