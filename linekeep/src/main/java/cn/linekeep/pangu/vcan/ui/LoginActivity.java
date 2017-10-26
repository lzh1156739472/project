package cn.linekeep.pangu.vcan.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import base.BaseActivity;
import butterknife.OnClick;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.exception.BmobException;

import cn.bmob.v3.listener.LogInListener;
import cn.linekeep.pangu.linekeep.R;

import model.UserModel;

public class LoginActivity extends BaseActivity {
    private EditText mEtUserName;
    private EditText mEtPassWard;
    private String userName;
    private String userpassWard;
    private SharedPreferences sharedPreferences;
    private Boolean LoginState;
    private Boolean isFromTuiChu = false;
    SharedPreferences.Editor aditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//用共享参数判断登录状态，也可以将对登录的user缓存到本地，两种写登录的方式
//        if (LoginState) {
//            setContentView(R.layout.activity_main);
//            initMainView();
//        } else {}
        setContentView(R.layout.activity_login);
        initView();
        initData();
        //做个查询，当前的用户
        //共享参数的表名是当前用户的 objectid
        //如果要想得到ibjectid，就必须做一个擦查询

    }

    private void initData() {
        BmobConfig config = new BmobConfig.Builder(this)
                .setApplicationId("982335f0c917b262ba9421e1db12cda8")
                ////请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                .setUploadBlockSize(1024 * 1024)
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }

    protected void initView() {
        mEtUserName = (EditText) findViewById(R.id.et_username);
        mEtPassWard = (EditText) findViewById(R.id.et_password);
    }

    /**
     * 去注册
     *
     * @param view
     */
    @OnClick(R.id.tv_regist)
    public void onRegisterClick(View view) {
        startActivity(RegistActivity.class, null, false);
    }

    //登录跳转
    public void Login(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                userName = mEtUserName.getText().toString();
                userpassWard = mEtPassWard.getText().toString();
                UserModel.getInstance().login(userName, userpassWard, new LogInListener() {

                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            sharedPreferences = getSharedPreferences(UserModel.getCurrentUser().getObjectId(), Context.MODE_PRIVATE);
                            isFromTuiChu = sharedPreferences.getBoolean("isTuiChu", false);
                            LoginState = sharedPreferences.getBoolean("LoginState", false);
                            aditor = sharedPreferences.edit();
                            //  aditor.putString(UserModel.getCurrentUser().getObjectId(),UserModel.getCurrentUser().getObjectId());
                            aditor.putBoolean("LoginState", LoginState);
                            aditor.putString("namezhuyao", userName);
                            aditor.putString("passwardzhuyao", userpassWard);
                            aditor.putString("urlzhuyao", "");
                            aditor.putString("jobzhuyao","");
                            aditor.commit();
                            //登录成功
                            Toast.makeText(LoginActivity.this, "正在登录", Toast.LENGTH_SHORT).show();
                            startActivity(MainActivity.class, null, true);
                        } else {
                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                        }
                    }
                });
                break;

        }
    }

    @Override
    protected void onRestart() {

        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }


}
