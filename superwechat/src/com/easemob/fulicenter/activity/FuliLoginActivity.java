package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.fulicenter.Constant;
import com.easemob.fulicenter.DemoHXSDKHelper;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.Result;
import com.easemob.fulicenter.bean.UserAvatar;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.db.UserDao;
import com.easemob.fulicenter.domain.User;
import com.easemob.fulicenter.task.DownloadContactsListTask;
import com.easemob.fulicenter.utils.CommonUtils;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.UserUtils;
import com.easemob.fulicenter.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuliLoginActivity extends Activity {
    private EditText usernameEditText;
    private EditText passwordEditText;

    private String currentUsername;
    private String currentPassword;

    private boolean progressShow;
    private boolean autoLogin = false;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果用户名密码都有，直接进入主页面
        if (DemoHXSDKHelper.getInstance().isLogined()) {
            autoLogin = true;
            startActivity(new Intent(FuliLoginActivity.this, FuliCenterMainActivity.class));
            return;
        }
        setContentView(R.layout.activity_fuli_login);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        setListener();

        if (FuliCenterApplication.getInstance().getUserName() != null) {
            usernameEditText.setText(FuliCenterApplication.getInstance().getUserName());
        }
    }

    private void setListener() {
        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 登录
     *
     * @param view
     */
    public void onFuliLogin(View view) {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        // 获取当前用户输入的用户名和密码
        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();
        // 如果用户名或者密码为空，则提示用户
        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        progressShow = true;
        pd = new ProgressDialog(FuliLoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                if (!progressShow) {
                    return;
                }
                loginAppServer();

            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loginAppServer() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,currentUsername)
                .addParam(I.User.PASSWORD,currentPassword)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String str) {
                        Result result = Utils.getResultFromJson(str, UserAvatar.class);
                        if (result != null & result.isRetMsg()) {
                            UserAvatar user = (UserAvatar) result.getRetData();
                            if (user != null) {
                                saveUser2DB(user);
                                loginSuccess(user);
                                downloadUserAvatar();
                            }
                        } else {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.Login_failed+ Utils.getResourceString(FuliLoginActivity.this,result.getRetCode()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        DemoHXSDKHelper.getInstance().logout(true,null);
                        Toast.makeText(getApplicationContext(), R.string.Login_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void downloadUserAvatar() {
        final OkHttpUtils2<Message> utils = new OkHttpUtils2<Message>();
        utils.url(UserUtils.getUserAvatarPath(currentUsername))
                .targetClass(Message.class)
                .doInBackground(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        byte[] data = response.body().bytes();
                        final String avatarUrl = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().uploadUserAvatar(data);
                    }
                })
                .execute(new OkHttpUtils2.OnCompleteListener<Message>() {
                    @Override
                    public void onSuccess(Message result) {

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void loginSuccess(UserAvatar user) {
        // 登陆成功，保存用户名密码
        FuliCenterApplication.getInstance().setUserName(currentUsername);
        FuliCenterApplication.getInstance().setPassword(currentPassword);
        FuliCenterApplication.getInstance().setUser(user);
        FuliCenterApplication.currentUserNick = user.getMUserNick();

        new DownloadContactsListTask(FuliLoginActivity.this,currentUsername).getContacts();
        try {
            // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
            // ** manually load all local groups and
            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
            // 处理好友和群组
            initializeContacts();
        } catch (Exception e) {
            e.printStackTrace();
            // 取好友或者群聊失败，不让进入主页面
            runOnUiThread(new Runnable() {
                public void run() {
                    pd.dismiss();
                    DemoHXSDKHelper.getInstance().logout(true,null);
                    Toast.makeText(getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
        boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                FuliCenterApplication.currentUserNick.trim());
        if (!updatenick) {
            Log.e("LoginActivity", "update current user nick fail");
        }
        if (!FuliLoginActivity.this.isFinishing() && pd.isShowing()) {
            pd.dismiss();
        }
        // 进入主页面
        Intent intent = new Intent(FuliLoginActivity.this,
                FuliCenterMainActivity.class);
        startActivity(intent);

        finish();
    }

    private void initializeContacts() {
        Map<String, User> userlist = new HashMap<String, User>();
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);

        // 存入内存
        ((DemoHXSDKHelper)HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(FuliLoginActivity.this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }

    private void saveUser2DB(UserAvatar user) {
        UserDao dao = new UserDao(FuliLoginActivity.this);
        dao.saveUserAvatar(user);
    }

    public void onFuliRegister(View view) {
        startActivityForResult(new Intent(this, FuliRegisterActivity.class), 0);
    }

    public void onPersonBack(View view) {
        Intent intent = new Intent(FuliLoginActivity.this, FuliCenterMainActivity.class);
        startActivity(intent);
        finish();
    }
}
