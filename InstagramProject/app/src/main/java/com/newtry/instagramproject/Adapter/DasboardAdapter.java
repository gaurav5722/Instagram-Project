package com.newtry.instagramproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newtry.instagramproject.Activity.CommentActivity2;
import com.newtry.instagramproject.Model.DasboardModel;
import com.newtry.instagramproject.Model.Notification;
import com.newtry.instagramproject.Model.PostModel;
import com.newtry.instagramproject.Model.User;
import com.newtry.instagramproject.R;
import com.newtry.instagramproject.databinding.DasboardRvSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class DasboardAdapter extends RecyclerView.Adapter<DasboardAdapter.viewHolder> {
    Context context;
    //    ArrayList<DasboardModel> list;
    //instead of the dasboardModel we have used Post Model
    ArrayList<PostModel> list;

    public DasboardAdapter(Context context, ArrayList<PostModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dasboard_rv_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PostModel post = list.get(position);
//        holder.name.setText(model.getName());
//        holder.about.setText(model.getAbout());
//        holder.postImg.setImageResource(model.getPostImg());
//        holder.profile.setImageResource(model.getProfile());
//        holder.like.setText(model.getLike());
//        holder.comment.setText(model.getComment());
//        holder.share.setText(model.getShare());
//        holder.save.setImageResource(model.getSave());
        //As the above data we have to set  by using the firebase
        Picasso.get()
                .load(post.getPostImage())//load from database
                .placeholder(R.drawable.hanuman_ji)
                .into(holder.binding.postImg); //set the loaded image
        holder.binding.like.setText(post.getPostLike() + "");
        String description = post.getPostDescription();
        holder.binding.comment.setText(post.getCommentCount()+"");
        if (description.equals("")) {
            holder.binding.tvDescription.setVisibility(View.GONE);
        } else {
            holder.binding.tvDescription.setText(post.getPostDescription());
            holder.binding.tvDescription.setVisibility(View.VISIBLE);
        }
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(post.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfile())
                                .placeholder(R.drawable.cartoon)
                                .into(holder.binding.profileImg);
                        holder.binding.name.setText(user.getName());
                        holder.binding.profession.setText(user.getProfession());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("post")
                .child(post.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_red, 0, 0, 0);
                        } else {
                            holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("post")
                                            .child(post.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("post")
                                                            .child(post.getPostId())
                                                            .child("postLike")
                                                            .setValue(post.getPostLike() + 1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_red, 0, 0, 0);
                                                                //Notification

                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setPostId(post.getPostId());
                                                                    notification.setPostedBy(post.getPostedBy());
                                                                    notification.setType("like");
                                                                    //saveing in the database
                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(post.getPostedBy())
                                                                            .push()
                                                                            .setValue(notification);
                                                                }
                                                            });

                                                }
                                            });
                                }
                            });
                        }
                        // to send the data and transfer the control on the commentActivity

                        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, CommentActivity2.class);//to transfer control from fragment to activity
                                intent.putExtra("postId",post.getPostId());
                                intent.putExtra("postedBy",post.getPostedBy());
                                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        //        ImageView profile,postImg,save;
//        TextView name,about,like,comment,share;
        DasboardRvSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
//            profile=itemView.findViewById(R.id.profileImg);
//            postImg = itemView.findViewById(R.id.postImg);
//            save= itemView.findViewById(R.id.save);
//            name=itemView.findViewById(R.id.name);
//            about=itemView.findViewById(R.id.profession);
//            like= itemView.findViewById(R.id.like);
//            comment=itemView.findViewById(R.id.comment);
//            share= itemView.findViewById(R.id.share);
            binding = DasboardRvSampleBinding.bind(itemView);
        }
    }
}
