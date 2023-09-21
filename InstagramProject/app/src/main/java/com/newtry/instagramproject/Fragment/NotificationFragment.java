package com.newtry.instagramproject.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.newtry.instagramproject.Adapter.VeiwPagerAdapter;
import com.newtry.instagramproject.R;


public class NotificationFragment extends Fragment {
 ViewPager viewPager;
 TabLayout tabLayout;


    public NotificationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_notification, container, false);
        viewPager= view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new VeiwPagerAdapter(getChildFragmentManager()));
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);




        // Inflate the layout for this fragment
        return view;
    }
}