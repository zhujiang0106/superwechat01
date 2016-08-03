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
import com.easemob.fulicenter.bean.CategoryGroupBean;
import com.easemob.fulicenter.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<CategoryGroupBean> mCategoryList;
    CategoryViewHolder mCategoryViewHolder;
    public CategoryAdapter(Context context, List<CategoryGroupBean> list) {
        mContext = context;
        mCategoryList = new ArrayList<CategoryGroupBean>();
        mCategoryList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_category, null);
        holder = new CategoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CategoryViewHolder) {
            mCategoryViewHolder = (CategoryViewHolder) holder;
            CategoryGroupBean categoryGroup = mCategoryList.get(position);
            ImageUtils.setGoodThumb(mContext, mCategoryViewHolder.ivCategoryThumb, categoryGroup.getImageUrl());
            mCategoryViewHolder.tvCategoryName.setText(categoryGroup.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public void initData(ArrayList<CategoryGroupBean> categoryArrayList) {
        if (mCategoryList != null) {
            mCategoryList.clear();
        }
        mCategoryList.addAll(categoryArrayList);
        notifyDataSetChanged();
    }

    private class CategoryViewHolder extends ViewHolder {
        LinearLayout layout;
        ImageView ivCategoryThumb,ivCategoryExpand;
        TextView tvCategoryName;
        public CategoryViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout_category);
            ivCategoryThumb = (ImageView) view.findViewById(R.id.iv_category_thumb);
            ivCategoryExpand = (ImageView) view.findViewById(R.id.iv_category_expand);
            tvCategoryName = (TextView) view.findViewById(R.id.tv_category_name);
        }
    }
}
