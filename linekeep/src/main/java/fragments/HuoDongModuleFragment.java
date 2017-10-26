package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import adapter.HuoDongAdapter;
import bean.HuoDongBean;
import cn.linekeep.pangu.linekeep.R;
import util.bannerutils.LocalImageHolderView;

import static fragments.ModuleFragment.getResId;

/**
 * Created by Administrator on 2017/8/25.
 */

public class HuoDongModuleFragment extends FindFragment {
    private PullToRefreshListView pullToRefreshListView;
    private HuoDongAdapter adapter;
    private ArrayList<HuoDongBean> list;
    private ListView listView;
    private ConvenientBanner convenientBanner;
    private List mImageList;
    private ImageView[] listImage;
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_find_huodong, null);
        View headView = View.inflate(getActivity(), R.layout.item_huodong_banner, null);
        initView(view, headView);
        initData();
        setData(headView);
        setListener(headView);
        return view;
    }

    private void setListener(final View view) {
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.removeHeaderView(view);
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.removeHeaderView(view);
                initData();
            }
        });
    }

    private void initView(View view, View headView) {
        convenientBanner = (ConvenientBanner) headView.findViewById(R.id.banner_huodong);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulllistview_find_huodong);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//只要头部
        listView = pullToRefreshListView.getRefreshableView();
        list= new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            HuoDongBean huoDongBean = new HuoDongBean();
            list.add(huoDongBean);
        }
        adapter = new HuoDongAdapter(list,getActivity());

    }

    private void initData(){
        //获取本地的图片
        for (int position =0; position <2; position++) {
            //这里面的a  代表drawable里面自己放的本地图片
            localImages.add(getResId("huodong" + position, R.drawable.class));
        }
    }

    private void setData(View headView){
        pullToRefreshListView.setAdapter(adapter);

        //开始自动翻页
        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        },localImages)
                //设置指示器是否可见
                .setPointViewVisible(true)
                //设置自动切换（同时设置了切换时间间隔）
                .startTurning(10000)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_LEFT)
                //设置点击监听事件
               // .setOnItemClickListener(this)
                //设置手动影响（设置了该项无法手动切换）
                .setManualPageable(true);


        //设置翻页的效果，不需要翻页效果可用不设
        //setPageTransformer(Transformer.DefaultTransformer);   // 集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。


         listView.addHeaderView(headView);
        pullToRefreshListView.onRefreshComplete();
    }
    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
