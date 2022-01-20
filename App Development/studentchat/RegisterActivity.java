package com.biswa1045.studentchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    GoogleSignInClient googleSignInClient;
    SignInButton btsignin;
    FirebaseAuth firebaseAuth;
    String userID;
    FirebaseFirestore fStore;
    RelativeLayout relative_signin;
    TextView splash_text1,splash_text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        relative_signin = findViewById(R.id.relative_signin);
        splash_text1 = findViewById(R.id.splash_text);
        splash_text2 = findViewById(R.id.splash_text2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splash_text1.setVisibility(View.INVISIBLE);
                splash_text2.setVisibility(View.INVISIBLE);
                relative_signin.setVisibility(View.VISIBLE);
            }
        },3000);
        btsignin = findViewById(R.id.bt_sign_in);
        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(RegisterActivity.this
                    , HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        //int signin option
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // int sign in clint
        googleSignInClient = GoogleSignIn.getClient(RegisterActivity.this
                , googleSignInOptions);
        btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {
                String s = "Google sign in successful";
                displayToast(s);
                //int sign in acctn
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    //check condition
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        , null);
                        //check cre
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            firebaseAuth = FirebaseAuth.getInstance();
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            if (firebaseUser != null) {


                                                userID = firebaseUser.getUid();

                                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                                Map<String, Object> user = new HashMap<>();
                                                String name_google = firebaseUser.getDisplayName();
                                                String  email_google = firebaseUser.getEmail();
                                                String img = firebaseUser.getPhotoUrl().toString();
                                                String  mob = firebaseUser.getPhoneNumber();
                                                if(mob==null){
                                                    mob=" ";
                                                }
                                                user.put("email", email_google);
                                                user.put("username", name_google);

                                                user.put("mobile",mob);
                                                user.put("class","CSE-A");
                                                user.put("image",img);

                                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                                        //  startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                        startActivity(new Intent(RegisterActivity.this
                                                                , HomeActivity.class)
                                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                        displayToast("Authentication successful");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterActivity.this, "User Created Unsuccessful", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            } else {

                                            }

                                        } else {
                                            displayToast("Authentication Failed:" + task.getException()
                                                    .getMessage());
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        finishAffinity();
    }
}