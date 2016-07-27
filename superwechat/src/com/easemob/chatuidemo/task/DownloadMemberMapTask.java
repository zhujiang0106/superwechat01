package com.easemob.chatuidemo.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.easemob.chatuidemo.SuperWeChatApplication;
import com.easemob.chatuidemo.bean.MemberUserAvatar;
import com.easemob.chatuidemo.bean.Result;
import com.easemob.chatuidemo.bean.UserAvatar;
import com.easemob.chatuidemo.data.OkHttpUtils2;
import com.easemob.chatuidemo.utils.I;
import com.easemob.chatuidemo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadMemberMapTask {
    Context mContext;
    String hxid;

    public DownloadMemberMapTask(Context mContext, String hxid) {
        this.mContext = mContext;
        this.hxid = hxid;
    }

    public void getMembers() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID)
                .addParam(I.Member.GROUP_HX_ID,hxid)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String str) {
                        Result result = Utils.getListResultFromJson(str, MemberUserAvatar.class);
                        ArrayList<MemberUserAvatar> list = (ArrayList<MemberUserAvatar>) result.getRetData();
                        if (list != null && list.size() > 0) {
                            Map<String, HashMap<String, MemberUserAvatar>> memberMap = SuperWeChatApplication.getInstance().getMemberMap();
                            if (!memberMap.containsKey(hxid)) {
                                memberMap.put(hxid, new HashMap<String, MemberUserAvatar>());
                            }
                            HashMap<String, MemberUserAvatar> map = memberMap.get(hxid);
                            for (MemberUserAvatar u : list) {
                                Log.i("main", u.getMUserName());
                                map.put(u.getMUserName(), u);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_member_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main", error);
                    }
                });
    }
}
