package com.newtry.instagramproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.newtry.instagramproject.Fragment.AddFragment;
import com.newtry.instagramproject.Fragment.HomeFragment;
import com.newtry.instagramproject.Fragment.NotificationFragment;
import com.newtry.instagramproject.Fragment.ProfileFragment;
import com.newtry.instagramproject.Fragment.SearchFragment;
import com.newtry.instagramproject.R;
import com.newtry.instagramproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth= FirebaseAuth.getInstance();
        setSupportActionBar(binding.toolbar);

        MainActivity.this.setTitle("My profile");

        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new HomeFragment()).commit();
        binding.toolbar.setVisibility(View.GONE);
        binding.readable.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i)
                {
                    case 0:
                       binding.toolbar.setVisibility(View.GONE);
                       getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new HomeFragment()).commit() ;
                        break;
                    case 1:
                        binding.toolbar.setVisibility(View.GONE);
                       getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new NotificationFragment()).commit();
                        break;

                    case 2:
                        binding.toolbar.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new AddFragment()).commit();
                        break;
                    case 3:
                         binding.toolbar.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new SearchFragment()).commit();
                        break;

                    case 4:

                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new ProfileFragment()).commit();
                        binding.toolbar.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.setting:
//                auth.signOut();
//                Toast.makeText(this, "Setting clicked and signed out", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
//                break;
        if(item.getItemId()==R.id.setting)
        {
            auth.signOut();
            Toast.makeText(this, "signed out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}