package com.biswa1045.studentchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    FirebaseFirestore fStore;
        EditText msg;
    DatabaseReference myRef;
    private final int PICK_IMAGE_REQUEST = 10;
    String userid;
    Dialog dialog;
    String name_fs;
    TextView name;
    ImageView img,msg_doc;
    StorageReference storageReference;
    Uri doc_uri;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<chatData> list;

    chatAdapter adapter;

    FirebaseUser user;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dialog = new Dialog(this);
        name = findViewById(R.id.name_home);
        recyclerView = findViewById(R.id.rcy_home);
        progressBar = findViewById(R.id.progessbar_home);
        storageReference = FirebaseStorage.getInstance().getReference("documents");
        ImageView down = findViewById(R.id.down_img);
        img = findViewById(R.id.image_home);
        LinearLayout l = findViewById(R.id.hidden_layout);
        fStore = FirebaseFirestore.getInstance();
         user = FirebaseAuth.getInstance().getCurrentUser();
        msg = findViewById(R.id.msg);
        msg_doc = findViewById(R.id.msg_doc);
        userid = user.getUid();



        name.setText(user.getDisplayName());
        Glide.with(getApplicationContext())
                .load(user.getPhotoUrl())
                .error(R.drawable.ic_launcher_background)
                .into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(in);
                finish();
            }
        });
        LinearLayoutManager manager= new LinearLayoutManager(this);
        down.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             if(l.getVisibility()==View.VISIBLE){
                 l.setVisibility(View.INVISIBLE);
                 down.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
             }else{
                 l.setVisibility(View.VISIBLE);
                 down.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
             }

          }
      });
      findViewById(R.id.documrnt).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent in = new Intent(HomeActivity.this,DocActivity.class);
              startActivity(in);
              finish();
          }
      });
        findViewById(R.id.student).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(HomeActivity.this,StudentsActivity.class);
                startActivity(in2);
                finish();
            }
        });
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String msg_str = msg.getText().toString().trim();


                String date_str= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String time_str= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());;
                String time_id= new SimpleDateFormat("dd:MM:yyyy HH:mm:ss", Locale.getDefault()).format(new Date());;

                myRef = FirebaseDatabase.getInstance().getReference().child("message").child(time_id);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {

                            myRef.child("name").setValue(user.getDisplayName());
                            myRef.child("userid").setValue(userid);
                            myRef.child("msg").setValue(msg_str);
                            myRef.child("time").setValue(time_str);
                            myRef.child("date").setValue(date_str);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                myRef.addListenerForSingleValueEvent(eventListener);



                reference.orderByChild("time").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {

                            chatData data=dataSnapshot.getValue(chatData.class);
                            list.add(data);
                        }
                        adapter=new chatAdapter(list,HomeActivity.this);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(HomeActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();
                    }
                });



                
            }
        });
        msg_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDoc();
            }
        });



        reference = FirebaseDatabase.getInstance().getReference().child("message");

        list=new ArrayList<>();
        manager.setReverseLayout(true);
        manager.setStackFromEnd(false);
        recyclerView.setLayoutManager(manager);
        adapter=new chatAdapter(list, this);
        recyclerView.setHasFixedSize(true);
        reference.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    chatData data=dataSnapshot.getValue(chatData.class);
                    list.add(data);
                }
                adapter=new chatAdapter(list,HomeActivity.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void SelectDoc()
    {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file from here..."), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            doc_uri = data.getData();
            showpopup();

        }


    }
    private String getfileExt(Uri docuri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(docuri));
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    public void showpopup(){

        TextView name;
        Button upload;

        dialog.setContentView(R.layout.custom_dialog);
        name =  dialog.findViewById(R.id.upload_name);
        upload = dialog.findViewById(R.id.upload_doc);

       upload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String name_str = name.getText().toString().trim();
               uploaddoc(name_str);
               dialog.dismiss();

           }
       });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void uploaddoc(String name){
        Date time = Calendar.getInstance().getTime();
        if(doc_uri != null){
            ProgressDialog progressDialog
                    = new ProgressDialog(HomeActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref =storageReference.child("doc"+time+"."+getfileExt(doc_uri));
            ref.putFile(doc_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(@NonNull Uri uri) {
                            String firbase_doc_url = uri.toString();
                            DocumentReference documentReference_doc = fStore.collection("document").document(""+time);
                            Map<String, Object> data = new HashMap<>();
                            data.put("name", name);
                            data.put("userid", userid);
                            data.put("link", firbase_doc_url);
                            data.put("owner_name", user.getDisplayName());





                            documentReference_doc.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Document uploaded",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    });


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext()," Document not upload, Try Again Later",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress
                            = (100.0
                            * snapshot.getBytesTransferred()
                            / snapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (double)progress + "%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"select Document to upload",Toast.LENGTH_SHORT).show();
            //    loadingDialog.dismiss();

        }
    }
}