package com.newtry.instagramproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newtry.instagramproject.Activity.CommentActivity2;
import com.newtry.instagramproject.Model.Notification;
//import com.newtry.instagramproject.Model.NotificationModel;
import com.newtry.instagramproject.Model.User;
import com.newtry.instagramproject.R;
import com.newtry.instagramproject.databinding.Notification2sampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{
//    ArrayList<NotificationModel> list;
    ArrayList<Notification> list;
    Context context;

    public NotificationAdapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification2sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
       // NotificationModel model= list.get(position);
//        holder.profileImg.setImageResource(model.getProfile());
//        holder.notification.setText(Html.fromHtml(model.getNotification()));
//        holder.time.setText(model.getTime());
        Notification notification = list.get(position);
        String type= notification.getType();
        String text = TimeAgo.using(notification.getNotificationAt());
        holder.binding.time.setText(text);
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notification.getNotificationBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User  user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile())
                                .placeholder(R.drawable.hanuman_ji)
                                .into(holder.binding.profileImg);
                        if(type.equals("like"))
                        {
                            holder.binding.notification.setText(Html.fromHtml("<b>"+user.getName()+"</b> liked your post"));
                        }
                        else if(type.equals("comment"))
                        {
                            holder.binding.notification.setText(Html.fromHtml("<b>"+user.getName()+"</b> commented on your post"));
                        }
                        else {
                            holder.binding.notification.setText(Html.fromHtml("<b>"+user.getName()+"</b> started following you"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("follow")) {
                    FirebaseDatabase.getInstance().getReference().child("notification")
                                    .child(notification.getPostedBy())
                                            .child(notification.getNotificationId())
                                                    .child("checkOpen")
                                                            .setValue(true);
                    holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent = new Intent(context, CommentActivity2.class);
                    intent.putExtra("postId", notification.getPostId());
                    intent.putExtra("postedBy", notification.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        boolean checkOpen = notification.isCheckOpen();
        if(checkOpen==true)
        {
            holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else{

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
//        ImageView profileImg;
//        TextView notification,time;
        Notification2sampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
//            profileImg= itemView.findViewById(R.id.profileImg);
//            notification = itemView.findViewById(R.id.notification);
//            time= itemView.findViewById(R.id.time);
            binding = Notification2sampleBinding.bind(itemView);

        }
    }
}
