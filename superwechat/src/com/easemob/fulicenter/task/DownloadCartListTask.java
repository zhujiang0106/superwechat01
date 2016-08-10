package com.easemob.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.bean.CartBean;
import com.easemob.fulicenter.bean.GoodDetailsBean;
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
public class DownloadCartListTask {
    Context mContext;
    String userName;

    public DownloadCartListTask(Context mContext, String userName) {
        this.mContext = mContext;
        this.userName = userName;
    }

    public void getCartList() {
        OkHttpUtils2<CartBean[]> utils = new OkHttpUtils2<CartBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,userName)
                .addParam(I.PAGE_ID,String.valueOf(I.PAGE_ID_DEFAULT+1))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        Log.i("main", "result=" + result);
                        if (result != null) {
                            final ArrayList<CartBean> cartArrayList = Utils.array2List(result);
                            Log.i("main", "从服务端下载的购物车商品信息：" + cartArrayList.toString());
                            final ArrayList<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
                            final ArrayList<GoodDetailsBean> cartGoodList = FuliCenterApplication.getInstance().getCartGoodList();
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
                                                    if (cartBean.isChecked()) {
                                                        cartGoodList.add(result);
                                                    }
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
