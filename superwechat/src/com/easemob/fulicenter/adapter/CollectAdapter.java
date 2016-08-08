package com.easemob.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.activity.GoodDetailsActivity;
import com.easemob.fulicenter.bean.CollectBean;
import com.easemob.fulicenter.bean.MessageBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.ImageUtils;
import com.easemob.fulicenter.viewholder.FooterViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.view.View.OnClickListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CollectAdapter extends Adapter<ViewHolder> {
    Context mContext;
    List<CollectBean> mCollectList;
    CollectViewHolder mCollectViewHolder;
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

    public CollectAdapter(Context context, List<CollectBean> list) {
        mContext = context;
        mCollectList = new ArrayList<CollectBean>();
        mCollectList.addAll(list);
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
                View view1 = inflater.inflate(R.layout.item_collect, parent,false);
                holder = new CollectViewHolder(view1);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CollectViewHolder) {
            Log.i("main", "count=" + mCollectList.size());
            mCollectViewHolder = (CollectViewHolder) holder;
            final CollectBean good = mCollectList.get(position);
            Log.i("main", "name=" + good.getGoodsName());
            ImageUtils.setGoodThumb(mContext, mCollectViewHolder.ivGoodThumb, good.getGoodsThumb());
            mCollectViewHolder.tvGoodName.setText(good.getGoodsName());
//            mCollectViewHolder.tvGoodPrice.setText(good.getCurrencyPrice());
            mCollectViewHolder.layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, GoodDetailsActivity.class).putExtra("goodId", good.getGoodsId()));
                }
            });
            mCollectViewHolder.mDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userName = FuliCenterApplication.getInstance().getUserName();
                    OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
                    utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                            .addParam(I.Collect.USER_NAME,userName)
                            .addParam(I.Collect.GOODS_ID, String.valueOf(good.getGoodsId()))
                            .targetClass(MessageBean.class)
                            .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean result) {
                                    if (result.isSuccess()) {
                                        Toast.makeText(mContext, "已经删除！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mContext, "删除失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(mContext, "删除失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
        if (holder instanceof FooterViewHolder) {
            mFooterViewHolder = (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerText);
        }
    }

    @Override
    public int getItemCount() {
        return mCollectList == null ? 1 : mCollectList.size() + 1;
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

    public void initData(ArrayList<CollectBean> goodBeanArrayList) {
        if (mCollectList != null) {
            mCollectList.clear();
        }
        mCollectList.addAll(goodBeanArrayList);
        notifyDataSetChanged();
    }
    public void addData(ArrayList<CollectBean> goodBeanArrayList) {
        mCollectList.addAll(goodBeanArrayList);
        notifyDataSetChanged();
    }

    private class CollectViewHolder extends ViewHolder {
        ImageView mDelete;
        LinearLayout layout;
        ImageView ivGoodThumb;
        TextView tvGoodName, tvGoodPrice;
        public CollectViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout_good);
            ivGoodThumb = (ImageView) view.findViewById(R.id.iv_good_thumb);
            tvGoodName = (TextView) view.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) view.findViewById(R.id.tv_good_price);
            mDelete = (ImageView) view.findViewById(R.id.ivDelete);
        }
    }

}
