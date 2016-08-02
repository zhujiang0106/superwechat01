package com.easemob.fulicenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.adapter.GoodAdapter;
import com.easemob.fulicenter.bean.NewGoodBean;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {
    FuliCenterMainActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;

    int pageId = 1;
    TextView tvHint;

    public BoutiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_boutique, null);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.srl_boutique);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_boutique);
//        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tvHint = (TextView) layout.findViewById(R.id.tv_refresh_hint);
    }

}
