package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fragments.VpHeadNewsFragment;

/**
 * Created by Administrator on 2017/8/7.
 */

public class VpHomeNewsHeadAdapter  extends FragmentPagerAdapter {
    private ArrayList<VpHeadNewsFragment> data;


    public VpHomeNewsHeadAdapter(FragmentManager fm, ArrayList<VpHeadNewsFragment> data) {
        super(fm);
        this.data = data;

    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
