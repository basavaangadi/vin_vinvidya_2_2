package com.vinuthana.vinvidya.viewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by KISHAN on 08-07-2017.
 */

public class VPager extends FragmentPagerAdapter {
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    ArrayList<String> strFragmentNamesList = new ArrayList<>();

    public VPager(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fgmt, String val)
    {
        fragmentList.add(fgmt);
        strFragmentNamesList.add(val);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    public CharSequence getPageTitle(int position)
    {
        return strFragmentNamesList.get(position);
    }
}
