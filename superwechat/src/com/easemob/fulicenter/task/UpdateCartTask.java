package com.easemob.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.bean.CartBean;
import com.easemob.fulicenter.bean.GoodDetailsBean;
import com.easemob.fulicenter.bean.MessageBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/20.
 */
public class UpdateCartTask {
    Context mContext;
    CartBean mCart;
    boolean isChanged;
    public UpdateCartTask(Context mContext, CartBean mCart,boolean isChanged) {
        this.mContext = mContext;
        this.mCart = mCart;
        this.isChanged = isChanged;
    }

    public void updareCart() {
        ArrayList<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
        if (cartList.contains(mCart)) {
            if (mCart.getCount() > 0) {
                updateCart(cartList);
            } else {
                //删除购物车数据
                deleteCart(cartList);
            }
        } else {
            //新增购物车数据
            addCart(cartList);
        }
    }

    private void addCart(final ArrayList<CartBean> cartList) {
        final ArrayList<GoodDetailsBean> cartGoodList = FuliCenterApplication.getInstance().getCartGoodList();
        OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID, String.valueOf(mCart.getGoodsId()))
                .addParam(I.Cart.COUNT, String.valueOf(mCart.getCount()))
                .addParam(I.Cart.IS_CHECKED, String.valueOf(mCart.isChecked()))
                .addParam(I.Cart.USER_NAME,mCart.getUserName())
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            cartList.add(mCart);
                            cartGoodList.add(mCart.getGoods());
                            mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void deleteCart(final ArrayList<CartBean> cartList) {
        final ArrayList<GoodDetailsBean> cartGoodList = FuliCenterApplication.getInstance().getCartGoodList();
        OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID, String.valueOf(mCart.getId()))
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            cartList.remove(mCart);
                            if (cartGoodList.contains(mCart.getGoods())) {
                                cartGoodList.remove(mCart.getGoods());
                                mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void updateCart(final ArrayList<CartBean> cartList) {
        final ArrayList<GoodDetailsBean> cartGoodList = FuliCenterApplication.getInstance().getCartGoodList();
        Log.i("main", "cartGoodList.size???" + cartGoodList.size());
        OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID, String.valueOf(mCart.getId()))
                .addParam(I.Cart.COUNT, String.valueOf(mCart.getCount()))
                .addParam(I.Cart.IS_CHECKED, String.valueOf(mCart.isChecked()))
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            cartList.set(cartList.indexOf(mCart), mCart);
                            if (isChanged) {
                                if (cartGoodList.contains(mCart.getGoods())) {
                                    cartGoodList.remove(mCart.getGoods());
                                } else {
                                    cartGoodList.add(mCart.getGoods());
                                }
                            }
                            Log.i("main", "cartGoodList.size==" + cartGoodList.size());
                            mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
