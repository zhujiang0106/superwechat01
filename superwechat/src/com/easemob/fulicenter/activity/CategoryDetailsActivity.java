package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.adapter.GoodAdapter;
import com.easemob.fulicenter.bean.NewGoodBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailsActivity extends Activity {
    CategoryDetailsActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    GoodAdapter mGoodAdapter;
    List<NewGoodBean> mGoodList;

    Button mSortPrice;
    Button mSortTime;
    boolean mSortPriceAsc;
    boolean mSortTimeAsc;
    int sortBy;

    int pageId = 1;
    TextView tvHint;
    int childId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        childId = getIntent().getIntExtra("category_child_id",0);
        mContext = this;
        mGoodList = new ArrayList<NewGoodBean>();
        sortBy = I.SORT_BY_ADDTIME_DESC;
        Log.i("main", "11SortBy=" + sortBy);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
        SortStatusChangeListener listener = new SortStatusChangeListener();
        Log.i("main", "22SortBy=" + sortBy);
        mSortPrice.setOnClickListener(listener);
        mSortTime.setOnClickListener(listener);
    }
    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastItemPosition >= mGoodAdapter.getItemCount() - 1
                        ) {
                    if (mGoodAdapter.isMore()) {
                        pageId++;
                        findNewGoodList(I.ACTION_PULL_UP,pageId);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvHint.setVisibility(View.VISIBLE);
                pageId = 1;
                findNewGoodList(I.ACTION_PULL_DOWN,pageId);
            }
        });
    }

    private void initData() {
        findNewGoodList(I.ACTION_DOWNLOAD,pageId);
    }

    private void findNewGoodList(final int action, int pageid) {
        OkHttpUtils2<NewGoodBean[]> utils = new OkHttpUtils2<NewGoodBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(childId))
                .addParam(I.PAGE_ID,String.valueOf(pageid))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
                    @Override
                    public void onSuccess(NewGoodBean[] result) {
                        Log.i("main", "result=" + result);
                        mGoodAdapter.setMore(true);
                        if (result != null) {
                            ArrayList<NewGoodBean> goodBeanArrayList = Utils.array2List(result);
                            Log.i("main", "goodBeanArrayList.size=" + goodBeanArrayList.size());
                            switch (action) {
                                case I.ACTION_DOWNLOAD:
                                    mGoodAdapter.initData(goodBeanArrayList);
                                    mGoodAdapter.setFooterText(getResources().getString(R.string.load_more));
                                    break;
                                case I.ACTION_PULL_DOWN:
                                    mGoodAdapter.initData(goodBeanArrayList);
                                    tvHint.setVisibility(View.GONE);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mGoodAdapter.setFooterText(getResources().getString(R.string.load_more));
                                    break;
                                case I.ACTION_PULL_UP:
                                    if (goodBeanArrayList.size() < I.PAGE_SIZE_DEFAULT) {
                                        mGoodAdapter.setMore(false);
                                        mGoodAdapter.setFooterText(getResources().getString(R.string.no_more));
                                    }
                                    mGoodAdapter.addData(goodBeanArrayList);
                                    break;
                            }
                        } else {
                            Toast.makeText(CategoryDetailsActivity.this,"该类别没有商品！",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_category);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_red,
                R.color.google_green,
                R.color.google_yellow
        );
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_category);
        // 设置布局每行的数量
        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        // 设置布局的方向
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mGoodAdapter = new GoodAdapter(mContext, mGoodList);
        mRecyclerView.setAdapter(mGoodAdapter);
        tvHint = (TextView) findViewById(R.id.tv_refresh_hint);
        mSortPrice = (Button) findViewById(R.id.btn_sort_price);
        mSortTime = (Button) findViewById(R.id.btn_sort_time);
    }
    public void onCategoryBack(View view) {
        finish();
    }

    class SortStatusChangeListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_sort_price:
                    Log.i("main", "33SortBy=" + sortBy);
                    if (mSortPriceAsc) {
                        sortBy = I.SORT_BY_PRICE_ASC;
                    } else {
                        sortBy = I.SORT_BY_PRICE_DESC;
                    }
                    mSortPriceAsc = !mSortPriceAsc;
                    break;
                case R.id.btn_sort_time:
                    Log.i("main", "44SortBy=" + sortBy);
                    if (mSortTimeAsc) {
                        sortBy = I.SORT_BY_ADDTIME_ASC;
                    } else {
                        sortBy = I.SORT_BY_ADDTIME_DESC;
                    }
                    Log.i("main", "55SortBy=" + sortBy);
                    mSortTimeAsc = !mSortTimeAsc;
                    break;
            }
            mGoodAdapter.setSortBy(sortBy);
        }
    }
}
