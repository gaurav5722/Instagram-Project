package com.newtry.instagramproject.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.newtry.instagramproject.Adapter.DasboardAdapter;
import com.newtry.instagramproject.Adapter.StoryAdapter;
import com.newtry.instagramproject.Model.DasboardModel;
import com.newtry.instagramproject.Model.PostModel;
import com.newtry.instagramproject.Model.Story;

import com.newtry.instagramproject.Model.UserStories;
import com.newtry.instagramproject.R;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {
    RecyclerView storeyRv;
    ShimmerRecyclerView dasbordRv;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Story> storyList;
    RoundedImageView addStoryImage;
    ActivityResultLauncher<String> galleryLauncher;
    FirebaseStorage storage;
    ProgressDialog dialog;
//    ArrayList<DasboardModel> dasboardList;
    // instead of the arraylist of the dasboardModel type we will use array list of the PostModel
    ArrayList<PostModel> postList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog= new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
       //initializing the dasboard recyclerview for simmer effect
        dasbordRv = view.findViewById(R.id.rv2);
        dasbordRv.showShimmerAdapter();
        //

        storeyRv = view.findViewById(R.id.rv);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Story Uploading");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        storyList = new ArrayList<>();
//        list.add(new StoryModel(R.drawable.cartoon, R.drawable.ic_live, R.drawable.hanuman_ji, "vishnu"));
//        list.add(new StoryModel(R.drawable.cartoon, R.drawable.hanuman_ji, R.drawable.hanuman_ji, "vishnu"));
//        list.add(new StoryModel(R.drawable.cartoon, R.drawable.ic_live, R.drawable.hanuman_ji, "vishnu"));
//        list.add(new StoryModel(R.drawable.cartoon, R.drawable.ic_live, R.drawable.hanuman_ji, "vishnu"));
//

        StoryAdapter adapter = new StoryAdapter(storyList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storeyRv.setLayoutManager(layoutManager);
        storeyRv.setNestedScrollingEnabled(false);
        storeyRv.setAdapter(adapter);

        //setting the recyclerView of the Story
        database.getReference()
                .child("stories").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        { storyList.clear();
                            for(DataSnapshot storySnapshot: snapshot.getChildren())
                            {
                                Story story= new Story();
                                story.setStoryBy(storySnapshot.getKey());
                                story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UserStories> stories= new ArrayList<>();
                                for (DataSnapshot snapshot1:storySnapshot.child("userStories").getChildren())
                                {
                                    UserStories userStories = snapshot1.getValue(UserStories.class);
                                    stories.add(userStories);
                                }
                                story.setStories(stories);
                                storyList.add(story);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






        //2nd fragment starts from here

//        dasboardList = new ArrayList<>();
        //instead of the dasboard list with dasboardModel we have used PostModel
        postList =new ArrayList<>();

//        dasboardList.add(new DasboardModel(R.drawable.hanuman_ji, R.drawable.cartoon, R.drawable.ic_bookmark, "vishnu", "nice ", "23", "32", "22"));
//        dasboardList.add(new DasboardModel(R.drawable.hanuman_ji, R.drawable.cartoon, R.drawable.ic_bookmark, "vishnu", "nice ", "23", "32", "22"));
//        dasboardList.add(new DasboardModel(R.drawable.hanuman_ji, R.drawable.cartoon, R.drawable.ic_bookmark, "vishnu", "nice ", "23", "32", "22"));


//        DasboardAdapter dasboardAdapter = new DasboardAdapter(getContext(), dasboardList);
        // the above data is to be set by using the firebase

        DasboardAdapter dasboardAdapter = new DasboardAdapter(getContext(), postList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        dasbordRv.setLayoutManager(manager);
        dasbordRv.setNestedScrollingEnabled(false);
        dasbordRv.addItemDecoration(new DividerItemDecoration(dasbordRv.getContext(), DividerItemDecoration.VERTICAL));

        database.getReference().child("post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               postList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    PostModel post = dataSnapshot.getValue(PostModel.class);
                    post.setPostId(dataSnapshot.getKey());
                    postList.add(post);

                } dasbordRv.setAdapter(dasboardAdapter);
                  dasbordRv.hideShimmerAdapter();
                dasboardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
          addStoryImage = view.findViewById(R.id.imageView1);
          addStoryImage.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  galleryLauncher.launch("image/*");
              }
          });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                addStoryImage.setImageURI(result);
                dialog.show();
                final StorageReference reference = storage.getReference()
                        .child("stories")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime()+"");
                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Story story= new Story();
                                story.setStoryAt(new Date().getTime());
                                database.getReference().child("stories")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .child("postedBy")
                                        .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                UserStories stories = new UserStories(uri.toString(),story.getStoryAt());
                                                database.getReference().child("stories")
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .child("userStories")
                                                        .push()
                                                        .setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });


        // Inflate the layout for this fragment
        return view;


    }
}