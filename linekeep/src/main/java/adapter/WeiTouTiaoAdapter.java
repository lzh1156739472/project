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
import bean.WeiTouTiaoBean;

/**
 * Created by Administrator on 2017/8/14.
 */

public class WeiTouTiaoAdapter extends BaseAdapter {
    private ArrayList<WeiTouTiaoBean.ArticlesBean> list;
    private Context context;

    public WeiTouTiaoAdapter(ArrayList<WeiTouTiaoBean.ArticlesBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = list.get(position).getTemplateType();
        if (viewType == 32 || viewType == 1 || viewType == 2) {
            return viewType;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 34;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderTypeVp viewHolderTypeVp;
        ViewHolderBigPic viewHolderBigPic;
        ViewHolderThreePic viewHolderThreePic;
        ViewHolderRightLeft viewHolderRithtLeft;
        ViewHolderPullDownRemaind viewHolderPullDownRemaind;
        int viewType = getItemViewType(position);
        WeiTouTiaoBean.ArticlesBean articlesBean = list.get(position);
        switch (viewType) {
            case 32:
                if (convertView == null) {
                    convertView = View.inflate(context, R
                            .layout.item_home_pulldown, null);
                    viewHolderPullDownRemaind = new ViewHolderPullDownRemaind(convertView);
                    convertView.setTag(viewHolderPullDownRemaind);
                } else {
                    viewHolderPullDownRemaind = (ViewHolderPullDownRemaind) convertView.getTag();
                }
                viewHolderPullDownRemaind.textView.setText(articlesBean.getTitle());
                break;

            case 1:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.item_home_rightleft, null);
                    viewHolderRithtLeft = new ViewHolderRightLeft(convertView);
                    convertView.setTag(viewHolderRithtLeft);
                } else {
                    viewHolderRithtLeft = (ViewHolderRightLeft) convertView.getTag();
                }
                viewHolderRithtLeft.textViewLeftTitile.setText(articlesBean.getTitle());
                Glide.with(context).load(articlesBean.getPics().get(0)).into(viewHolderRithtLeft.imageViewRightShow);
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
                viewHolderThreePic.textViewTitle.setText(articlesBean.getTitle());
                Glide.with(context).load(articlesBean.getPics().get(0)).into(viewHolderThreePic.imageViewLeft);
                Glide.with(context).load(articlesBean.getPics().get(1)).into(viewHolderThreePic.imageViewMiddle);
                Glide.with(context).load(articlesBean.getPics().get(2)).into(viewHolderThreePic.imageViewRight);
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
         //   this.viewPager = (ViewPager) view.findViewById(R.id.vp_home_new_head);
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


