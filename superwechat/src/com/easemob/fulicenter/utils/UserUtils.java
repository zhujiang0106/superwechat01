package com.easemob.fulicenter.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.fulicenter.DemoHXSDKHelper;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.CartBean;
import com.easemob.fulicenter.bean.GoodDetailsBean;
import com.easemob.fulicenter.bean.MemberUserAvatar;
import com.easemob.fulicenter.bean.UserAvatar;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.domain.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static User getUserInfo(String username){
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(username);
        if(user == null){
            user = new User(username);
        }
            
        if(user != null){
            //demo没有这些数据，临时填充
        	if(TextUtils.isEmpty(user.getNick()))
        		user.setNick(username);
        }
        return user;
    }
    public static UserAvatar getAppUserInfo(String username){
        UserAvatar user = FuliCenterApplication.getInstance().getUserMap().get(username);
        if(user == null){
            user = new UserAvatar(username);
        }
        return user;
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    /**
     * 设置用户联系人头像
     * @param username
     */
    public static void setAppUserAvatar(Context context, String username, ImageView imageView){
        String path = "";
        if(path != null && username != null){
            path = getUserAvatarPath(username);
            Log.i("main", "path==" + path);
            Picasso.with(context).load(path).placeholder(R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    public static String getUserAvatarPath(String username) {
        StringBuilder path = new StringBuilder(I.SERVER_TOOL);
        path.append(I.QUESTION).append(I.KEY_REQUEST)
                .append(I.EQUALS).append(I.REQUEST_DOWNLOAD_AVATAR)
                .append(I.AND)
                .append(I.AVATAR_TYPE)
                .append(I.EQUALS).append(username);
        return path.toString();
    }

    /**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (user != null && user.getAvatar() != null) {
			Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}
    /**
     * 设置当前用户头像
     */
    public static void setAppCurrentUserAvatar(Context context, ImageView imageView) {
        String userName = FuliCenterApplication.getInstance().getUserName();
        setAppUserAvatar(context,userName,imageView);
    }
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
    	User user = getUserInfo(username);
    	if(user != null){
    		textView.setText(user.getNick());
    	}else{
    		textView.setText(username);
    	}
    }
    
    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }
    /**
     * 设置当前用户昵称
     */
    public static void setAppCurrentUserNick(TextView textView){
        UserAvatar user = FuliCenterApplication.getInstance().getUser();
        if (textView != null && user != null) {
            if (user.getMUserNick() != null) {
                textView.setText(user.getMUserNick());
            } else {
                textView.setText(user.getMUserName());
            }
        }
    }

    /**
     * 保存或更新某个用户
     * @param newUser
     */
	public static void saveUserInfo(User newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}

    /**
     * 设置用户好友昵称
     * @param username
     * @param nameTextview
     */
    public static void setAppUserNick(String username, TextView nameTextview) {
        UserAvatar user = getAppUserInfo(username);
       setAppUserNick(user,nameTextview);
    }
    /**
     * 设置用户昵称
     * @param user
     * @param nameTextview
     */
    public static void setAppUserNick(UserAvatar user, TextView nameTextview) {
        if (user != null) {
            if (user.getMUserNick() != null) {
                nameTextview.setText(user.getMUserNick());
            } else {
                nameTextview.setText(user.getMUserName());
            }
        }
    }

    public static void setAppMemberNick(String hxid, String username, TextView view) {
//        MemberUserAvatar member = getAppMemberInfo(hxid, username);
//        setMemberNick(view, member);
    }

    private static void setMemberNick(TextView view, MemberUserAvatar member) {
        if (member != null && member.getMUserNick() != null) {
            view.setText(member.getMUserNick());
        } else {
            view.setText(member.getMUserName());
        }
    }

    public static void getCartList(int pageId, final ArrayList<CartBean> cartList) {
        OkHttpUtils2<CartBean[]> utils = new OkHttpUtils2<CartBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,String.valueOf(FuliCenterApplication.getInstance().getUserName()))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        Log.i("main", "result=" + result);
                        if (result != null) {
                            final ArrayList<CartBean> cartArrayList = Utils.array2List(result);
                            Log.i("main", "从服务端下载的购物车商品信息：" + cartArrayList.toString());
//                            final ArrayList<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
                            for (final CartBean cartBean : cartArrayList) {
                                OkHttpUtils2<GoodDetailsBean> utils = new OkHttpUtils2<GoodDetailsBean>();
                                utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                                        .addParam(I.Cart.GOODS_ID, String.valueOf(cartBean.getGoodsId()))
                                        .targetClass(GoodDetailsBean.class)
                                        .execute(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                                            @Override
                                            public void onSuccess(GoodDetailsBean result) {
                                                if (result != null) {
                                                    cartBean.setGoods(result);
                                                    cartList.add(cartBean);
                                                }
                                            }

                                            @Override
                                            public void onError(String error) {

                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }
}
