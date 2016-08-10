package com.easemob.fulicenter.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.fulicenter.DemoHXSDKHelper;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.Result;
import com.easemob.fulicenter.bean.UserAvatar;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.db.UserDao;
import com.easemob.fulicenter.task.DownloadContactsListTask;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.UserUtils;
import com.easemob.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {
    FuliCenterMainActivity mContext;
    TextView tvCollectCount;
    ImageView ivAvatar;
    TextView tvName,tvSetting;
    RelativeLayout mCollect;
    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = inflater.inflate(R.layout.fragment_person, container, false);
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void initData() {
        String userName = FuliCenterApplication.getInstance().getUserName();
        UserDao dao = new UserDao(mContext);
        UserAvatar user = dao.getUserAvatar(userName);
        if (user != null) {
            tvName.setText(user.getMUserNick());
        }
        UserUtils.setAppCurrentUserAvatar(mContext, ivAvatar);
    }

    private void setListener() {
        MySetOnClickLIstener listener = new MySetOnClickLIstener();
        tvSetting.setOnClickListener(listener);
        mCollect.setOnClickListener(listener);
        updateCollectCountListener();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class MySetOnClickLIstener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_person_set:
                    startActivity(new Intent(mContext, SettingsActivity.class));
                    break;
                case R.id.layout_person_collect_goods:
                    startActivity(new Intent(mContext, CollectActivity.class));
                    break;
            }
        }
    }

    private void initView(View layout) {
        ivAvatar = (ImageView) layout.findViewById(R.id.iv_person_avatar);
        tvName = (TextView) layout.findViewById(R.id.tv_person_name);

        tvSetting = (TextView) layout.findViewById(R.id.tv_person_set);
        tvCollectCount = (TextView) layout.findViewById(R.id.tv_person_good_count);
        mCollect = (RelativeLayout) layout.findViewById(R.id.layout_person_collect_goods);
    }

    @Override
    public void onStart() {
        super.onStart();
        // ** 免登陆情况 加载所有本地群和会话
        //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
        //加上的话保证进了主页面会话和群组都已经load完毕
        long start = System.currentTimeMillis();
        EMGroupManager.getInstance().loadAllGroups();
        EMChatManager.getInstance().loadAllConversations();
        String currentUser = EMChatManager.getInstance().getCurrentUser();
        Log.i("main", "当前用户是：" + currentUser);
        String userName = FuliCenterApplication.getInstance().getUserName();
        Log.i("main", "当前用户名是什么？" + userName);
        UserDao dao = new UserDao(mContext);
        UserAvatar user = dao.getUserAvatar(userName);
        if (user == null) {
            Log.i("main", "有没有啊");
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
            Log.i("main", "这里应该有吧？" + user.toString());
            FuliCenterApplication.getInstance().setUser(user);
            FuliCenterApplication.currentUserNick = user.getMUserNick();
        }
        new DownloadContactsListTask(mContext,userName).getContacts();
//        myThread();
    }

    /*private void myThread() {
        new Thread(new Runnable() {
            public void run() {
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    String currentUser = EMChatManager.getInstance().getCurrentUser();
                    Log.i("main", "当前用户是：" + currentUser);
                    String userName = FuliCenterApplication.getInstance().getUserName();
                    Log.i("main", "当前用户名是什么？" + userName);
                    UserDao dao = new UserDao(mContext);
                    UserAvatar user = dao.getUserAvatar(userName);
                    if (user == null) {
                        Log.i("main", "有没有啊");
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
                        Log.i("main", "这里应该有吧？" + user.toString());
                        FuliCenterApplication.getInstance().setUser(user);
                        FuliCenterApplication.currentUserNick = user.getMUserNick();
                    }
                    new DownloadContactsListTask(mContext,userName).getContacts();
                }else {
                    // 因为未登陆，闪屏进入登陆界面
                    startActivity(new Intent(mContext, FuliLoginActivity.class));
                    mContext.finish();
                }
            }
        }).start();
    }*/

    class UpdateCollectCount extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int count = FuliCenterApplication.getInstance().getCollectCount();
            Log.i("main", "count==" + count);
            tvCollectCount.setText(String.valueOf(count));
        }
    }

    UpdateCollectCount mReceiver;
    private void updateCollectCountListener() {
        mReceiver = new UpdateCollectCount();
        IntentFilter filter = new IntentFilter("update_collect");
        mContext.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);
    }
}
