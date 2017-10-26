package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


import adapter.FindVpAdapter;
import cn.linekeep.pangu.linekeep.R;
import bean.HuoDongBean;

/**
 * Created by zx on 2017/8/3.
 */

public class FindFragment extends Fragment {
    private TabLayout tabLayout;
    private ArrayList<HuoDongBean> listData;
    private String[] titles;
    private ArrayList<Fragment> listFrag;
    private FindVpAdapter adapter;
    private ViewPager vpDa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_find, null);
        initView(view);
        initData();
        setData();
        setListener();
        return view;
    }

    private void setListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (tabLayout.getTabAt(i) == tab) {
                        vpDa.setCurrentItem(i);
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setData() {
        vpDa.setAdapter(adapter);
        // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpDa);
    }

    private void initData() {
        HuoDongModuleFragment huoDong = new HuoDongModuleFragment();
        FuJinModuleFragment fujin = new FuJinModuleFragment();

        for (int i = 0; i < titles.length; i++) {
            if (i == 0) {
                Bundle bundle = new Bundle();
                bundle.putString("findname", titles[i]);
                huoDong.setArguments(bundle);
                listFrag.add(huoDong);
            }
            if (i == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("findname", titles[i]);
                fujin.setArguments(bundle);
                listFrag.add(fujin);
            }
        }


    }

    private void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tbl_find_fenlei);
        vpDa = (ViewPager) view.findViewById(R.id.vp_find_neirong);
        listData = new ArrayList<>();
        listFrag = new ArrayList<>();
        titles = getResources().getStringArray(R.array.tbl_find_name);
        adapter = new FindVpAdapter(getChildFragmentManager(), getContext(), listFrag, titles);
    }


}
