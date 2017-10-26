package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.lang.reflect.Field;
import java.util.ArrayList;

import adapter.EcnomicAdapter;
import adapter.NewsAdapter;
import adapter.NewsChuangYeAdapter;
import adapter.NewsShiYeAdapter;
import adapter.QuanZiAdapter;
import adapter.VpHomeNewsHeadAdapter;
import adapter.WeiTouTiaoAdapter;

import bean.QuanZiBean;
import cn.linekeep.pangu.linekeep.R;
import cn.linekeep.pangu.vcan.ui.NewsDetailActivity;
import json.JsonUrl;
import bean.ChuangYeBean;
import bean.EcnomicBean;
import bean.HomeHotBean;
import bean.HomeShiYeBean;
import bean.WeiTouTiaoBean;
import netinterface.RetrofitInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.bannerutils.LocalImageHolderView;

/**
 * Created by zx on 2017/8/3.
 */

public class ModuleFragment extends Fragment implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {
    //圈子
    private QuanZiAdapter quanZiAdapter;
    private ArrayList<QuanZiBean> listQuanZi;

    private PullToRefreshListView mPullToRefreshListView;
    private RetrofitInterface mRetrofitInterface;
    private Retrofit mRetrofit;
    ArrayList<HomeHotBean.ArticlesBean> listData;
    private int ShiYeNewsCount = 1;//用于控制视野新闻数据的加载次数，以便进行倒序加载

    private ArrayList<HomeShiYeBean.RecommendArticlesBean> listZhongKong;
    ArrayList<HomeShiYeBean.RecommendArticlesBean> listRecommentArticles;
    private NewsAdapter mNewsAdapter;
    private NewsShiYeAdapter shiYeAdapter;
    private ViewPager mViewPager;
    private NewsChuangYeAdapter mCYAdapter;
    private ArrayList<ChuangYeBean.ArticlesBean> listCYData;
    private EcnomicAdapter ecnomicAdapter;
    private ArrayList<EcnomicBean.ArticlesBean> listCJData;

    private WeiTouTiaoAdapter mWeiTouTiaoAdapter;
    private ArrayList<WeiTouTiaoBean.ArticlesBean> listWeiTouTiao;

    private ArrayList<VpHeadNewsFragment> listVpHeadNewsFragment;
    private VpHomeNewsHeadAdapter headVpAdapter;


    //添加头部的listview  由pulltoRefresh转化而来
    private ListView listView;

    private int addHeadcount;
    private String type;
    private ConvenientBanner convenientBanner;
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    // private ArrayList<String> transformerList = new ArrayList<String>();
    private ArrayAdapter transformerArrayAdapter;
    private int countFragment;

    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_model, null);
        View headerView = View.inflate(getContext(), R.layout.item_home_vp_head, null);
        initView(view);
        View viewRadio = View.inflate(getContext(), R.layout.fragment_vp_head_news, null);
        initViewHead(headerView);
        initData(headerView);
        // init();
        setData();
        setListener(headerView);
        return view;
    }

    private void setData() {
        mPullToRefreshListView.setAdapter(quanZiAdapter);
    }


    private void init() {
        initImageLoader();
        loadTestDatas();
        //本地图片例子
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages);
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        //设置指示器的方向
        // .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
        //.setOnPageChangeListener(this)//监听翻页事件
        // .setOnItemClickListener(this);

        convenientBanner.setManualPageable(false);//设置不能手动影响

        // 网络加载例子
//        networkImages=Arrays.asList(images);
//        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
//            @Override
//            public NetworkImageHolderView createHolder() {
//                return new NetworkImageHolderView();
//            }
//        },networkImages);


