package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import android.widget.TextView;

import java.util.List;

import cn.linekeep.pangu.linekeep.R;

/**
 * Created by Administrator on 2017/10/27.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    private Context mContext;
    private List<String> mData;

    public GridAdapter(Context context, List<String> data) {
        mData = data;
        mContext = context;
    }

    //创建ViewHolder的地方
    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_label, null);
        GridViewHolder holder = new GridViewHolder(itemView);
        return holder;
    }

    //绑定数据的地方
    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        holder.mTextView.setText(mData.get(position));
    }

    //获取RecyclerView的总数
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public static class GridViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public GridViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_label_item);
        }
    }


}
