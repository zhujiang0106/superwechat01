package com.easemob.fulicenter.activity;


import android.content.Context;
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
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.adapter.BoutiqueAdapter;
import com.easemob.fulicenter.adapter.GoodAdapter;
import com.easemob.fulicenter.bean.BoutiqueBean;
import com.easemob.fulicenter.bean.NewGoodBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {
    FuliCenterMainActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;

    BoutiqueAdapter mBoutiqueAdapter;
    List<BoutiqueBean> mBoutiqueList;

    TextView tvHint;

    public BoutiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_boutique, null);
        mBoutiqueList = new ArrayList<BoutiqueBean>();
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setPullDownRefreshListener();
    }
    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvHint.setVisibility(View.VISIBLE);
                findBoutiqueList(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData() {
        findBoutiqueList(I.ACTION_DOWNLOAD);
    }

    private void findBoutiqueList(final int action) {
        OkHttpUtils2<BoutiqueBean[]> utils = new OkHttpUtils2<BoutiqueBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<BoutiqueBean[]>() {
                    @Override
                    public void onSuccess(BoutiqueBean[] result) {
                        Log.i("main", "result=" + result);
                        if (result != null) {
                            ArrayList<BoutiqueBean> BoutiqueArrayList = Utils.array2List(result);
                            Log.i("main", "BoutiqueArrayList.size=" + BoutiqueArrayList.size());
                            switch (action) {
                                case I.ACTION_DOWNLOAD:
                                    mBoutiqueAdapter.initData(BoutiqueArrayList);
                                    break;
                                case I.ACTION_PULL_DOWN:
                                    mBoutiqueAdapter.initData(BoutiqueArrayList);
                                    tvHint.setVisibility(View.GONE);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initView(View layout) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.srl_boutique);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_red,
                R.color.google_green,
                R.color.google_yellow
        );
        mBoutiqueAdapter = new BoutiqueAdapter(mContext, mBoutiqueList);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_boutique);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(mBoutiqueAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        tvHint = (TextView) layout.findViewById(R.id.tv_refresh_hint);
    }

}
