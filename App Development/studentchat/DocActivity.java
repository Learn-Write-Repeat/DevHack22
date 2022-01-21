package com.biswa1045.studentchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocActivity extends AppCompatActivity {
    @BindView(R.id.progress_bar_doc)
    ProgressBar progressBar;

    @BindView(R.id.doc_rcy)
    RecyclerView rcv;

    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        ButterKnife.bind(this);
        db = FirebaseFirestore.getInstance();
        init();

        findViewById(R.id.back_d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenteed = new Intent(DocActivity.this, HomeActivity.class);
                startActivity(intenteed);
                finish();
            }
        });
        getcartList();
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(DocActivity.this,HomeActivity.class);
        startActivity(in);
        finish();
    }
    private void init() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        rcv.setLayoutManager(gridLayoutManager);


    }


    private void getcartList() {



        Query query = db.collection("document");

        FirestoreRecyclerOptions<retrieveModelDoc> response = new FirestoreRecyclerOptions.Builder<retrieveModelDoc>()
                .setQuery(query, retrieveModelDoc.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<retrieveModelDoc, DocActivity.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(DocActivity.FriendsHolder holder, int position, retrieveModelDoc model) {
                progressBar.setVisibility(View.GONE);


                String name_str = model.getName();
                String link_str = model.getLink();
                String owner_str = model.getOwner_name();
                holder.name.setText(name_str);
                holder.owner.setText(owner_str);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name="owner_str"+"name_str"+".pdf";
                        Intent intent = new Intent(getApplicationContext(),pdfActivity.class);
                        intent.putExtra("LINK", link_str);
                        intent.putExtra("NAME", name);
                        startActivity(intent);
                    }
                });





            }

            @Override
            public DocActivity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_document, group, false);

                return new DocActivity.FriendsHolder(view);
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


        @BindView(R.id.name_document_item)
        TextView name;
        @BindView(R.id.name_owner_item)
        TextView owner;


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
}