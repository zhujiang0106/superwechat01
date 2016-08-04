package com.easemob.fulicenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.CategoryChildBean;
import com.easemob.fulicenter.bean.CategoryGroupBean;
import com.easemob.fulicenter.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    List<CategoryGroupBean> mCategoryGroupList;
    List<List<CategoryChildBean>> mCategoryChildList;

    public CategoryAdapter(Context context, List<CategoryGroupBean> groupList, List<List<CategoryChildBean>> childList) {
        mContext = context;
        mCategoryGroupList = new ArrayList<CategoryGroupBean>();
        mCategoryGroupList.addAll(groupList);
        mCategoryChildList = new ArrayList<List<CategoryChildBean>>();
        mCategoryChildList.addAll(childList);
    }

    @Override
    public int getGroupCount() {
        return mCategoryGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCategoryChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int i) {
        if (mCategoryGroupList != null) {
            return mCategoryGroupList.get(i);
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mCategoryChildList != null) {
            return mCategoryChildList.get(groupPosition).get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        GroupViewHolder holder;
        CategoryGroupBean group = mCategoryGroupList.get(groupPosition);
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category, null);
            holder = new GroupViewHolder();
            holder.ivCategoryThumb = (ImageView) view.findViewById(R.id.iv_category_thumb);
            holder.ivCategoryExpand = (ImageView) view.findViewById(R.id.iv_category_expand);
            holder.tvCategoryName = (TextView) view.findViewById(R.id.tv_category_name);
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }
        ImageUtils.setGoodThumb(mContext, holder.ivCategoryThumb, group.getImageUrl());
        holder.tvCategoryName.setText(group.getName());
        if (isExpanded) {
            holder.ivCategoryExpand.setImageResource(R.drawable.expand_off);
        } else {
            holder.ivCategoryExpand.setImageResource(R.drawable.expand_on);
        }
        return view;
    }

    public void initData(List<CategoryGroupBean> groupList) {
        if (mCategoryGroupList != null) {
            mCategoryGroupList.clear();
        }
        mCategoryGroupList.addAll(groupList);
        notifyDataSetChanged();
    }

    public void initChildList(List<CategoryChildBean> childList) {
        mCategoryChildList.add(childList);
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        ImageView ivCategoryThumb;
        ImageView ivCategoryExpand;
        TextView tvCategoryName;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        CategoryChildBean child = mCategoryChildList.get(groupPosition).get(childPosition);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_child, null);
            holder = new ChildViewHolder();
            holder.ivChild = (ImageView) convertView.findViewById(R.id.iv_child_thumb);
            holder.tvChildName = (TextView) convertView.findViewById(R.id.tv_child_name);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        ImageUtils.setGoodThumb(mContext, holder.ivChild, child.getImageUrl());
        holder.tvChildName.setText(child.getName());
        return convertView;
    }
    class ChildViewHolder {
        ImageView ivChild;
        TextView tvChildName;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

}
