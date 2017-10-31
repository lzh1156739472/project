package fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import adapter.GridAdapter;
import adapter.VpHomeAdaper;
import cn.linekeep.pangu.linekeep.R;

/**
 * Created by zx on 2017/8/3.
 */

public class HomeFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTitle;
    private ArrayList<ModuleFragment> listModelFragment;
    private TextView textView;
    private LinearLayout ly;
    PopupWindow popupWindow;
    private TextView tuichu;
    private RecyclerView recyclerViewYi, recyclerViewWei;
    private GridAdapter gridapter;
    private List<String> mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.home_fragment, null);
        View labelView = View.inflate(getContext(), R.layout.label_layout, null);
        tuichu = (TextView) labelView.findViewById(R.id.tv_label_aditor);
        recyclerViewYi = (RecyclerView) labelView.findViewById(R.id.rv_inuse_label);
        recyclerViewWei = (RecyclerView) labelView.findViewById(R.id.rv_noinuse_label);
        popupWindow = new PopupWindow(labelView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);//设置点击外部的时候，窗口也可以消失
        initView(view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(ly, Gravity.BOTTOM, 0, 0);
                //      backgroundAlpha((float) 0.5);
                mData = new ArrayList<>();
                gridapter = new GridAdapter(getContext(), mData);

                for (int i = 0; i < 100; i++) {
                    mData.add("标签" + i);
                }

                setData();
                gridapter.notifyDataSetChanged();
                ;

            }
        });
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        return view;
    }

    private void setData() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5, LinearLayoutManager.VERTICAL, false);
        recyclerViewYi.setLayoutManager(manager);
        recyclerViewYi.setAdapter(gridapter);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tb_tl);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_home_news);
        mTitle = getResources().getStringArray(R.array.tb_name);
        textView = (TextView) view.findViewById(R.id.tv_jiaohao);
        ly = (LinearLayout) view.findViewById(R.id.popwindow);


        listModelFragment = new ArrayList<>();
        for (int i = 0; i < mTitle.length; i++) {
            ModuleFragment moduleFragment = new ModuleFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", mTitle[i]);
            moduleFragment.setArguments(bundle);
            listModelFragment.add(moduleFragment);
        }


        //设置TabLayout与ViewPager的联动

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setSelectedTabIndicatorColor(Color.BLUE);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tb = mTabLayout.getTabAt(i);//获取对应位置的tab
            //  tb.setIcon(R.mipmap.ic_launcher);//设置tab的图标
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    if (mTabLayout.getTabAt(i) == tab) {
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

        VpHomeAdaper vpHomeAdapter = new VpHomeAdaper(getFragmentManager(), listModelFragment, mTitle);
        mViewPager.setAdapter(vpHomeAdapter);
        mViewPager.setCurrentItem(1);

    }
}
