package adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/25.
 */

public class FindVpAdapter extends FragmentPagerAdapter {
    private Context context;
    private ArrayList<Fragment> list;
    private String[] titles;

    public FindVpAdapter(FragmentManager fm, Context cx, ArrayList<Fragment> fg, String[] ts) {
        super(fm);
        context = cx;
        list = fg;
        titles =ts;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
