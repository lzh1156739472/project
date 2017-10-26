package cn.linekeep.pangu.vcan.ui;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;


import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


import base.BaseActivity;
import bean.imbean.User;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.linekeep.pangu.linekeep.R;
import db.NewFriendManager;
import event.RefreshEvent;
import fragments.ConnectFragment;
import fragments.FindFragment;
import fragments.HomeFragment;
import fragments.ConversationFragment;
import fragments.MineFragment;
import util.imutils.IMMLeaks;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg;
    private LinearLayout ly;
    private ArrayList<Fragment> listfg;
    private ArrayList<RadioButton> listrb;
    private FragmentManager manger;
    private FragmentTransaction transaction;
    private int curIndex, tarIndex;//记录当前和目标的碎片索引
 //  private ImageView iv_conversation_tips,iv_contact_tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        EventBus.getDefault().post(new RefreshEvent());
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getAvatar()));
                    } else {
                        toast(e.getMessage());
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    toast(status.getMsg());
                    Logger.i(BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());
    }


    @Override
    protected void initView() {
        super.initView();
        manger = getSupportFragmentManager();
        rg = (RadioGroup) findViewById(R.id.rg_main);
        ly = (LinearLayout) findViewById(R.id.main_linearlayout);
//        iv_contact_tips = (ImageView) findViewById(R.id.iv_contact_tips);
//        iv_conversation_tips = (ImageView) findViewById(R.id.iv_conversation_tips);
        listfg = new ArrayList<>();
        listrb = new ArrayList<>();
        int len = rg.getChildCount();
        for (int i = 0; i < len; i++) {
            listrb.add((RadioButton) rg.getChildAt(i));
        }
        listfg.add(new HomeFragment());
        listfg.add(new FindFragment());
        listfg.add(new ConnectFragment());
        listfg.add(new ConversationFragment());
        MineFragment mineFragment = new MineFragment();
        //    mineFragment.setArguments(bundle);
        listfg.add(mineFragment);
        listrb.get(0).setChecked(true);
        transaction = manger.beginTransaction();
        transaction.add(R.id.main_linearlayout, listfg.get(0));
        transaction.commit();
        curIndex = 0;
        rg.setOnCheckedChangeListener(this);
    }

    public void changeFragment() {
        //验证当前显示的碎片和要切换的碎片是否为同一个
        if (curIndex != tarIndex)//不是同一个就切换
        {
            Fragment currF = listfg.get(curIndex);
            Fragment tarF = listfg.get(tarIndex);
            FragmentTransaction transaction = manger.beginTransaction();

            if (tarF.isAdded())//验证要切换的碎片是否添加过，如果添加过就显示
            {
                transaction.hide(currF).show(tarF);
                //隐藏当前的碎片对象，显示的要切换的碎片对象

            } else {//否则就添加
                //隐藏当前碎片对象，添加奥显示的碎片对象
                transaction.hide(currF).add(R.id.main_linearlayout, tarF);
            }
            transaction.commit();
            curIndex = tarIndex;
        } else {
            Toast.makeText(this, "已经是当前页啦", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int i = 0;
        for (; i < listrb.size(); i++) {
            if (listrb.get(i).getId() == checkedId) {
                tarIndex = i;
                //        listrb.get(i).setTextColor(Color.BLUE);
            }
        }
        {
            changeFragment();//切换碎片
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //每次进来应用都检查会话和好友请求的情况
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.5、通知有自定义消息接收
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

    /**
     *
     */
    private void checkRedPoint() {
        //TODO 会话：4.4、获取全部会话的未读消息数量
        int count = (int) BmobIM.getInstance().getAllUnReadCount();

        if (count > 0) {
         // iv_conversation_tips.setVisibility(View.VISIBLE);
        } else {
          // iv_conversation_tips.setVisibility(View.GONE);
        }
        //TODO 好友管理：是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
          // iv_contact_tips.setVisibility(View.VISIBLE);
        } else {
         //   iv_contact_tips.setVisibility(View.GONE);
        }
    }




}


