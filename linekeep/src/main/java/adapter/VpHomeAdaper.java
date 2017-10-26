package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fragments.ModuleFragment;

/**
 * Created by zx on 2017/8/3.
 */

public class VpHomeAdaper extends FragmentPagerAdapter {
    private ArrayList<ModuleFragment> list;
    private String[] title;//标题的名称

    public VpHomeAdaper(FragmentManager fm, ArrayList<ModuleFragment> list, String[] title) {
        super(fm);
        this.list = list;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
