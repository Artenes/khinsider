package com.artenesnogueira.khinsider.view;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.artenesnogueira.khinsider.R;
import com.artenesnogueira.khinsider.adapter.MainActivityPageAdapter;

/**
 * Main screen of the app that displays top albums in a tab layout
 */
public class MainActivity extends AppCompatActivity {

    private MainActivityPageAdapter mAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MainActivityPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);
    }
}
