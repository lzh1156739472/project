package adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.linekeep.pangu.linekeep.R;
import bean.HomeHotBean;

/**
 * Created by Administrator on 2017/8/5.
 */

public class NewsAdapter extends BaseAdapter {
    private ArrayList<HomeHotBean.ArticlesBean> data;
    private Context context;

    public NewsAdapter(ArrayList<HomeHotBean.ArticlesBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int posizon) {
        return data.get(posizon);
    }

    @Override
    public long getItemId(int posizon) {
        return posizon;
    }

    @Override
    public int getViewTypeCount() {
        return 34;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = data.get(position).getTemplateType();

        if (viewType == 32 || viewType == 1 || viewType == 2) {
            return viewType;
        } else {
            return 0;
        }


    }

    @Override
    public View getView(int posizon, View convertView, ViewGroup viewGroup) {
        ViewHolderTypeVp viewHolderTypeVp;
        ViewHolderBigPic viewHolderBigPic;
        ViewHolderThreePic viewHolderThreePic;
        ViewHolderRightLeft viewHolderRithtLeft;
        ViewHolderPullDownRemaind viewHolderPullDownRemaind;
        int viewType = getItemViewType(posizon);
        HomeHotBean.ArticlesBean articeBean = data.get(posizon);
        switch (viewType) {
            case 32:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_home_pulldown, null);
                    viewHolderPullDownRemaind = new ViewHolderPullDownRemaind(convertView);
                    convertView.setTag(viewHolderPullDownRemaind);
                } else {
                    viewHolderPullDownRemaind = (ViewHolderPullDownRemaind) convertView.getTag();
                }
                viewHolderPullDownRemaind.textView.setText(articeBean.getTitle());
                break;

            case 1:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_home_rightleft, null);
                    viewHolderRithtLeft = new ViewHolderRightLeft(convertView);
                    convertView.setTag(viewHolderRithtLeft);
                } else {
                    viewHolderRithtLeft = (ViewHolderRightLeft) convertView.getTag();
                }
                viewHolderRithtLeft.textViewLeftTitile.setText(articeBean.getTitle());
                Glide.with(context).load(articeBean.getPics().get(0)).into(viewHolderRithtLeft.imageViewRightShow);
                viewHolderRithtLeft.textViewKind.setVisibility(View.GONE);
                viewHolderRithtLeft.texViewComment.setVisibility(View.GONE);
                viewHolderRithtLeft.textViewTime.setVisibility(View.GONE);
                break;
            case 2:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_homt_three_pic, null);
                    //  LayoutInflater.from(context).inflate(R.layout.item_homt_three_pic, null);
                    viewHolderThreePic = new ViewHolderThreePic(convertView);
                    convertView.setTag(viewHolderThreePic);
                } else {
                    viewHolderThreePic = (ViewHolderThreePic) convertView.getTag();
                }
                viewHolderThreePic.textViewTitle.setText(articeBean.getTitle());
                Glide.with(context).load(articeBean.getPics().get(0)).into(viewHolderThreePic.imageViewLeft);
                Glide.with(context).load(articeBean.getPics().get(1)).into(viewHolderThreePic.imageViewMiddle);
                Glide.with(context).load(articeBean.getPics().get(2)).into(viewHolderThreePic.imageViewRight);
                break;
            case 0:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_null, null);
                }
        }

        return convertView;


    }

    public static class ViewHolderPullDownRemaind {
        TextView textView;

        public ViewHolderPullDownRemaind(View view) {
            this.textView = (TextView) view.findViewById(R.id.tv_home_pulldown_remind);

        }
    }

    //viewpager
    public static class ViewHolderTypeVp {
        ViewPager viewPager;

        public ViewHolderTypeVp(View view) {
          //  this.viewPager = (ViewPager) view.findViewById(R.id.vp_home_new_head);
        }
    }

    //大图片
    public static class ViewHolderBigPic {
        TextView textViewTitle;
        ImageView imageViewShow;

        public ViewHolderBigPic(View view) {
            this.imageViewShow = (ImageView) view.findViewById(R.id.iv_home_onepic_title);
            this.textViewTitle = (TextView) view.findViewById(R.id.tv_home_onepic_title);
        }
    }
    //一个主题三张图片

    public static class ViewHolderThreePic {
        TextView textViewTitle;
        ImageView imageViewRight, imageViewMiddle, imageViewLeft;

        public ViewHolderThreePic(View view) {
            this.imageViewRight = (ImageView) view.findViewById(R.id.iv_home_three_right);
            this.imageViewMiddle = (ImageView) view.findViewById(R.id.iv_home_three_middle);
            this.imageViewLeft = (ImageView) view.findViewById(R.id.iv_home_three_left);
            this.textViewTitle = (TextView) view.findViewById(R.id.tv_home_three_title);
        }
    }

    //左右排版
    public static class ViewHolderRightLeft {
        TextView textViewLeftTitile, textViewKind, texViewComment, textViewTime;
        ImageView imageViewRightShow;

        public ViewHolderRightLeft(View view) {
            this.imageViewRightShow = (ImageView) view.findViewById(R.id.iv_home_rightleft_suolue);
            this.textViewLeftTitile = (TextView) view.findViewById(R.id.tv_home_rightlft_title);
            this.textViewKind = (TextView) view.findViewById(R.id.tv_home_rightleft_kind);
            this.texViewComment = (TextView) view.findViewById(R.id.tv_home_rightleft_comment);
            this.textViewTime = (TextView) view.findViewById(R.id.tv_home_time);

        }


    }


}

