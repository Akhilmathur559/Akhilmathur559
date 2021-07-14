package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ShowableListMenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterActivity extends AppCompatActivity {
    EditText name, age,mobile;
    ShapeableImageView profilebtn;
    TextView regesterbtn;
    TextView usertxtcick;
//    TextView userskip;
    FirebaseStorage firebaseStorage;
    FirebaseAuth fbAuth;
    FirebaseDatabase database;
    FirebaseFirestore firebaseDatabase;
    FirebaseUser User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
//        SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
//        String FirstTime = preferences.getString("FirstTimeInstall", "");
//        if (FirstTime.equals("yes")){
//            Intent intent=new Intent(UserRegisterActivity.this,HomeActivity.class);
//            startActivity(intent);
//
//        }
//        else{
//            SharedPreferences.Editor editor= preferences.edit();
//            editor.putString("FirstTimeInstall","yes");
//            editor.apply();
//        }
        age = findViewById(R.id.editAge);
        mobile = findViewById(R.id.editMobile);
        name = findViewById(R.id.editName);
        usertxtcick = findViewById(R.id.userprofileclick);
//        userskip=findViewById(R.id.skip);
        regesterbtn = findViewById(R.id.registerbtn);
        fbAuth = FirebaseAuth.getInstance();
        User = fbAuth.getCurrentUser();
        firebaseDatabase = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
//        userskip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(UserRegisterActivity.this,HomeActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//        regesterbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String Name=name.getText().toString();
//                String age=Age.getText().toString();
//                String mobile=Mobile.getText().toString();
//                if (Name.isEmpty()){
//                    Toast.makeText(UserRegisterActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
//                }
//                else if (age.isEmpty()){
//                    Toast.makeText(UserRegisterActivity.this, "Enter age", Toast.LENGTH_SHORT).show();
//                }
//                else if (mobile.isEmpty()){
//                    Toast.makeText(UserRegisterActivity.this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Updatetofirebase();
//                }
//
//            }
//
//        });
//    }
        regesterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name=name.getText().toString();
                String Age=age.getText().toString();
                String Mobile=mobile.getText().toString();
                if (Name.isEmpty()){
                    Toast.makeText(UserRegisterActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }
                else if (Age.isEmpty()){
                    Toast.makeText(UserRegisterActivity.this, "Enter age", Toast.LENGTH_SHORT).show();
                }
                else if (Mobile.isEmpty()){
                    Toast.makeText(UserRegisterActivity.this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
                }
                else {
                    getUserdetails();
                }
            }
        });
//
//    private void Updatetofirebase() {
//        String Name=name.getText().toString();
//        String age=Age.getText().toString();
//        String mobile=Mobile.getText().toString();
//        ProgressDialog dialog=new ProgressDialog(UserRegisterActivity.this);
//        dialog.setTitle("Regester User");
//        dialog.setMessage("Please Wait ");
//        dialog.show();
////        StorageReference uploader=firebaseStorage.getReference("Img1"+new Random().nextInt(50));
////        uploader.putFile()
//        DocumentReference documentReference=firebaseDatabase.collection("notes").document(User.getUid()).collection("userDetails").document();
//        Map<String, Object> data=new HashMap<>();
//        data.put("Name",name);
//        data.put("Age",age);
//        data.put("Mobile",Mobile);
//        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Intent intent=new Intent(UserRegisterActivity.this, HomeActivity.class);
//                intent.putExtra("username",Name);
//                startActivity(intent);
//                Toast.makeText(UserRegisterActivity.this, "Welcome"+ name, Toast.LENGTH_SHORT).show();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(UserRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//
//
    }
    public void getUserdetails(){
        ProgressDialog dialog=new ProgressDialog(UserRegisterActivity.this);
        dialog.setTitle("Regester User");
        dialog.setMessage("Please Wait ");
        dialog.show();
        String Name=name.getText().toString();
        String Age=age.getText().toString();
        String Mobile=mobile.getText().toString();
        FirebaseAuth fbAuth=FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        User=FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference=firebaseFirestore.collection("notes").document(User.getUid()).collection("UserDetails").document();
        Map<String,Object>data=new HashMap<>();
        data.put("Name",Name);
        data.put("Age",Age);
        data.put("Mobile",Mobile);
        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent=new Intent(UserRegisterActivity.this,HomeActivity.class);
                startActivity(intent);

                Toast.makeText(UserRegisterActivity.this, "Welcome "+Name, Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                dialog.dismiss();
                Toast.makeText(UserRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
//    public void getUserdetails(){
//        name=(EditText)findViewById(R.id.editName);
//        age=(EditText)findViewById(R.id.editAge);
//        User=FirebaseAuth.getInstance().getCurrentUser();
//        mobile=(EditText)findViewById(R.id.editMobile);
//        database=FirebaseDatabase.getInstance();
//        DatabaseReference db=database.getReference();
//        dataHolder obj=new dataHolder(name.getText().toString(),age.getText().toString(),mobile.getText().toString());
//
//        db.child(User.getUid()).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Intent intent=new Intent(UserRegisterActivity.this,HomeActivity.class);
//                intent.putExtra("name",dataHolder.class.getName());
//                startActivity(intent);
//                Toast.makeText(UserRegisterActivity.this, "Welome", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull  Exception e) {
//                Toast.makeText(UserRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//
//    }

}
