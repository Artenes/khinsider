package com.artenesnogueira.khinsider.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_action) {
            startActivity(new Intent(this, SearchActivity.class));
        }
        return true;
    }
}
