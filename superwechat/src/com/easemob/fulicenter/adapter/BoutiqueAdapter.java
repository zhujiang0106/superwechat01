package com.easemob.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.BoutiqueBean;
import com.easemob.fulicenter.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<BoutiqueBean> mBoutiqueList;
    BoutiqueViewHolder mBoutiqueViewHolder;
    public BoutiqueAdapter(Context context, List<BoutiqueBean> list) {
        mContext = context;
        mBoutiqueList = new ArrayList<BoutiqueBean>();
        mBoutiqueList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_boutique, null);
        holder = new BoutiqueViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof BoutiqueViewHolder) {
            mBoutiqueViewHolder = (BoutiqueViewHolder) holder;
            BoutiqueBean boutique = mBoutiqueList.get(position);
            ImageUtils.setGoodThumb(mContext, mBoutiqueViewHolder.ivBoutiqueThumb, boutique.getImageurl());
            mBoutiqueViewHolder.tvBoutiqueTitle.setText(boutique.getTitle());
            mBoutiqueViewHolder.tvBoutiqueName.setText(boutique.getName());
            mBoutiqueViewHolder.tvBoutiqueDesc.setText(boutique.getDescription());

        }
    }

    @Override
    public int getItemCount() {
        return mBoutiqueList.size();
    }

    public void initData(ArrayList<BoutiqueBean> boutiqueArrayList) {
        if (mBoutiqueList != null) {
            mBoutiqueList.clear();
        }
        mBoutiqueList.addAll(boutiqueArrayList);
        notifyDataSetChanged();
    }

    private class BoutiqueViewHolder extends ViewHolder {
        RelativeLayout layout;
        ImageView ivBoutiqueThumb;
        TextView tvBoutiqueTitle,tvBoutiqueName, tvBoutiqueDesc;
        public BoutiqueViewHolder(View view) {
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.layout_boutique);
            ivBoutiqueThumb = (ImageView) view.findViewById(R.id.iv_boutique_thumb);
            tvBoutiqueTitle = (TextView) view.findViewById(R.id.tv_boutique_title);
            tvBoutiqueName = (TextView) view.findViewById(R.id.tv_boutique_name);
            tvBoutiqueDesc = (TextView) view.findViewById(R.id.tv_boutique_desc);
        }
    }
}
