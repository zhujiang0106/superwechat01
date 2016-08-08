package com.easemob.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.bean.MessageBean;
import com.easemob.fulicenter.bean.Result;
import com.easemob.fulicenter.bean.UserAvatar;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadCollectCountTask {
    Context mContext;
    String userName;

    public DownloadCollectCountTask(Context mContext, String userName) {
        this.mContext = mContext;
        this.userName = userName;
    }

    public void getCollectCount() {
        final OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Contact.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        if (msg != null) {
                            if (msg.isSuccess()) {
                                FuliCenterApplication.getInstance().setCollectCount(Integer.valueOf(msg.getMsg()));
                            } else {
                                FuliCenterApplication.getInstance().setCollectCount(0);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_collect"));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
