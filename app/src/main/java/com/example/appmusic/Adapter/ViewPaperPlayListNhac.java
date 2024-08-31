package com.example.appmusic.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPaperPlayListNhac extends FragmentPagerAdapter {
    public final ArrayList<Fragment> fragmentsArrayList = new ArrayList<>();
    public ViewPaperPlayListNhac(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsArrayList.size();
    }
    public void AddFragment(Fragment fragment){
        fragmentsArrayList.add(fragment);
    }
}
