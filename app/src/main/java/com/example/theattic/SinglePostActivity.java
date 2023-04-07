package com.example.theattic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SinglePostActivity extends AppCompatActivity {
    private ImageView singelImage;

    private TextView singleTitle, singleDesc,postDate,postAuthor;
    String post_key = null;
    private DatabaseReference mDatabase;
    private Button deleteBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        singelImage = findViewById(R.id.singleImageview);
        singleTitle = findViewById(R.id.singleTitle);
        postDate = findViewById(R.id.postDate);
        postAuthor = findViewById(R.id.postAuthor);

        singleDesc = findViewById(R.id.singleDesc);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        post_key = getIntent().getExtras().getString("PostID");
        deleteBtn = findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(post_key).removeValue();
                Intent mainintent = new Intent(SinglePostActivity.this,
                        MainActivity.class);
                startActivity(mainintent);
            }
        });
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("postImage").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                String post_author = (String) dataSnapshot.child("displayName").getValue();
                String post_author_image = (String) dataSnapshot.child("profilePhoto").getValue();
                String post_date = (String) dataSnapshot.child("date").getValue();
                String post_time = (String) dataSnapshot.child("time").getValue();

                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                postDate.setText("Date posted: "+post_date+" "+post_time);
                postAuthor.setText("Author: "+post_author);
                Picasso.with(SinglePostActivity.this).load(post_image).into(singelImage);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)){
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    }
