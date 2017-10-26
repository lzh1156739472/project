package cn.linekeep.pangu.vcan.ui;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import base.ParentWithNaviActivity;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import cn.linekeep.pangu.linekeep.R;
import event.FinishEvent;
import model.BaseModel;
import model.UserModel;

public class RegistActivity extends ParentWithNaviActivity {
    private EditText mEtName;
    private EditText mEtPassWard,et_password_again;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor aditor;
    private   Boolean LoginState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();

    }


    protected void initView() {

        mEtName = (EditText) findViewById(R.id.et_regist_username);
        mEtPassWard = (EditText) findViewById(R.id.et_regist_passward);
        et_password_again = (EditText) findViewById(R.id.et_password_again);
    }

    public void Regist(View view) {
        switch (view.getId()) {
            case R.id.bt_regist:

                UserModel.getInstance().register(mEtName.getText().toString(), mEtPassWard.getText().toString(), et_password_again.getText().toString(), new LogInListener() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            EventBus.getDefault().post(new FinishEvent());
                            sharedPreferences = getSharedPreferences(UserModel.getCurrentUser().getObjectId(), Context.MODE_PRIVATE);
                            aditor= sharedPreferences.edit();
                            LoginState = true;
                            aditor.putBoolean("LoginState", LoginState);
                            aditor.putString("namezhuyao", mEtName.getText().toString());
                            aditor.putString("passwardzhuyao", mEtPassWard.getText().toString());
                            aditor.putString("urlzhuyao", "");
                            aditor.putString("jobzhuyao","");
                            aditor.putString("objectid",UserModel.getCurrentUser().getObjectId());
                            aditor.commit();
                            startActivity(MainActivity.class, null, true);
                        } else {
                            if (e.getErrorCode() == BaseModel.CODE_NOT_EQUAL) {
                                et_password_again.setText("");
                            }
                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                        }
                    }

                });

                break;

            case R.id.iv_regist_back:
                finish();
                break;
        }
    }

    @Override
    protected String title() {
        return "注册";
    }
}
