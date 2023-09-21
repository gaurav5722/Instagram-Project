package com.newtry.instagramproject.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.newtry.instagramproject.Model.PostModel;
import com.newtry.instagramproject.Model.User;
import com.newtry.instagramproject.R;
import com.newtry.instagramproject.databinding.FragmentAddBinding;
import com.squareup.picasso.Picasso;

import java.util.Date;


public class AddFragment extends Fragment {
FragmentAddBinding binding;
    Uri uri;
    FirebaseAuth auth ;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog dialog;//to show the progress while image is uploaded in storage and in realtime database

    public AddFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddBinding.inflate(inflater, container, false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);//To not set any cancel button on it
        dialog.setCanceledOnTouchOutside(false);//dialog must not be cancelled when touched outside

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    User user= snapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getProfile())
                            .placeholder(R.drawable.cartoon)
                            .into(binding.profileImg);
                    binding.name.setText(user.getName());
                    binding.profession.setText(user.getProfession());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.etPostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               String description = binding.etPostDescription.getText().toString();
               if(!description.isEmpty())
               {
                   binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_btn_bg));
                   binding.btnPost.setTextColor(getContext().getResources().getColor(R.color.white));
                   binding.btnPost.setEnabled(true);
               }
               else{
                   binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_active_btn));
                   binding.btnPost.setTextColor(getContext().getResources().getColor(R.color.gray));
                   binding.btnPost.setEnabled(false);
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.ivAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
// Button post to store in firebase storage and then setting it in firebase database;
        binding.btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.show();
                StorageReference reference = storage.getReference().child("posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime()+"");
                       reference .putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       //Empty costructer called
                                       PostModel post= new PostModel();
                                       //Setting the data in the PostModel
                                       post.setPostImage(uri.toString());
                                       post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                       post.setPostDescription(binding.etPostDescription.getText().toString());
                                       post.setPostedAt(new Date().getTime());
                                       //Now to set the data in the database

                                       database.getReference().child("post")
                                               .push()//we have to upload at new nodes each time
                                               .setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void unused) {
                                                       dialog.dismiss();
                                                       Toast.makeText(getContext(), "Posted successfully", Toast.LENGTH_SHORT).show();
                                                   }
                                               });


                                   }
                               });
                           }
                       });

            }
        });



        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null)
        {
            uri = data.getData();
            binding.ivPostImage.setImageURI(uri);
            binding.ivPostImage.setVisibility(View.VISIBLE);
            binding.btnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_btn_bg));
            binding.btnPost.setTextColor(getContext().getResources().getColor(R.color.white));
            binding.btnPost.setEnabled(true);

        }
    }
}