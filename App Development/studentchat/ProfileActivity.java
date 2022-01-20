package com.biswa1045.studentchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore fStore;
    String userid;

    TextView name,email,class_d,mobile;
    ImageView img_p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = findViewById(R.id.name_p);
        img_p = findViewById(R.id.img_p);
        email = findViewById(R.id.email_p);
        class_d = findViewById(R.id.class_p);
        mobile = findViewById(R.id.mobile_p);
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userid = user.getUid();




        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(),gso);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        finishAffinity();
                    }
                });

            }
        });
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        mobile.setText(user.getPhoneNumber());
        class_d.setText("CSE-A");
        Glide.with(getApplicationContext())
                .load(user.getPhotoUrl())
                .error(R.drawable.ic_launcher_background)
                .into(img_p);
        findViewById(R.id.back_p).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenteed = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intenteed);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent in = new Intent(ProfileActivity.this,HomeActivity.class);
        startActivity(in);
        finish();
    }

}