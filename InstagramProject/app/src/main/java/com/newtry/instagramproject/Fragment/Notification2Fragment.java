package com.newtry.instagramproject.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newtry.instagramproject.Adapter.NotificationAdapter;
import com.newtry.instagramproject.Model.Notification;
//import com.newtry.instagramproject.Model.NotificationModel;
import com.newtry.instagramproject.R;

import java.util.ArrayList;


public class Notification2Fragment extends Fragment {
// ArrayList<NotificationModel> list;
 ArrayList<Notification> list;


    public Notification2Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_notification2, container, false);
        RecyclerView notificationRecyclerView = view.findViewById(R.id.rv4);
                list= new ArrayList<>();//we have used html tag under notification and in Notification adapter we have to also perform changes
//        list.add(new NotificationModel(R.drawable.cartoon,"<b>Vishnu</b> you have a notificatio from gaurav","4:00pm"));
//        list.add(new NotificationModel(R.drawable.cartoon,"<b>Vishnu</b> you have a notificatio from gaurav","4:00pm"));
//        list.add(new NotificationModel(R.drawable.hanuman_ji,"<b>Gaurav</b> you have a notificatio from Vishnu","8:00pm"));
//        list.add(new NotificationModel(R.drawable.cartoon,"<b>Vishnu</b> you have a notificatio from gaurav","4:00pm"));
//        list.add(new NotificationModel(R.drawable.hanuman_ji,"<b>Saurabh</b> you have a notificatio from Mohan","9:00pm"));
//        list.add(new NotificationModel(R.drawable.cartoon,"<b>Vishnu</b> you have a notificatio from gaurav","4:00pm"));
//        list.add(new NotificationModel(R.drawable.cartoon,"<b>Vishnu</b> you have a notificatio from gaurav","4:00pm"));
//        list.add(new NotificationModel(R.drawable.hanuman_ji,"<b>Gaurav</b> you have a notificatio from Vishnu","8:00pm"));
//        list.add(new NotificationModel(R.drawable.cartoon,"<b>Vishnu</b> you have a notificatio from gaurav","4:00pm"));
//        list.add(new NotificationModel(R.drawable.cartoon,"<b>Vishnu</b> you have a notificatio from gaurav","4:00pm"));
//        list.add(new NotificationModel(R.drawable.hanuman_ji,"<b>Gaurav</b> you have a notificatio from Vishnu","8:00pm"));
        // Inflate the layout for this fragment
        NotificationAdapter adapter = new NotificationAdapter(list,getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        notificationRecyclerView.setLayoutManager(manager);
        notificationRecyclerView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        if(snapshot.exists()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren())
                            {
                                Notification notification = dataSnapshot.getValue(Notification.class);
                                notification.setNotificationId(dataSnapshot.getKey());
                               if(!notification.getNotificationBy().equals(FirebaseAuth.getInstance().getUid()))
                               {  list.add(notification);}

                            }
                            adapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }
}