package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bean.HuoDongBean;
import cn.linekeep.pangu.linekeep.R;

/**
 * Created by Administrator on 2017/9/27.
 */

public class HuoDongAdapter extends BaseAdapter {
    private ArrayList<HuoDongBean> list;
    private Context context;

    public HuoDongAdapter(ArrayList<HuoDongBean> list, Context context) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
        if(convertView ==null){
            convertView = View.inflate(context,R.layout.item_huodong,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==0){
            viewHolder.ivShow.setImageResource(R.drawable.banner1);
        }if(position==3){
            viewHolder.ivShow.setImageResource(R.drawable.banner4);
        }if(position==4||position%4==0||position==2){
            viewHolder.ivShow.setImageResource(R.drawable.banner2);
            viewHolder.tvTitle.setText("主题：科技是第一发展生产力");
        }else {
            viewHolder.ivShow.setImageResource(R.drawable.banner3);
            viewHolder.tvTitle.setText("主题：IT创业者线下交流会");
        }

        return convertView;
    }

    public  static  class  ViewHolder{
        TextView tvTitle,tvTime,tvAddress;
        ImageView ivShow;

        public ViewHolder(View view) {
            this.tvTitle = (TextView) view.findViewById(R.id.tv_huodong_name);
            this.tvTime = (TextView) view.findViewById(R.id.tv_huodong_time);
            this.tvAddress = (TextView) view.findViewById(R.id.tv_huodong_address);
            this.ivShow = (ImageView) view.findViewById(R.id.iv_huodong_show);
        }
    }
}
