package com.newtry.instagramproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newtry.instagramproject.Model.Follow;
import com.newtry.instagramproject.Model.Notification;
import com.newtry.instagramproject.Model.User;
import com.newtry.instagramproject.R;
import com.newtry.instagramproject.databinding.UserSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder>{
    Context context;
    ArrayList<User> list;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
    User user = list.get(position);
        Picasso.get()
                .load(user.getProfile())
                .placeholder(R.drawable.cartoon)
                .into(holder.binding.profileImg);
        holder.binding.name.setText(user.getName());
        holder.binding.profession.setText(user.getProfession());
        //Checking whether every user of the instagram has a current user as follower and if it has the current user as follower than the snapshot exists and the button must be changed and disabled.
        //and if it not exists than the onclick listener must be applied on each button
        FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                                .child(user.getUserID())
                                        .child("followers")
                                                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.binding.btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_active_btn));
                            holder.binding.btnFollow.setText("Following");
                            holder.binding.btnFollow.setTextColor(context.getResources().getColor(R.color.gray));
                            holder.binding.btnFollow.setEnabled(false);
                        }else{
                            holder.binding.btnFollow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //FriendModel has been not used now instead use  Follow
                                    //FriendAdapter is not used now but instead of that FollowAdapter is used
                                    //friend_rv_sample is used as a layout file
                                    Follow follow = new Follow();//calling empty constructor of the follow
                                    follow.setFollowedAt(new Date().getTime()); //setting the followed time
                                    follow.setFollowedBy(FirebaseAuth.getInstance().getUid());//setting the user that has followed

                                    //The logic for storeing data in the database for the user who has followed the another user
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(user.getUserID())
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Users")
                                                            .child(user.getUserID())
                                                            .child("followerCount")
                                                            .setValue(user.getFollowerCount()+1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.btnFollow.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_active_btn));
                                                                    holder.binding.btnFollow.setText("Following");
                                                                    holder.binding.btnFollow.setTextColor(context.getResources().getColor(R.color.gray));
                                                                    holder.binding.btnFollow.setEnabled(false);
                                                                    Toast.makeText(context, "You followed"+user.getName(), Toast.LENGTH_SHORT).show();
                                                                //Notification

                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setType("follow");
                                                                    FirebaseDatabase.getInstance().getReference().child("notification")
                                                                            .child(user.getUserID())
                                                                            .push()
                                                                            .setValue(notification);

                                                                }
                                                            });
                                                }
                                            });



                                }
                            });
                        }
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

    public class viewHolder extends RecyclerView.ViewHolder{
        UserSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
           binding = UserSampleBinding.bind(itemView);
        }
    }
}
