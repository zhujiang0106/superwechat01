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
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.adapter.BoutiqueAdapter;
import com.easemob.fulicenter.adapter.CategoryAdapter;
import com.easemob.fulicenter.bean.BoutiqueBean;
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
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;

    CategoryAdapter mCategoryAdapter;
    List<CategoryGroupBean> mCategoryGroupList;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_category, null);
        mCategoryGroupList = new ArrayList<CategoryGroupBean>();
        initView(layout);
        initData();
        return layout;
    }

    private void initData() {
        findCategoryGroupList();
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
                            ArrayList<CategoryGroupBean> categoryArrayList = Utils.array2List(result);
                            Log.i("main", "BoutiqueArrayList.size=" + categoryArrayList.size());
                            mCategoryAdapter.initData(categoryArrayList);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initView(View layout) {
        mCategoryAdapter = new CategoryAdapter(mContext, mCategoryGroupList);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_category);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

}
