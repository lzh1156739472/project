package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bean.QuanZiBean;
import cn.linekeep.pangu.linekeep.R;

/**
 * Created by Administrator on 2017/9/24.
 */

public class QuanZiAdapter extends BaseAdapter {
    private ArrayList<QuanZiBean> list;
    private Context context;

    public QuanZiAdapter(ArrayList<QuanZiBean> list, Context context) {
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_quanzi, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.tvNeiRong.setText("内容已经改变");
        if (position == 2 || position % 3 == 0) {
         viewHolder.imageView.setVisibility(View.VISIBLE);
        }else {
            viewHolder.imageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class ViewHolder {

        TextView tvNeiRong;
        ImageView  imageView;

        public ViewHolder(View view) {

            this.tvNeiRong = (TextView) view.findViewById(R.id.tv_quanzi_neirong);
            this.imageView = (ImageView) view.findViewById(R.id.iv_quanzi_tupian);
        }
    }
}
