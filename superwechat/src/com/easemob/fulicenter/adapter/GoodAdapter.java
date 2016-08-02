package com.easemob.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.NewGoodBean;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.ImageUtils;
import com.easemob.fulicenter.viewholder.FooterViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by Administrator on 2016/8/1.
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<NewGoodBean> mGoodList;
    GoodViewHolder mGoodViewHolder;
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

    public GoodAdapter(Context context, List<NewGoodBean> list) {
        mContext = context;
        mGoodList = new ArrayList<NewGoodBean>();
        mGoodList.addAll(list);
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
                View view1 = inflater.inflate(R.layout.item_good, parent,false);
                holder = new GoodViewHolder(view1);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof GoodViewHolder) {
            Log.i("main", "count=" + mGoodList.size());
            mGoodViewHolder = (GoodViewHolder) holder;
            NewGoodBean good = mGoodList.get(position);
            Log.i("main", "name=" + good.getGoodsName());
            ImageUtils.setGoodThumb(mContext, mGoodViewHolder.ivGoodThumb, good.getGoodsThumb());
            mGoodViewHolder.tvGoodName.setText(good.getGoodsName());
            mGoodViewHolder.tvGoodPrice.setText(good.getCurrencyPrice());
        }
        if (holder instanceof FooterViewHolder) {
            mFooterViewHolder = (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerText);
        }
    }

    @Override
    public int getItemCount() {
        return mGoodList == null ? 1 : mGoodList.size() + 1;
//        return mGoodList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    public void initData(ArrayList<NewGoodBean> goodBeanArrayList) {
        if (mGoodList != null) {
            mGoodList.clear();
        }
        mGoodList.addAll(goodBeanArrayList);
        notifyDataSetChanged();
    }
    public void addData(ArrayList<NewGoodBean> goodBeanArrayList) {
        for (NewGoodBean ngb : goodBeanArrayList) {
            if (!mGoodList.contains(ngb)) {
                mGoodList.add(ngb);
            }
        }
//        mGoodList.addAll(goodBeanArrayList);
        notifyDataSetChanged();
    }

    private class GoodViewHolder extends ViewHolder {
        LinearLayout layout;
        ImageView ivGoodThumb;
        TextView tvGoodName, tvGoodPrice;
        public GoodViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout_good);
            ivGoodThumb = (ImageView) view.findViewById(R.id.iv_good_thumb);
            tvGoodName = (TextView) view.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) view.findViewById(R.id.tv_good_price);
        }
    }
}
