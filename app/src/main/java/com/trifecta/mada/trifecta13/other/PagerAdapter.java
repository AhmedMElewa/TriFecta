package com.trifecta.mada.trifecta13.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.trifecta.mada.trifecta13.fragment.HomeTab;
import com.trifecta.mada.trifecta13.fragment.StoresTab;

/**
 * Created by Mada on 3/17/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeTab tab1 = new HomeTab();
                return tab1;
            case 1:
                StoresTab tab2 = new StoresTab();
                return tab2;
            default:
                HomeTab tab = new HomeTab();
                return tab;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}