package com.easemob.chatuidemo.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.easemob.chatuidemo.SuperWeChatApplication;
import com.easemob.chatuidemo.bean.GroupAvatar;
import com.easemob.chatuidemo.bean.Result;
import com.easemob.chatuidemo.bean.UserAvatar;
import com.easemob.chatuidemo.data.OkHttpUtils2;
import com.easemob.chatuidemo.utils.I;
import com.easemob.chatuidemo.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadGroupsListTask {
    Context mContext;
    String userName;

    public DownloadGroupsListTask(Context mContext, String userName) {
        this.mContext = mContext;
        this.userName = userName;
    }

    public void getContacts() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_FIND_GROUP_BY_USER_NAME)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String str) {
                        Result result = Utils.getListResultFromJson(str, GroupAvatar.class);
                        ArrayList<GroupAvatar> list = (ArrayList<GroupAvatar>) result.getRetData();
                        if (list != null && list.size() > 0) {
                            SuperWeChatApplication.getInstance().setGroupList(list);
                            mContext.sendStickyBroadcast(new Intent("update_group_list"));
                            for (GroupAvatar g : list) {
                                SuperWeChatApplication.getInstance().getGroupMap().put(g.getMGroupHxid(), g);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main", error);
                    }
                });
    }
}
