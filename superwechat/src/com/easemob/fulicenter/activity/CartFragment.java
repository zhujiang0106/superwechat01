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
import android.widget.TextView;

import com.easemob.fulicenter.D;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.adapter.CartAdapter;
import com.easemob.fulicenter.adapter.GoodAdapter;
import com.easemob.fulicenter.bean.CartBean;
import com.easemob.fulicenter.bean.GoodDetailsBean;
import com.easemob.fulicenter.bean.NewGoodBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.ImageUtils;
import com.easemob.fulicenter.utils.UserUtils;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    FuliCenterMainActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    CartAdapter mCartAdapter;
    List<CartBean> mCartList;

    int pageId = 1;
    TextView tvHint;



    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_cart, null);
        mCartList = new ArrayList<CartBean>();
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setPullDownRefreshListener();
//        setPullUpRefreshListener();
    }

    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastItemPosition >= mCartAdapter.getItemCount() - 1
                        ) {
                    if (mCartAdapter.isMore()) {
                        pageId++;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
                mCartAdapter.initData(cartList);
                tvHint.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initData() {
        ArrayList<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
        Log.i("main", "最新的cartList：" + cartList);
        mCartAdapter.initData(cartList);
    }
    /*private void findNewGoodList(final int action, int pageid) {
        OkHttpUtils2<CartBean[]> utils = new OkHttpUtils2<CartBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,String.valueOf(FuliCenterApplication.getInstance().getUserName()))
                .addParam(I.PAGE_ID,String.valueOf(pageid))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        Log.i("main", "result=" + result);
                        mCartAdapter.setMore(true);
                        if (result != null) {
                            final ArrayList<CartBean> cartArrayList = Utils.array2List(result);
                            Log.i("main", "从服务端下载的购物车商品信息：" + cartArrayList.toString());
                            switch (action) {
                                case I.ACTION_DOWNLOAD:
                                    Log.i("main", "cartList====" + cartArrayList);
                                    mCartAdapter.initData(cartArrayList);
                                    mCartAdapter.setFooterText(getResources().getString(R.string.load_more));
                                    break;
                                case I.ACTION_PULL_DOWN:
                                    mCartAdapter.initData(cartArrayList);
                                    tvHint.setVisibility(View.GONE);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mCartAdapter.setFooterText(getResources().getString(R.string.load_more));
                                    break;
                                case I.ACTION_PULL_UP:
                                    if (cartArrayList.size() < I.PAGE_SIZE_DEFAULT) {
                                        mCartAdapter.setMore(false);
                                        mCartAdapter.setFooterText(getResources().getString(R.string.no_more));
                                    }
                                    mCartAdapter.addData(cartArrayList);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }*/

    private void initView(View layout) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.srl_cart);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_red,
                R.color.google_green,
                R.color.google_yellow
        );
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_cart);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCartAdapter = new CartAdapter(mContext, mCartList);
        mRecyclerView.setAdapter(mCartAdapter);
        tvHint = (TextView) layout.findViewById(R.id.tv_refresh_hint);

    }

}