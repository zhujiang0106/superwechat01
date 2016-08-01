package com.easemob.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.CartBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<CartBean> mCartList;
    CartViewHolder mCartViewHolder;
    public CartAdapter(Context context, List<CartBean> list) {
        mContext = context;
        mCartList = new ArrayList<CartBean>();
        mCartList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_cart, null);
        holder = new CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CartViewHolder) {
            mCartViewHolder = (CartViewHolder) holder;
            CartBean cart = mCartList.get(position);
            mCartViewHolder.tvCartName.setText(cart.getUserName());
            mCartViewHolder.tvCartCount.setText("("+cart.getCount()+")");
            mCartViewHolder.tvCartPrice.setText("$"+cart.getCount());
        }
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    private class CartViewHolder extends ViewHolder {
        LinearLayout layout;
        ImageView ivCartCheckbox,ivCartThumb;
        TextView tvCartName,tvCartCount,tvCartPrice;
        public CartViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout_cart);
            ivCartCheckbox = (ImageView) view.findViewById(R.id.iv_cart_checkbox);
            ivCartThumb = (ImageView) view.findViewById(R.id.iv_cart_thumb);
            tvCartName = (TextView) view.findViewById(R.id.tv_cart_name);
            tvCartCount = (TextView) view.findViewById(R.id.tv_cart_count);
            tvCartPrice = (TextView) view.findViewById(R.id.tv_cart_price);
        }
    }
}
