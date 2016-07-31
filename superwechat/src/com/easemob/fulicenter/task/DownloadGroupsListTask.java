package com.easemob.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.bean.GroupAvatar;
import com.easemob.fulicenter.bean.Result;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;

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
                            FuliCenterApplication.getInstance().setGroupList(list);
                            mContext.sendStickyBroadcast(new Intent("update_group_list"));
                            for (GroupAvatar g : list) {
                                FuliCenterApplication.getInstance().getGroupMap().put(g.getMGroupHxid(), g);
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
