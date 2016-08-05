package com.easemob.fulicenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.adapter.BoutiqueAdapter;
import com.easemob.fulicenter.adapter.CategoryAdapter;
import com.easemob.fulicenter.bean.BoutiqueBean;
import com.easemob.fulicenter.bean.CategoryChildBean;
import com.easemob.fulicenter.bean.CategoryGroupBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    FuliCenterMainActivity mContext;
    ExpandableListView melvCategory;

    CategoryAdapter mCategoryAdapter;
    List<CategoryGroupBean> mCategoryGroupList;
    List<CategoryChildBean> mCategoryChildList;
    List<ArrayList<CategoryChildBean>> mChildList;

    long mGroupId;
    int mPageId = 1;

    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_category, null);
        mCategoryGroupList = new ArrayList<CategoryGroupBean>();
        mCategoryChildList = new ArrayList<CategoryChildBean>();
        mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        initView(layout);
        initData();
        return layout;
    }

    private void initData() {
        findCategoryGroupList();
    }

    private void findCategoryChildList(List<CategoryGroupBean> groupList) {
        final int groupCount = groupList.size();
        Log.i("main", "groupCount" + groupCount);
        for (int i=0;i<groupCount;i++) {
            CategoryGroupBean groupBean = groupList.get(i);
            final int index = i;
            int id = groupBean.getId();
            Log.i("main", "这个ID是：：" + id);
            OkHttpUtils2<CategoryChildBean[]> utils = new OkHttpUtils2<CategoryChildBean[]>();
            utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                    .addParam(I.CategoryChild.PARENT_ID, String.valueOf(id))
                    .addParam(I.PAGE_ID, String.valueOf(mPageId))
                    .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                    .targetClass(CategoryChildBean[].class)
                    .execute(new OkHttpUtils2.OnCompleteListener<CategoryChildBean[]>() {
                        @Override
                        public void onSuccess(CategoryChildBean[] result) {
                            Log.i("main", "result=" + result);
                            if (result != null) {
                                ArrayList<CategoryChildBean> childList = Utils.array2List(result);
                                Log.i("main", "childList=" + childList.toString());
                                if (index == groupCount - 1) {

                                } else {
                                    mCategoryAdapter.initChildList(childList);
                                }
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }

    private void findCategoryGroupList() {
        OkHttpUtils2<CategoryGroupBean[]> utils = new OkHttpUtils2<CategoryGroupBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]>() {
                    @Override
                    public void onSuccess(CategoryGroupBean[] result) {
                        Log.i("main", "result=" + result);
                        if (result != null) {
                            List<CategoryGroupBean> groupList = Utils.array2List(result);
                            Log.i("main", "222" + groupList.toString());
                            mCategoryAdapter.initData(groupList);
                            findCategoryChildList(groupList);
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }

    private void initView(View layout) {
        mCategoryAdapter = new CategoryAdapter(mContext, mCategoryGroupList,mChildList);
        melvCategory = (ExpandableListView) layout.findViewById(R.id.rv_category);
        melvCategory.setAdapter(mCategoryAdapter);
        melvCategory.setGroupIndicator(null);
    }

}
