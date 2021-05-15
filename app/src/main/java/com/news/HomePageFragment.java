package com.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.backend.DatabaseManagement;
import com.backend.NewsPreview;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


public class HomePageFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View mView;
    private LinearLayout linearLayout;




    public HomePageFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public void LoadDatabase(int amount) {
        DatabaseManagement databaseManagement = new DatabaseManagement();
        databaseManagement.getLatestPreviews(new DatabaseManagement.newsListPreviewsCallback() {
            @Override
            public void onCallback(List<NewsPreview> list) {
                Log.d("Out",list.get(0).getPreviewContent());
            }
        }, amount);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_home_page, container, false);
        tabLayout = mView.findViewById(R.id.tab_layout);
        viewPager = mView.findViewById(R.id.homepage_viewpager);
        linearLayout = mView.findViewById(R.id.linear_homepage);

        HomePageViewPagerAdapter adapter = new HomePageViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return mView;

    }
}
