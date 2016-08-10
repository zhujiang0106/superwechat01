package com.easemob.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.CartBean;
import com.easemob.fulicenter.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<CartBean> mCartList;
    CartViewHolder mCartViewHolder;

    boolean isMore;

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
        View view = inflater.inflate(R.layout.item_cart, parent,false);
        holder = new CartViewHolder(view);
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
            mCartViewHolder.tvCartCount.setText("("+cart.getCount()+")");
        }
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public void initData(final ArrayList<CartBean> cart) {
        if (mCartList != null) {
            mCartList.clear();
        }
        mCartList.addAll(cart);
        notifyDataSetChanged();
    }
    public void addData(ArrayList<CartBean> cart) {
        mCartList.addAll(cart);
        notifyDataSetChanged();
    }

    private class CartViewHolder extends ViewHolder {
        RelativeLayout layout;
        ImageView ivCartThumb;
        CheckBox cbCartSelect;
        TextView tvCartName,tvCartCount,tvCartPrice;
        public CartViewHolder(View view) {
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.layout_cart);
            cbCartSelect = (CheckBox) view.findViewById(R.id.cb_cart_checkbox);
            ivCartThumb = (ImageView) view.findViewById(R.id.iv_cart_thumb);
            tvCartName = (TextView) view.findViewById(R.id.tv_cart_name);
            tvCartCount = (TextView) view.findViewById(R.id.tv_cart_count);
            tvCartPrice = (TextView) view.findViewById(R.id.tv_cart_price);
        }
    }
}
