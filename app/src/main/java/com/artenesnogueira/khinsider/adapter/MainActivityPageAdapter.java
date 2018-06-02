package com.artenesnogueira.khinsider.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.artenesnogueira.khinsider.api.model.TopAlbums;
import com.artenesnogueira.khinsider.view.TopAlbumFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to display pages in the main activity
 */
public class MainActivityPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> pages;
    private final List<String> titles;

    public MainActivityPageAdapter(FragmentManager fm) {
        super(fm);

        pages = new ArrayList<>(4);
        pages.add(TopAlbumFragment.make(TopAlbums.TOP_40));
        pages.add(TopAlbumFragment.make(TopAlbums.NEWLY_ADDED));
        pages.add(TopAlbumFragment.make(TopAlbums.LAST_SIX_MOTHS));
        pages.add(TopAlbumFragment.make(TopAlbums.ALL_TIME));

        titles = new ArrayList<>(4);
        titles.add("Top 40");
        titles.add("Top Newly Added");
        titles.add("Top Last 6 Months");
        titles.add("Top All Time");

    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}