package fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import adapter.VpHomeAdaper;
import cn.linekeep.pangu.linekeep.R;

/**
 * Created by zx on 2017/8/3.
 */

public class HomeFragment extends Fragment {
    private TabLayout  mTabLayout;
    private ViewPager mViewPager;
    private String[] mTitle;
    private ArrayList<ModuleFragment> listModelFragment;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.home_fragment,null);
        View view2 = View.inflate(getContext(),R.layout.label_layout,null);
        initView(view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tb_tl);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_home_news);
        mTitle=getResources().getStringArray(R.array.tb_name);
        textView = (TextView) view.findViewById(R.id.tv_jiaohao);
        listModelFragment = new ArrayList<>();
        for(int i = 0;i<mTitle.length;i++){
            ModuleFragment moduleFragment = new ModuleFragment();
            Bundle bundle=new Bundle();
            bundle.putString("type",mTitle[i]);
            moduleFragment.setArguments(bundle);
            listModelFragment.add(moduleFragment);
        }




        //设置TabLayout与ViewPager的联动

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setSelectedTabIndicatorColor(Color.BLUE);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tb= mTabLayout.getTabAt(i);//获取对应位置的tab
          //  tb.setIcon(R.mipmap.ic_launcher);//设置tab的图标
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    if(mTabLayout.getTabAt(i)==tab)
                    {
                        mViewPager.setCurrentItem(i);
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

        VpHomeAdaper vpHomeAdapter = new VpHomeAdaper(getFragmentManager(),listModelFragment,mTitle);
        mViewPager.setAdapter(vpHomeAdapter);
        mViewPager.setCurrentItem(1);
    }
}
