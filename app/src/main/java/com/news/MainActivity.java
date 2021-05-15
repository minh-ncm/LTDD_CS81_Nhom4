package com.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.backend.DatabaseManagement;
import com.backend.NewsPreview;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0 :
                        bottomNavigationView.getMenu().findItem(R.id.menu_homepage).setChecked(true);
                        break;
                    case 1 :
                        bottomNavigationView.getMenu().findItem(R.id.menu_person).setChecked(true);
                        break;
                    case 2 :
                        bottomNavigationView.getMenu().findItem(R.id.menu_notification).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch (item.getItemId()){
                    case R.id.menu_homepage:
                        viewPager.setCurrentItem(R.id.menu_homepage);
                        Log.d("menu_click","menu_homepage");
                        break;
                    case R.id.menu_person:
                        viewPager.setCurrentItem(R.id.menu_person);
                        Log.d("menu_click","menu_person");
                        break;
                    case R.id.menu_notification:
                        viewPager.setCurrentItem(R.id.menu_notification);
                        Log.d("menu_click","menu_notification");
                        break;
                }
                return true;
            }

        });
        DatabaseManagement databaseManagement = new DatabaseManagement();
        databaseManagement.getLatestPreviews(new DatabaseManagement.newsListPreviewsCallback() {
            @Override
            public void onCallback(List<NewsPreview> list) {

            }

        }, 10);
    }
}