package com.easemob.fulicenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.adapter.GoodAdapter;
import com.easemob.fulicenter.bean.NewGoodBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    FuliCenterMainActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    GoodAdapter mGoodAdapter;
    List<NewGoodBean> mGoodList;

    int pageId = 1;

    public NewGoodsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_new_goods, null);
        mGoodList = new ArrayList<NewGoodBean>();
        initView(layout);
        initData();
        return layout;
    }

    private void initData() {
        findNewGoodList();
    }
    private void findNewGoodList(/*OkHttpUtils2.OnCompleteListener<NewGoodBean[]> listener*/) {
        OkHttpUtils2<NewGoodBean[]> utils = new OkHttpUtils2<NewGoodBean[]>();
        Log.i("main", "1111");
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
                    @Override
                    public void onSuccess(NewGoodBean[] result) {
                        Log.i("main", "result=" + result);
                        if (result != null) {
                            Log.i("main", "result.lenth=" + result.length);
                            ArrayList<NewGoodBean> goodBeanArrayList = Utils.array2List(result);
                            mGoodAdapter.initData(goodBeanArrayList);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.i("main", "4444");
                    }
                });
        Log.i("main", "2222");
    }

    private void initView(View layout) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.srl);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_red,
                R.color.google_green,
                R.color.google_yellow
        );
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_new_goods);
        // 设置布局每行的数量
        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        // 设置布局的方向
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mGoodAdapter = new GoodAdapter(mContext, mGoodList);
        mRecyclerView.setAdapter(mGoodAdapter);
    }

}
