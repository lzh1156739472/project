package netinterface;

import bean.ChuangYeBean;
import bean.EcnomicBean;
import bean.HomeHotBean;

import bean.HomeShiYeBean;
import bean.WeiTouTiaoBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/8/4.
 */

public interface RetrofitInterface {
    // 直接发送网络请求
    @GET
    Call<HomeHotBean> getNetJsonCall(@Url String str);

    @GET
    Call<HomeShiYeBean> getDataUrl(@Url String s);

    @GET
    Call<ChuangYeBean> getDataCYUrl(@Url String str);

    @GET
    Call<EcnomicBean> getDataCJ(@Url String str);

    @GET
    Call<WeiTouTiaoBean> getDataWeiTouTiao(@Url String str);
}
