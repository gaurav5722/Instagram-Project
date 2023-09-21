package com.newtry.instagramproject.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.newtry.instagramproject.Adapter.FollowerAdapter;
import com.newtry.instagramproject.Adapter.FriendAdapter;
import com.newtry.instagramproject.Model.Follow;
import com.newtry.instagramproject.Model.FriendModel;
import com.newtry.instagramproject.Model.User;
import com.newtry.instagramproject.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {
  ArrayList<Follow> list;
  RecyclerView profileRecycle;
    ImageView coverPhoto,ivVerifiedAccount,profileImage;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view=inflater.inflate(R.layout.fragment_profile, container, false);



       list= new ArrayList<>();
//         list.add(new FriendModel(R.drawable.hanuman_ji));
//         list.add(new FriendModel(R.drawable.hanuman_ji));
//        list.add(new FriendModel(R.drawable.cartoon));
//        list.add(new FriendModel(R.drawable.hanuman_ji));
//        list.add(new FriendModel(R.drawable.cartoon));
//        list.add(new FriendModel(R.drawable.hanuman_ji));

        FollowerAdapter adapter = new FollowerAdapter(getContext(),list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        profileRecycle = view.findViewById(R.id.rv3);
        profileRecycle.setLayoutManager(manager);
        profileRecycle.setAdapter(adapter);
        //To set the followers list
        database.getReference()
                .child("Users")
                .child(auth.getUid())//get the currently logged in user
                .child("followers")
                .addValueEventListener(new ValueEventListener() {//to get the data of the user
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Follow follow = dataSnapshot.getValue(Follow.class);
                            list.add(follow);
                        }
                        adapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



         coverPhoto= view.findViewById(R.id.ivCoverPhoto);
         ivVerifiedAccount = view.findViewById(R.id.ivVerifiedAccount);
        TextView name,profession,followerCount;
        name= view.findViewById(R.id.name);
        profession= view.findViewById(R.id.profession);
        profileImage = view.findViewById(R.id.profileImg);
        followerCount = view.findViewById(R.id.followerCount);
        ImageView changeCoverPhoto = view.findViewById(R.id.changeCoverPhoto);


        //Ta save the coverPhoto in database
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    //setting the cover photo
                    Picasso.get()
                            .load(user.getCoverPhoto())
                            .placeholder(R.drawable.hanuman_ji)//in case no image is avalaible
                            .into(coverPhoto);
                    profession.setText(user.getProfession());
                    name.setText(user.getName());
                    followerCount.setText(user.getFollowerCount()+"");//to set in the formate of the string the given integer
                    //setting the profile
                    Picasso.get()
                            .load(user.getProfile())
                            .placeholder(R.drawable.cartoon)
                            .into(profileImage);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //A code to open the galary and get the images of the required
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT); // intent to get some data
                intent.setType("image/*");//to get the images
                startActivityForResult(intent,11);
            }
        });
        ivVerifiedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,22);
            }
        });

        // Inflate the layout for this fragment
        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 11){
            if(data.getData()!=null)
            {  //to Save Photo in the storage
                Uri uri = data.getData();
                coverPhoto.setImageURI(uri);
                StorageReference reference = storage.getReference().child("cover_photo")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Cover photo saved", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                                Toast.makeText(getContext(), "Image is saved in database", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }}else{
                if(data.getData()!=null)
                {  //to Save Photo in the storage
                    Uri uri = data.getData();
                    profileImage.setImageURI(uri);
                    StorageReference reference = storage.getReference().child("profile_image")
                            .child(FirebaseAuth.getInstance().getUid());
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Profile image saved", Toast.LENGTH_SHORT).show();
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    database.getReference().child("Users").child(auth.getUid()).child("profile").setValue(uri.toString());
                                    Toast.makeText(getContext(), "ProfileImage is saved in database", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }

            }


    }
}