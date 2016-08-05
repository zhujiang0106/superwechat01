package com.easemob.fulicenter.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.fulicenter.DemoHXSDKHelper;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.BoutiqueBean;
import com.easemob.fulicenter.bean.Result;
import com.easemob.fulicenter.bean.UserAvatar;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.db.UserDao;
import com.easemob.fulicenter.task.DownloadContactsListTask;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {
    FuliCenterMainActivity mContext;
    private static final int sleepTime = 1000;

    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_boutique, null);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            public void run() {
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();

                    String userName = FuliCenterApplication.getInstance().getUserName();
                    UserDao dao = new UserDao(mContext);
                    UserAvatar user = dao.getUserAvatar(userName);
                    if (user == null) {
                        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
                        utils.setRequestUrl(I.REQUEST_FIND_USER)
                                .addParam(I.User.USER_NAME,userName)
                                .targetClass(String.class)
                                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                                    @Override
                                    public void onSuccess(String str) {
                                        Result result = Utils.getResultFromJson(str, UserAvatar.class);
                                        if (result != null & result.isRetMsg()) {
                                            UserAvatar user = (UserAvatar) result.getRetData();
                                            if (user != null) {
                                                FuliCenterApplication.getInstance().setUser(user);
                                                FuliCenterApplication.currentUserNick = user.getMUserNick();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                    }
                                });
                    } else {
                        FuliCenterApplication.getInstance().setUser(user);
                        FuliCenterApplication.currentUserNick = user.getMUserNick();
                    }
                    new DownloadContactsListTask(mContext,userName).getContacts();

                    long costTime = System.currentTimeMillis() - start;
                    //等待sleeptime时长
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //进入主页面
                    startActivity(new Intent(mContext, FuliCenterMainActivity.class));
                }else {
                    // 因为未登陆，闪屏进入登陆界面
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(mContext, FuliLoginActivity.class));
                    mContext.finish();
                }
            }
        }).start();
    }
}
