package com.newtry.instagramproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newtry.instagramproject.Adapter.CommentAdapter;
import com.newtry.instagramproject.Model.CommentModel;
import com.newtry.instagramproject.Model.Notification;
import com.newtry.instagramproject.Model.PostModel;
import com.newtry.instagramproject.Model.User;
import com.newtry.instagramproject.R;
import com.newtry.instagramproject.databinding.ActivityComment2Binding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity2 extends AppCompatActivity {
    ActivityComment2Binding binding;
    Intent intent;
    String postId,postedBy;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<CommentModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComment2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        setSupportActionBar(binding.toolbar3);//put the toolbar
        CommentActivity2.this.setTitle("comments");// to set the title of the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to set the back button

        database = FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();

        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

        database.getReference()
                .child("post")
                .child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostModel post= snapshot.getValue(PostModel.class);
                        Picasso.get()
                                .load(post.getPostImage())
                                .placeholder(R.drawable.hanuman_ji)
                                .into(binding.postImage);
                        binding.description.setText(post.getPostDescription());
                        binding.like.setText(post.getPostLike()+"");
                        binding.comment.setText(post.getCommentCount()+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        database.getReference().child("Users").child(postedBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user  = snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfile())
                        .placeholder(R.drawable.hanuman_ji)
                        .into(binding.profileImg);

                binding.Name.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentModel  comment = new CommentModel();
                comment.setCommentBody(binding.etCommentBody.getText().toString());
                comment.setCommentedBy(FirebaseAuth.getInstance().getUid());
                comment.setCommentedAt(new Date().getTime());
                database.getReference()
                        .child("post")
                        .child(postId)
                        .child("comments")
                        .push()// to push it in another node
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //To set the count for the comment
                                database.getReference().child("post")
                                        .child(postId)
                                        .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int commentCount =0;
                                                if(snapshot.exists())
                                                {
                                                    commentCount =snapshot.getValue(Integer.class);
                                                }database.getReference().child("post")
                                                        .child(postId).child("commentCount")
                                                        .setValue(commentCount+1)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                binding.etCommentBody.setText("");
                                                                Toast.makeText(CommentActivity2.this, "commented", Toast.LENGTH_SHORT).show();
                                                                //Notification for comments

                                                                Notification notification = new Notification();
                                                                notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                notification.setNotificationAt(new Date().getTime());
                                                                notification.setPostId(postId);
                                                                notification.setPostedBy(postedBy);
                                                                notification.setType("comment");
                                                                FirebaseDatabase.getInstance().getReference()
                                                                        .child("notification")
                                                                        .child(postedBy)
                                                                        .push()
                                                                        .setValue(notification);


                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            }
                        });
            }
        });
        CommentAdapter adapter = new CommentAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.rvComment.setAdapter(adapter);
        binding.rvComment.setLayoutManager(manager);
        database.getReference().child("post")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                           for(DataSnapshot snapshot1 :snapshot.getChildren())
                           {
                               CommentModel model = snapshot1.getValue(CommentModel.class);
                               list.add(model);
                           }
                           adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}