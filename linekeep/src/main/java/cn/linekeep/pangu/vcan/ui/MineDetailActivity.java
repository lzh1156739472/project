package cn.linekeep.pangu.vcan.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import bean.imbean.User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.linekeep.pangu.linekeep.R;
import model.UserModel;

public class MineDetailActivity extends AppCompatActivity {
    private RelativeLayout rlTop, rlImage, rlName, rlJob;
    private ImageView ivHeadPhoto;
    private TextView tvName, tvJob;
    private SharedPreferences sp;


    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    private SharedPreferences.Editor aditor;
    private String z;
    private BmobFile bmobFile;
    private String imagePath;


    private String imageLuJingZhongKong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mine_detail);
        initView();
        initData();
        setListener();
    }


    private void initData() {
        //读取sd卡兵显示在控件上
        String imageName = sp.getString("imageName", "");
        if (imageName.equals("")) {
            //当没有设置任何头像的时候默认不做操作，加载默认头像
        } else {
            if (imageName.equals(imageLuJingZhongKong)) {
                //不做任何操作
            } else {
                String sdpath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();// 获取sdcard的根路径
                String filepath = sdpath + File.separator + imageName;
                File file = new File(filepath);
                if (file.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(filepath);
                    // 将图片显示到ImageView中
                    ivHeadPhoto.setImageBitmap(bm);
                    imageLuJingZhongKong = imageName;
                } else {
                    Toast.makeText(MineDetailActivity.this, "SD卡不存在", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private void setListener() {
        rlTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });

        rlName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineDetailActivity.this, NameAmendActivity.class));

            }
        });

        rlJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineDetailActivity.this, JobRecommendActivity.class));
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (data == null) {
            return;
        }
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);
            // Log.e("IMAGE", "onActivityResult: " + imagePath);
            //将图片显示在控件上
            showImage(imagePath);
            c.close();
            //TODO
            //判断图片文件是否相同，入股哦相同，将不再上传到服务器
            if (UserModel.getCurrentUser().getImage() == null) {
                bmobFile = new BmobFile(new File(imagePath));
                bmobFile.uploadblock(new UploadFileListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            aditor.putString("imagePath", imagePath);
                            aditor.putString("imageName", bmobFile.getFilename());
                            aditor.putString("urlzhuyao", bmobFile.getFileUrl());
                            User user = BmobUser.getCurrentUser(User.class);
                            z = user.getObjectId();
                            Toast.makeText(MineDetailActivity.this, "上传文件成功", Toast.LENGTH_SHORT).show();
                            //更新用户数据的url数据
                            final User myUser = new User();
                            myUser.setPic(bmobFile.getFileUrl());
                            myUser.setAvatar(bmobFile.getFileUrl());
                            myUser.setImage(imagePath);
                            myUser.update(z, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        aditor.commit();
                                        Toast.makeText(MineDetailActivity.this, "更新url成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MineDetailActivity.this, "更新url失败", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {
                            Toast.makeText(MineDetailActivity.this, "上传文件失败", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onProgress(Integer value) {
                        // 返回的上传进度（百分比）
                    }
                });

            } else {
                if (UserModel.getCurrentUser().getImage().equals(imagePath)) {
                    //不做任何处理
                    showImage(imagePath);
                    Toast.makeText(this, "图片文件一样，无需上传和更新", Toast.LENGTH_SHORT).show();
                } else {
                    bmobFile = new BmobFile(new File(imagePath));
                    bmobFile.uploadblock(new UploadFileListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                aditor.putString("imagePath", imagePath);
                                aditor.putString("imageName", bmobFile.getFilename());
                                aditor.putString("urlzhuyao", bmobFile.getFileUrl());
                                aditor.commit();
                                User user = BmobUser.getCurrentUser(User.class);
                                z = user.getObjectId();
                                Toast.makeText(MineDetailActivity.this, "上传文件成功", Toast.LENGTH_SHORT).show();
                                //更新用户数据的url数据
                                final User myUser = new User();
                                myUser.setPic(bmobFile.getFileUrl());
                                myUser.setImage(imagePath);
                                myUser.setAvatar(bmobFile.getFileUrl());
                                myUser.update(z, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            aditor.commit();
                                            Toast.makeText(MineDetailActivity.this, "更新url成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MineDetailActivity.this, "更新url失败", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(MineDetailActivity.this, "上传文件失败", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                        }
                    });

                }
            }


        }


    }


    //加载图片
    private void showImage(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ivHeadPhoto.setImageBitmap(bm);
    }


    private void initView() {
        rlTop = (RelativeLayout) findViewById(R.id.rl_detail_mine_top);
        rlImage = (RelativeLayout) findViewById(R.id.rl_detail_mine_photo);
        rlName = (RelativeLayout) findViewById(R.id.rl_mine_detail_name);
        rlJob = (RelativeLayout) findViewById(R.id.rl_mine_detail_job);
        ivHeadPhoto = (ImageView) findViewById(R.id.iv_mine_detail_amend);
        tvName = (TextView) findViewById(R.id.tv_mine_detail_name);
        tvJob = (TextView) findViewById(R.id.tv_mine_detail_job);
        sp = getSharedPreferences(UserModel.getCurrentUser().getObjectId(), MODE_PRIVATE);
        aditor = sp.edit();
        tvName.setText(sp.getString("namezhuyao", ""));
        User user = UserModel.getCurrentUser();
        if(user.getJob()==null){
            tvJob.setText("未设置相关信息");
        }else {
            tvJob.setText(user.getJob());
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        tvName.setText(sp.getString("namezhuyao", ""));
        User user = UserModel.getCurrentUser();
        if(user.getJob()==null){
            tvJob.setText("未设置相关信息");
        }else {
            tvJob.setText(user.getJob());
        }

        //读取sd卡兵显示在控件上
        String imageName = sp.getString("imageName", "");
        if (imageName.equals("")) {
            //当没有设置任何头像的时候默认不做操作，加载默认头像
        } else {
            if (imageName.equals(imageLuJingZhongKong)) {
                //不做任何操作
            } else {
                String sdpath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();// 获取sdcard的根路径
                String filepath = sdpath + File.separator + imageName;
                File file = new File(filepath);
                if (file.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(filepath);
                    // 将图片显示到ImageView中
                    ivHeadPhoto.setImageBitmap(bm);
                    imageLuJingZhongKong = imageName;
                } else {
                    Toast.makeText(MineDetailActivity.this, "SD卡不存在", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}



