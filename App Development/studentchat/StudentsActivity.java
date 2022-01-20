package com.biswa1045.studentchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentsActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar_student)
    ProgressBar progressBar;

    @BindView(R.id.allstudent_rcy)
    RecyclerView rcv;

    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        ButterKnife.bind(this);
        db = FirebaseFirestore.getInstance();
        init();


        getcartList();
        findViewById(R.id.back_s).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenteed = new Intent(StudentsActivity.this, HomeActivity.class);
                startActivity(intenteed);
                finish();
            }
        });
    }
    private void init() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        rcv.setLayoutManager(gridLayoutManager);


    }


    private void getcartList() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        Query query = db.collection("users");

        FirestoreRecyclerOptions<retrieveModel> response = new FirestoreRecyclerOptions.Builder<retrieveModel>()
                .setQuery(query, retrieveModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<retrieveModel, StudentsActivity.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(StudentsActivity.FriendsHolder holder, int position, retrieveModel model) {
                progressBar.setVisibility(View.GONE);


                String name_str = model.getUsername();
                holder.name.setText(name_str);


                Glide.with(getApplicationContext())
                        .load(model.getImage())
                           .error(R.drawable.ic_launcher_background)
                        .into(holder.img);



            }

            @Override
            public StudentsActivity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_allstudent, group, false);

                return new StudentsActivity.FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        rcv.setAdapter(adapter);


    }


    public class FriendsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_allstudent_item)
        ImageView img;

        @BindView(R.id.name_allstudent_item)
        TextView name;


        public FriendsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onBackPressed() {
        Intent in = new Intent(StudentsActivity.this,HomeActivity.class);
        startActivity(in);
        finish();
    }
}