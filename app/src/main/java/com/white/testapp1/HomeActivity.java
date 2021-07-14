package com.white.testapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth fbAuth;
    ImageView postbtn;
    FloatingActionButton maddtask;
    private int requestcode=123;
    String Username;
//    Button signoutbtn;
    Uri imageUri;
    Bitmap bitmap;
    TextView usertxtclick;
//    TabLayout hometab, persontab, settingtab, cattab;
    RecyclerView recyclerviewmain;
    FirebaseAuth FbAuth=FirebaseAuth.getInstance();
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseUser User=FirebaseAuth.getInstance().getCurrentUser();
    CircleImageView profilebtn;
    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        postbtn=findViewById(R.id.postbtn);
        setUsername();
        firebaseStorage = FirebaseStorage.getInstance();
        maddtask=findViewById(R.id.maddtask);
        usertxtclick=findViewById(R.id.userprofileclick);
        profilebtn=findViewById(R.id.profilebtn);
        fbAuth=FirebaseAuth.getInstance();
        recyclerviewmain=findViewById(R.id.recyclerviewmain);

        firebaseFirestore=FirebaseFirestore.getInstance();
        User=fbAuth.getCurrentUser();

        usertxtclick.setText(getIntent().getStringExtra("name"));
//        hometab=findViewById(R.id.hometab);
//        cattab=findViewById(R.id.cattab);
//        persontab=findViewById(R.id.persontab);
//        settingtab=findViewById(R.id.settingtab);
//        signoutbtn=findViewById(R.id.btnsignout);
//        signoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Signoutbtn();
//            }
//        });
//        loadWithGlide();
        Query query=firebaseFirestore.collection("notes").document(User.getUid()).collection("myNotes").orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<firebasemodel>allusernotes=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
        noteAdapter=new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull  firebasemodel model) {

                int colorcode=getRandomcolor();
                holder.view.setBackgroundColor(holder.itemView.getResources().getColor(colorcode,null));
                holder.notetitle.setText(model.getTitle());
                holder.notecontent.setText(model.getContent());
//                holder.times.setText(model.getTimestamp());
                String docId=noteAdapter.getSnapshots().getSnapshot(position).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(),EditnotesActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("content", model.getContent());
                        intent.putExtra("noteId", docId);
                        v.getContext().startActivity(intent);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                            popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    Intent intent = new Intent(v.getContext(), EditnotesActivity.class);
                                    intent.putExtra("title", model.getTitle());
                                    intent.putExtra("content", model.getContent());
                                    intent.putExtra("noteId", docId);
                                    v.getContext().startActivity(intent);

                                    return true;
                                }
                            });
                            popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(User.getUid()).collection("myNotes").document(docId);
                                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                    return true;
                                }
                            });
                            popupMenu.setGravity(Gravity.TOP);
                            popupMenu.show();

                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_items,parent,false);
                return new NoteViewHolder(view);
            }
        };
        recyclerviewmain.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerviewmain.setLayoutManager(staggeredGridLayoutManager);
        recyclerviewmain.setAdapter(noteAdapter);


        maddtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,CreateNoteActivity.class);
                startActivity(intent);

            }
        });
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                popupMenu.getMenu().add("Logout").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Dialog dialog= new Dialog(HomeActivity.this);
                        dialog.setContentView(R.layout.popuplogout);
                        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        TextView yes=dialog.findViewById(R.id.yes);
                        TextView no=dialog.findViewById(R.id.no);
                        TextView txtexit=dialog.findViewById(R.id.txtexit);
                        txtexit.setText("Do you Realy Want to Logout");
                        txtexit.setPadding(10,30,10,0);
                        txtexit.setTextSize(17);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Signoutbtn();
                            }
                        });
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                        return true;
                    }
                });
                popupMenu.getMenu().add("Profile").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent=new Intent(HomeActivity.this,UserdetailActivity.class);
                        startActivity(intent);
                        finish();
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
//            LayoutInflater inflater=(LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.profile_pic);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;
                dialog.show();
                ImageView imgbtn=dialog.findViewById(R.id.imgbtn);
                imgbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.getMenu().add("Upload").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent,requestcode);
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                StorageReference storageReference=FirebaseStorage.getInstance().getReference();
                                StorageReference imagefolder=storageReference.child("Images");
                                StorageReference imagedelete=imagefolder.child(User.getUid());
                                imagedelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(HomeActivity.this, "Removed success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(HomeActivity.this, "Does not Uploaded any Image", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                return false;
                            }
                        });
                        popupMenu.setGravity(Gravity.CENTER);
                        popupMenu.show();

                    }
                });
            }
        });

        usertxtclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, UserdetailActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerviewmain);

    }
    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull  RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {
            int position=viewHolder.getAdapterPosition();
            String docId=noteAdapter.getSnapshots().getSnapshot(position).getId();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(User.getUid()).collection("myNotes").document(docId);
                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(HomeActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    Snackbar.make(recyclerviewmain,"item deleted",Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    DocumentReference documentReference2 = firebaseFirestore.collection("notes").document(User.getUid()).collection("myNotes").document(docId);
                    documentReference2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(HomeActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    break;
            }

        }
    };

    private int getRandomcolor() {
        List<Integer>colorcode=new ArrayList<>();
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.col4);
        colorcode.add(R.color.col5);
        colorcode.add(R.color.col6);
        colorcode.add(R.color.col7);
        colorcode.add(R.color.col8);
        colorcode.add(R.color.col9);
        colorcode.add(R.color.col10);
        colorcode.add(R.color.col11);
        Random random=new Random();
        int num=random.nextInt(colorcode.size());
        return colorcode.get(num);
    }

    public void Signoutbtn(){
        fbAuth.signOut();
        Toast.makeText(HomeActivity.this, "Signout Successfully", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView notetitle;
        private TextView notecontent;
//        private TextView times;
        LinearLayout notelayout;
        View view;

        public NoteViewHolder(@NonNull  View itemView) {
            super(itemView);
             notetitle=itemView.findViewById(R.id.notetitle);
             notecontent=itemView.findViewById(R.id.notecontent);
             notelayout=itemView.findViewById(R.id.layotnote);
//             times=itemView.findViewById(R.id.times);
             view=itemView.findViewById(R.id.view);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter!=null) {
            noteAdapter.stopListening();
        }
    }

    @Override
    public void onBackPressed() {
        Dialog dialog= new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.popuplogout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView yes=dialog.findViewById(R.id.yes);
       TextView no=dialog.findViewById(R.id.no);
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.super.onBackPressed();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        if (requestCode==requestcode && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(imageUri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                profilebtn.setImageBitmap(bitmap);
                Log.d("papapa", String.valueOf(profilebtn));
            }catch (Exception e){

            }
            Uploadimage(imageUri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void Uploadimage(Uri uri){
        StorageReference reference=firebaseStorage.getReference();
        StorageReference imagesfolder=reference.child("Images");
        StorageReference imageref=imagesfolder.child(User.getUid());
        imageref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(HomeActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                Log.d("photobug",auth.getUid());
                Log.e("photobug", e.getMessage());
                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void loadWithGlide(){
        StorageReference storageReference=firebaseStorage.getReference();
        StorageReference imageShow=storageReference.child("Images");
        StorageReference imageshown=imageShow.child(User.getUid());
        profilebtn=findViewById(R.id.profilebtn);
        Glide.with(HomeActivity.this)
                .load(imageshown)
                .into(profilebtn);
    }
    public  void setUsername() {
        usertxtclick=findViewById(R.id.userprofileclick);

        String docId = firebaseFirestore.collection("notes").document(User.getUid()).getId();
        DocumentReference documentReference = firebaseFirestore.collection("notes").document(User.getUid()).collection("UserDetails").document(docId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                usertxtclick.setText(value.getString("Name"));

            }
        });
    }


}