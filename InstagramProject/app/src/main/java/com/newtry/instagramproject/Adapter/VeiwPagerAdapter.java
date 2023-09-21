package com.newtry.instagramproject.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.newtry.instagramproject.Fragment.Notification2Fragment;
import com.newtry.instagramproject.Fragment.RequestFragment;

public class VeiwPagerAdapter extends FragmentPagerAdapter {
    public VeiwPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
//in this cases we do not put break;
    @NonNull
    @Override//getItem is used to demonstrate which fragment must be replaced at which position
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new Notification2Fragment();
            case 1: return new RequestFragment();
            default: return new Notification2Fragment();
        }
    }
//return the number of items in the viewPager
    @Override
    public int getCount() {
        return 2;
    }
    //to settitle of the page


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position==0)
        {
            title="Notification";
        }
        else if(position == 1)
        {
            title= "Request";
        }
        return title;
    }
}
