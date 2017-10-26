package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import bean.imbean.User;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.linekeep.pangu.linekeep.R;
import cn.linekeep.pangu.vcan.ui.MineDetailActivity;
import cn.linekeep.pangu.vcan.ui.SetDetailActivity;
import model.UserModel;

/**
 * Created by zx on 2017/8/3.
 */

public class MineFragment extends Fragment {
    private static final String TAG = "finalData";
    private TextView mTvName, mTvJob;
    private ImageView mIvPhoto;
    private RelativeLayout rlDetail,rlSet;
    private String userName;
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor aditor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_mine, null);
        sharedPreferences = getActivity().getSharedPreferences(UserModel.getCurrentUser().getObjectId(), Context.MODE_PRIVATE);
        aditor = sharedPreferences.edit();
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initData() {

    }

    private void setListener() {
        rlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MineDetailActivity.class);
                startActivity(intent);
            }
        });

        //设置中有退出选项，需要传值登录状态，登录名和密码等信息
        rlSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetDetailActivity.class);
                startActivity(intent);
            }
        });

    }
    private void initView(View view) {
        mTvName = (TextView) view.findViewById(R.id.tv_mine_name);
        mTvJob = (TextView) view.findViewById(R.id.tv_mine_job);
        mIvPhoto = (ImageView) view.findViewById(R.id.iv_mine_touxiang);
        rlDetail = (RelativeLayout) view.findViewById(R.id.rl_mine_info);
        rlSet = (RelativeLayout) view.findViewById(R.id.rl_mine_shezhi);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("fragment", "onResume: " );
        User user = UserModel.getInstance().getCurrentUser();
        mTvName.setText(user.getUsername());
        if(user.getJob()==null){
              mTvJob.setText("未设置相关信息");
        }else {
            mTvJob.setText(user.getJob());
        }

        final String imageName = sharedPreferences.getString("imageName", "");

            if (imageName.equals("")) {
                //当没有设置任何头像的时候默认不做操作，加载默认头像
            } else {
                    //直接读取sd卡的图片不下载
                    boolean isSdCardExist = Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED);// 判断sdcard是否存在
                    if (isSdCardExist) {
                        String sdpath = Environment.getExternalStorageDirectory()
                                .getAbsolutePath();// 获取sdcard的根路径
                        String filepath = sdpath + File.separator + imageName;
                        File file = new File(filepath);

                        if (file.exists()) {//文件存在的话就将图片处理并显示
                            Bitmap bm = BitmapFactory.decodeFile(filepath);
                            // 将图片显示到ImageView中
                            mIvPhoto.setImageBitmap(bm);
                        }else {
                            Toast.makeText(getContext(), "文件不存在,，开始下载", Toast.LENGTH_SHORT).show();
                            String url = sharedPreferences.getString("urlzhuyao", "");
                            BmobFile bmobfile = new BmobFile(imageName, "", url);
                            File saveFile = new File(Environment.getExternalStorageDirectory(), imageName);
                            bmobfile.download(saveFile, new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getActivity(), "已经下载到本机"+s, Toast.LENGTH_SHORT).show();
                                        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                                                Environment.MEDIA_MOUNTED);// 判断sdcard是否存在
                                        if (isSdCardExist)
                                        {
                                            String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();// 获取sdcard的根路径
                                            String filepath = sdpath + File.separator + imageName;
                                            File file = new File(filepath);
                                            if (file.exists())
                                            {
                                                Bitmap bm = BitmapFactory.decodeFile(filepath);
                                                // 将图片显示到ImageView中
                                                mIvPhoto.setImageBitmap(bm);

                                            }else{
                                                Toast.makeText(getContext(), "下载后文件不存在", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "SD卡不存在", Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });

                        }
                    } else {
                        Toast.makeText(getContext(), "SD卡不存在", Toast.LENGTH_SHORT).show();
                    }


        }


    }

}
