package avida.ican.Ican.View.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Ican.View.Custom.Resorse;

/**
 * Created by AtrasVida on 2018-05-16 at 11:58 AM
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
    private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //adding fragments and title method
    public void addFrag(Fragment fragment, int titleRes) {
        String title= Resorse.getString(titleRes);
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
