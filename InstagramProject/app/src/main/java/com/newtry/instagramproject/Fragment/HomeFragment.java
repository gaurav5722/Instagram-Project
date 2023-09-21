package com.newtry.instagramproject.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newtry.instagramproject.Adapter.StoryAdapter;
import com.newtry.instagramproject.Model.StoryModel;
import com.newtry.instagramproject.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    RecyclerView storeyRv;
    ArrayList<StoryModel> list;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        storeyRv = view.findViewById(R.id.rv);
        list= new ArrayList<>();
        list.add(new StoryModel(R.drawable.cartoon,R.drawable.ic_live,R.drawable.cartoon,"vishnu"));
        list.add(new StoryModel(R.drawable.cartoon,R.drawable.ic_live,R.drawable.cartoon,"vishnu"));
        list.add(new StoryModel(R.drawable.cartoon,R.drawable.ic_live,R.drawable.cartoon,"vishnu"));
        list.add(new StoryModel(R.drawable.cartoon,R.drawable.ic_live,R.drawable.cartoon,"vishnu"));


        StoryAdapter adapter = new StoryAdapter(list,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storeyRv.setLayoutManager(layoutManager);
        storeyRv.setNestedScrollingEnabled(false);
        storeyRv.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;


    }
}