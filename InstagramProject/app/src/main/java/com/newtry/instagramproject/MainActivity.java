package com.newtry.instagramproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.newtry.instagramproject.Fragment.AddFragment;
import com.newtry.instagramproject.Fragment.HomeFragment;
import com.newtry.instagramproject.Fragment.NotificationFragment;
import com.newtry.instagramproject.Fragment.ProfileFragment;
import com.newtry.instagramproject.Fragment.SearchFragment;
import com.newtry.instagramproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new HomeFragment()).commit();
        binding.readable.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i)
                {
                    case 0:
                       getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new HomeFragment()).commit() ;
                        break;
                    case 1:
                       getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new NotificationFragment()).commit();
                        break;

                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new AddFragment()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new SearchFragment()).commit();
                        break;

                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer,new ProfileFragment()).commit();
                        break;
                }
            }
        });


    }
}