//手动New并且添加到ListView Header的例子
//        ConvenientBanner mConvenientBanner = new ConvenientBanner(this,false);
//        mConvenientBanner.setMinimumHeight(500);
//        mConvenientBanner.setPages(
//                new CBViewHolderCreator<LocalImageHolderView>() {
//                    @Override
//                    public LocalImageHolderView createHolder() {
//                        return new LocalImageHolderView();
//                    }
//                }, localImages)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
//                        //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnItemClickListener(this);
//        listView.addHeaderView(mConvenientBanner);
    }


    //初始化网络图片缓存库
    private void initImageLoader() {
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity().getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    /*
    加入测试Views
    * */
    private void loadTestDatas() {
        //本地图片集合
        for (int position =1; position <= 6; position++)
            localImages.add(getResId("banner" + position, R.drawable.class));


//        //各种翻页效果
//        transformerList.add(DefaultTransformer.class.getSimpleName());
//        transformerList.add(AccordionTransformer.class.getSimpleName());
//        transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
//        transformerList.add(CubeInTransformer.class.getSimpleName());
//        transformerList.add(CubeOutTransformer.class.getSimpleName());
//        transformerList.add(DepthPageTransformer.class.getSimpleName());
//        transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
//        transformerList.add(FlipVerticalTransformer.class.getSimpleName());
//        transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
//        transformerList.add(RotateDownTransformer.class.getSimpleName());
//        transformerList.add(RotateUpTransformer.class.getSimpleName());
//        transformerList.add(StackTransformer.class.getSimpleName());
//        transformerList.add(ZoomInTransformer.class.getSimpleName());
//        transformerList.add(ZoomOutTranformer.class.getSimpleName());
//
//        transformerArrayAdapter.notifyDataSetChanged();
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


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(7000);

    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

//    //点击切换效果
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
////        点击后加入两个内容
////        localImages.clear();
////        localImages.add(R.drawable.ic_test_2);
////        localImages.add(R.drawable.ic_test_4);
////        convenientBanner.notifyDataSetChanged();
//
//        //控制是否循环
////        convenientBanner.setCanLoop(!convenientBanner.isCanLoop());
//
//
//   //     String transforemerName = transformerList.get(position);
//        try {
//            Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
//            ABaseTransformer transforemer= (ABaseTransformer)cls.newInstance();
//            convenientBanner.getViewPager().setPageTransformer(true,transforemer);
//
//            //部分3D特效需要调整滑动速度
//            if(transforemerName.equals("StackTransformer")){
//                convenientBanner.setScrollDuration(1200);
//            }
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (java.lang.InstantiationException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Toast.makeText(getActivity(), "监听到翻到第" + position + "了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

//    @Override
//    public void onItemClick(int position) {
//        Toast.makeText(getActivity(),"点击了第"+position+"个", Toast.LENGTH_SHORT).show();
//    }


    private void initViewHead(View headerView) {
        convenientBanner = (ConvenientBanner) headerView.findViewById(R.id.convenientBanner);
//        if(countFragment==0){
//            convenientBanner .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
//            countFragment++;
//        }

        listVpHeadNewsFragment = new ArrayList<>();
        mPullToRefreshListView.setAdapter(transformerArrayAdapter);
    }


    private void setListener(final View view) {
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.removeHeaderView(view);
                initData(view);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                listView.removeHeaderView(view);
                initData(view);
            }
        });
    }

    private void initView(View view) {



        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pullRefresh_home_hot);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//只要头部
        listView = mPullToRefreshListView.getRefreshableView();
        listRecommentArticles = new ArrayList<>();
        listZhongKong = new ArrayList<>();
        listData = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(listData, getActivity());
        shiYeAdapter = new NewsShiYeAdapter(listRecommentArticles, getContext());
        listCYData = new ArrayList<>();
        mCYAdapter = new NewsChuangYeAdapter(listCYData, getActivity());
        listCJData = new ArrayList<>();
        ecnomicAdapter = new EcnomicAdapter(listCJData, getActivity());
        listWeiTouTiao = new ArrayList<>();
        mWeiTouTiaoAdapter = new WeiTouTiaoAdapter(listWeiTouTiao, getActivity());
        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), NewsDetailActivity.class));
            }
        });

        mRetrofit = new Retrofit.Builder().baseUrl("http://api.k.sohu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //初始化接口实例
        mRetrofitInterface = mRetrofit.create(RetrofitInterface.class);

        Bundle bundle = getArguments();
        type = (String) bundle.get("type");


    }

    private void initData(View view) {
        if(type.equals("圈子")){
            //圈子
            listQuanZi = new ArrayList<>();
            quanZiAdapter  = new QuanZiAdapter(listQuanZi,getActivity());
            for (int i = 0; i < 30; i++) {
                QuanZiBean quanZiBean = new QuanZiBean();
                listQuanZi.add(quanZiBean);
                quanZiAdapter.notifyDataSetChanged();
            }

            mPullToRefreshListView.onRefreshComplete();

        }

        if (type.equals("热点")) {
//            if (addHeadcount == 0) {
            listView.addHeaderView(view);
            //   }
            //   ++addHeadcount;
            //首页的数据添加
            mRetrofitInterface.getNetJsonCall(JsonUrl.HotNewsStr).enqueue(new Callback<HomeHotBean>() {
                @Override
                public void onResponse(Call<HomeHotBean> call, Response<HomeHotBean> response) {
                    if (response.isSuccessful()) {
                        HomeHotBean homeHotBean = response.body();
                        listData.addAll(homeHotBean.getArticles());
                        mNewsAdapter.notifyDataSetChanged();
                        //给pulltorefresh设置适配器
                        mPullToRefreshListView.setAdapter(mNewsAdapter);
                        mPullToRefreshListView.onRefreshComplete();
                        for (int i = 0; i < homeHotBean.getArticles().get(0).getNewsItems().size(); i++) {
//                            images[i]=homeHotBean.getArticles().get(0).getNewsItems().get(i).getPics().get(0);
                        }
                        //给viewpager添加数据并设置适配器
//                        listVpHeadNewsFragment.clear();
//                        for (int i = 0; i < listData.get(0).getNewsItems().size() + 2; i++) {
//                            VpHeadNewsFragment vpHeadNewsFragment = new VpHeadNewsFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("duixiang", homeHotBean);
//                            bundle.putInt("idKey", i);
//                            vpHeadNewsFragment.setArguments(bundle);
//                            listVpHeadNewsFragment.add(vpHeadNewsFragment);
//                        }

//                     headVpAdapter = new VpHomeNewsHeadAdapter(getChildFragmentManager(), listVpHeadNewsFragment);
//                        headVpAdapter.notifyDataSetChanged();
//                        mViewPager.setAdapter(headVpAdapter);
                        //;  mViewPager.setCurrentItem(2, false);
                        Log.e("TAG3", listVpHeadNewsFragment.size() + "打印长度验证是否有数据");



                        loadTestDatas();
                        //本地图片例子
                        convenientBanner.setPages(
                                new CBViewHolderCreator<LocalImageHolderView>() {
                                    @Override
                                    public LocalImageHolderView createHolder() {
                                        return new LocalImageHolderView();
                                    }
                                }, localImages);
                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设

                        //设置指示器的方向
                        // .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                        //.setOnPageChangeListener(this)//监听翻页事件
                        // .setOnItemClickListener(this);

                        convenientBanner.setManualPageable(true);//设置不能手动影响

                    }
                }

                @Override
                public void onFailure(Call<HomeHotBean> call, Throwable t) {


                }
            });


        }

        if (type.equals("视野")) {
            if (ShiYeNewsCount == 1) {
                mRetrofitInterface.getDataUrl(JsonUrl.ShiYeStr).enqueue(new Callback<HomeShiYeBean>() {
                    @Override
                    public void onResponse(Call<HomeShiYeBean> call, Response<HomeShiYeBean> response) {
                        if (response.isSuccessful()) {
                            HomeShiYeBean homeShiYeBean = response.body();
                            listRecommentArticles.clear();
                            listRecommentArticles.addAll(homeShiYeBean.getRecommendArticles());
                            shiYeAdapter.notifyDataSetChanged();
                            mPullToRefreshListView.setAdapter(shiYeAdapter);
                            mPullToRefreshListView.onRefreshComplete();
                            ShiYeNewsCount = 2;
                        }
                    }

                    @Override
                    public void onFailure(Call<HomeShiYeBean> call, Throwable t) {

                    }
                });
                type = "视野";
                listZhongKong.addAll(listRecommentArticles);
            }

            if (ShiYeNewsCount == 2) {
                mRetrofitInterface.getDataUrl(JsonUrl.ShiYeStr).enqueue(new Callback<HomeShiYeBean>() {
                    @Override
                    public void onResponse(Call<HomeShiYeBean> call, Response<HomeShiYeBean> response) {
                        if (response.isSuccessful()) {
                            HomeShiYeBean homeShiYeBean = response.body();
                            listRecommentArticles.clear();
                            listRecommentArticles.addAll(homeShiYeBean.getRecommendArticles());
                            listRecommentArticles.addAll(listZhongKong);
                            mPullToRefreshListView.setAdapter(shiYeAdapter);
                            shiYeAdapter.notifyDataSetChanged();
                            mPullToRefreshListView.onRefreshComplete();
                            listZhongKong.clear();
                            listZhongKong.addAll(listRecommentArticles);
                            type = "视野";
                            ShiYeNewsCount = 2;
                        }
                    }

                    @Override
                    public void onFailure(Call<HomeShiYeBean> call, Throwable t) {

                    }
                });
            }

        }

        if (type.equals("创业")) {
            mRetrofitInterface.getDataCYUrl(JsonUrl.ChuangYeStr).enqueue(new Callback<ChuangYeBean>() {
                @Override
                public void onResponse(Call<ChuangYeBean> call, Response<ChuangYeBean> response) {
                    if (response.isSuccessful()) {
                        ChuangYeBean chuangYeBean = response.body();
                        listCYData.clear();
                        // Log.e("homeShiYeBean",homeShiYeBean.getRecommendArticles().size()+"");
                        listCYData.addAll(chuangYeBean.getArticles());
                        Log.e("ssss", listCYData.size() + "");
                        mCYAdapter.notifyDataSetChanged();
                        mPullToRefreshListView.setAdapter(mCYAdapter);
                        mPullToRefreshListView.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(Call<ChuangYeBean> call, Throwable t) {

                }
            });

        }

        if (type.equals("财经")) {
            mRetrofitInterface.getDataCJ(JsonUrl.CaiJingStr).enqueue(new Callback<EcnomicBean>() {
                @Override
                public void onResponse(Call<EcnomicBean> call, Response<EcnomicBean> response) {
                    if (response.isSuccessful()) {
                        EcnomicBean ecnomicBean = response.body();
                        listCJData.clear();
                        listCJData.addAll(ecnomicBean.getArticles());
                        Log.e("ssss", listCYData.size() + "");
                        ecnomicAdapter.notifyDataSetChanged();
                        mPullToRefreshListView.setAdapter(ecnomicAdapter);
                        mPullToRefreshListView.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(Call<EcnomicBean> call, Throwable t) {

                }
            });

        }

        if (type.equals("生活")) {
            mRetrofitInterface.getDataWeiTouTiao(JsonUrl.weiTouTiaoStr).enqueue(new Callback<WeiTouTiaoBean>() {
                @Override
                public void onResponse(Call<WeiTouTiaoBean> call, Response<WeiTouTiaoBean> response) {
                    if (response.isSuccessful()) {
                        WeiTouTiaoBean weiTouTiaoBean = response.body();
                        listWeiTouTiao.clear();
                        // Log.e("homeShiYeBean",homeShiYeBean.getRecommendArticles().size()+"");
                        listWeiTouTiao.addAll(weiTouTiaoBean.getArticles());
                        Log.e("ssss", listCYData.size() + "");
                        mWeiTouTiaoAdapter.notifyDataSetChanged();
                        mPullToRefreshListView.setAdapter(mWeiTouTiaoAdapter);
                        mPullToRefreshListView.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(Call<WeiTouTiaoBean> call, Throwable t) {

                }
            });

        }


        if (type.equals("文化")) {
            mRetrofitInterface.getDataCYUrl(JsonUrl.ChuangYeStr).enqueue(new Callback<ChuangYeBean>() {
                @Override
                public void onResponse(Call<ChuangYeBean> call, Response<ChuangYeBean> response) {
                    if (response.isSuccessful()) {
                        ChuangYeBean chuangYeBean = response.body();
                        listCYData.clear();
                        listCYData.addAll(chuangYeBean.getArticles());
                        Log.e("ssss", listCYData.size() + "");
                        mCYAdapter.notifyDataSetChanged();
                        mPullToRefreshListView.setAdapter(mCYAdapter);
                        mPullToRefreshListView.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(Call<ChuangYeBean> call, Throwable t) {

                }
            });

        }


        if (type.equals("历史")) {
            mRetrofitInterface.getDataCYUrl(JsonUrl.ChuangYeStr).enqueue(new Callback<ChuangYeBean>() {
                @Override
                public void onResponse(Call<ChuangYeBean> call, Response<ChuangYeBean> response) {
                    if (response.isSuccessful()) {
                        ChuangYeBean chuangYeBean = response.body();
                        listCYData.clear();
                        // Log.e("homeShiYeBean",homeShiYeBean.getRecommendArticles().size()+"");
                        listCYData.addAll(chuangYeBean.getArticles());
                        Log.e("ssss", listCYData.size() + "");
                        mCYAdapter.notifyDataSetChanged();
                        mPullToRefreshListView.setAdapter(mCYAdapter);
                        mPullToRefreshListView.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(Call<ChuangYeBean> call, Throwable t) {

                }
            });

        }


        if (type.equals("+")) {
            mRetrofitInterface.getDataCYUrl(JsonUrl.ChuangYeStr).enqueue(new Callback<ChuangYeBean>() {
                @Override
                public void onResponse(Call<ChuangYeBean> call, Response<ChuangYeBean> response) {
                    if (response.isSuccessful()) {
                        ChuangYeBean chuangYeBean = response.body();
                        listCYData.clear();
                        // Log.e("homeShiYeBean",homeShiYeBean.getRecommendArticles().size()+"");
                        listCYData.addAll(chuangYeBean.getArticles());
                        Log.e("ssss", listCYData.size() + "");
                        mCYAdapter.notifyDataSetChanged();
                        mPullToRefreshListView.setAdapter(mCYAdapter);
                        mPullToRefreshListView.onRefreshComplete();
                    }
                }

                @Override
                public void onFailure(Call<ChuangYeBean> call, Throwable t) {

                }
            });

        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
