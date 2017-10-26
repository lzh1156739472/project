package cn.linekeep.pangu.vcan.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import base.ParentWithNaviActivity;
import bean.imbean.User;
import cn.bmob.newim.BmobIM;

import cn.linekeep.pangu.linekeep.R;
import model.UserModel;

public class SetDetailActivity extends ParentWithNaviActivity {
    private RelativeLayout rlTuiChu, rlFanHui;
    private Boolean isTuiChu, LoginState;
    private SharedPreferences sf;
    private SharedPreferences.Editor editor;

    private Intent intent;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tvName.setText(UserModel.getInstance().getCurrentUser().getUsername());
        initData();
        setListener();
    }

    private void initData() {
        tvName.setText(sf.getString("namezhuyao", ""));
    }

    private void setListener() {
        //退出登录
        rlTuiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 警告对话框

//                1、获得AlertDialog的静态内部类Builder对象，由该类来创建对话框。
//
//                2、通过Builder对象设置对话框的标题、按钮以及按钮将要响应的事件。
//
//                3、调用Builder的create()方法创建对话框。
//
//                4、调用AlertDialog的show()方法显示对话框。


                AlertDialog.Builder builder = new AlertDialog.Builder(SetDetailActivity.this);
                builder.setMessage("确定要推出登录吗？").
                        // 设置确定按钮
                                setPositiveButton("是", new DialogInterface.OnClickListener() {
                            // 单击事件
                            public void onClick(DialogInterface dialog, int which) {
                                LoginState = false;
                                isTuiChu = true;
                                editor.putBoolean("isTuiChu", isTuiChu);
                                editor.putBoolean("LoginState", LoginState);
                                editor.putString("namezhuyao", sf.getString("namezhuyao", ""));
                                editor.putString("passwardzhuyao", sf.getString("passwardzhuyao", ""));
                                editor.commit();
                                Intent intent = new Intent(SetDetailActivity.this, LoginActivity.class);
                                Bundle bundle = new Bundle();
                                User user = UserModel.getCurrentUser();
                                bundle.putString("userName", user.getUsername());
                               // bundle.putString("userPassward", user.get);
                                intent.putExtras(bundle);
                                UserModel.getInstance().logout();
                                //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
                                BmobIM.getInstance().disConnect();

                                startActivity(intent);
                                // startActivity(LoginActivity.class,bundle);
                                Toast.makeText(SetDetailActivity.this, "正在退出", Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                // 创建对话框
                AlertDialog ad = builder.create();
                // 显示对话框
                ad.show();
            }
        });


        //返回
        rlFanHui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void initView() {
        super.initView();
        rlTuiChu = (RelativeLayout) findViewById(R.id.rl_mine_set_detail_tuichu);
        rlFanHui = (RelativeLayout) findViewById(R.id.rl_setting_back);
        sf = getSharedPreferences(UserModel.getCurrentUser().getObjectId(), MODE_PRIVATE);
        tvName = (TextView) findViewById(R.id.tv_set_detail_nametop);
        editor = sf.edit();


    }

    @Override
    protected String title() {
        return "设置";
    }
}
