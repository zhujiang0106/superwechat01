package com.easemob.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.activity.GoodDetailsActivity;
import com.easemob.fulicenter.bean.CartBean;
import com.easemob.fulicenter.bean.GoodDetailsBean;
import com.easemob.fulicenter.bean.NewGoodBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.ImageUtils;
import com.easemob.fulicenter.utils.UserUtils;
import com.easemob.fulicenter.viewholder.FooterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<CartBean> mCartList;
    CartViewHolder mCartViewHolder;

    FooterViewHolder mFooterViewHolder;

    boolean isMore;
    String footerText;

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }
    public CartAdapter(Context context, List<CartBean> list) {
        mContext = context;
        mCartList = new ArrayList<CartBean>();
        mCartList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case I.TYPE_FOOTER:
                View view = inflater.inflate(R.layout.item_footer, parent,false);
                holder = new FooterViewHolder(view);
                break;
            case I.TYPE_ITEM:
                View view1 = inflater.inflate(R.layout.item_cart, parent,false);
                holder = new CartViewHolder(view1);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CartViewHolder) {
            mCartViewHolder = (CartViewHolder) holder;
            final CartBean cart = mCartList.get(position);
            Log.i("main", "现在的mCartList==" + mCartList.toString());
            Log.i("main", "现在的mCartList的大小是==" + mCartList.size());
            ImageUtils.setGoodThumb(mContext, mCartViewHolder.ivCartThumb, cart.getGoods().getGoodsThumb());
            mCartViewHolder.tvCartName.setText(cart.getGoods().getGoodsName());
            mCartViewHolder.tvCartPrice.setText(cart.getGoods().getCurrencyPrice());
//            mCartViewHolder.tvCartCount.setText(cart.getCount());
//            getGood(cart);
        }
        if (holder instanceof FooterViewHolder) {
            mFooterViewHolder = (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerText);
        }
    }

    private void getGood(final CartBean cart) {
        OkHttpUtils2<GoodDetailsBean> utils = new OkHttpUtils2<GoodDetailsBean>();
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Cart.GOODS_ID, String.valueOf(cart.getGoodsId()))
                .targetClass(GoodDetailsBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                    @Override
                    public void onSuccess(GoodDetailsBean result) {
                        if (result != null) {
                            cart.setGoods(result);
//                            ImageUtils.setGoodThumb(mContext, mCartViewHolder.ivCartThumb, result.getGoodsThumb());
//                            mCartViewHolder.tvCartName.setText(result.getGoodsName());
//                            mCartViewHolder.tvCartPrice.setText(result.getCurrencyPrice());
//                            mCartViewHolder.tvCartCount.setText(cart.getCount());
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mCartList == null ? 1 : mCartList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initData(final ArrayList<CartBean> cart) {
        if (mCartList != null) {
            mCartList.clear();
        }
        mCartList.addAll(cart);
        notifyDataSetChanged();
    }
//    public void addData(ArrayList<CartBean> cart) {
//        mCartList.addAll(cart);
//        notifyDataSetChanged();
//    }

    private class CartViewHolder extends ViewHolder {
        RelativeLayout layout;
        ImageView ivCartCheckbox,ivCartThumb;
        TextView tvCartName,tvCartCount,tvCartPrice;
        public CartViewHolder(View view) {
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.layout_cart);
            ivCartCheckbox = (ImageView) view.findViewById(R.id.iv_cart_checkbox);
            ivCartThumb = (ImageView) view.findViewById(R.id.iv_cart_thumb);
            tvCartName = (TextView) view.findViewById(R.id.tv_cart_name);
            tvCartCount = (TextView) view.findViewById(R.id.tv_cart_count);
            tvCartPrice = (TextView) view.findViewById(R.id.tv_cart_price);
        }
    }
}
