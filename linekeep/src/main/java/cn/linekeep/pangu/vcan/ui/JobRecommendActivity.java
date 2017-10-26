package cn.linekeep.pangu.vcan.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import bean.imbean.User;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.linekeep.pangu.linekeep.R;
import model.UserModel;

public class JobRecommendActivity extends AppCompatActivity {
    private EditText etJobAmend;
    private SharedPreferences sp;
    private String z;
    private Button button;
    private SharedPreferences.Editor aditor;
    private RelativeLayout rlAmendBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_job_recommend);
        initView();
        setListener();
    }

    private void setListener() {
        //名字的修改
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String job = etJobAmend.getText().toString();
                if(!job.equals("")){
                    if(job.equals(sp.getString("jobzhuyao",""))){
                        Toast.makeText(JobRecommendActivity.this, "没有任何修改", Toast.LENGTH_SHORT).show();
                    }else {
                        final User myUser = new User();
                        myUser.setJob(job);
                        myUser.update(z, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
//                                 aditor.putString("objectid",p.getObjectId());
                                    aditor.putString("jobzhuyao",job);
                                    aditor.commit();
                                    Toast.makeText(JobRecommendActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(JobRecommendActivity.this, "修改失败", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }

                }else {
                    Toast.makeText(JobRecommendActivity.this, "不能为空", Toast.LENGTH_SHORT).show();

                }

            }
        });


        //返回
        rlAmendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initView() {
        etJobAmend = (EditText) findViewById(R.id.et_job_amend);
        rlAmendBack = (RelativeLayout) findViewById(R.id.rl_job_amend);
        sp = getSharedPreferences(UserModel.getCurrentUser().getObjectId(), MODE_PRIVATE);
        // z =sp.getString("objectid","");
        z= UserModel.getCurrentUser().getObjectId();
        button = (Button) findViewById(R.id.bt_job_amend);
        aditor = sp.edit();
        aditor.putString("jobzhuyao","");
        String job = UserModel.getCurrentUser().getJob();
        etJobAmend.setText(job);
        if(job!=null){
            etJobAmend.setSelection(job.length()==0?0:job.length());
        }

    }
}
