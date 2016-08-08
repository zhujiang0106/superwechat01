package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.adapter.CollectAdapter;
import com.easemob.fulicenter.bean.CollectBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends Activity {
    CollectActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    CollectAdapter mCollectAdapter;
    List<CollectBean> mGoodList;

    int pageId = 1;
    TextView tvHint,tvTitle;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        name = getIntent().getStringExtra("title_name");
        mContext = this;
        mGoodList = new ArrayList<CollectBean>();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
    }
    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastItemPosition >= mCollectAdapter.getItemCount() - 1
                        ) {
                    if (mCollectAdapter.isMore()) {
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
        String userName = FuliCenterApplication.getInstance().getUserName();
        Log.i("main", "userName===" + userName);
        OkHttpUtils2<CollectBean[]> utils = new OkHttpUtils2<CollectBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME,String.valueOf(userName))
                .addParam(I.PAGE_ID,String.valueOf(pageid))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CollectBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CollectBean[]>() {
                    @Override
                    public void onSuccess(CollectBean[] result) {
                        Log.i("main", "result=" + result);
                        mCollectAdapter.setMore(true);
                        if (result != null) {
                            ArrayList<CollectBean> collectBeanArrayList = Utils.array2List(result);
                            Log.i("main", "collectBeanArrayList.size=" + collectBeanArrayList.size());
                            switch (action) {
                                case I.ACTION_DOWNLOAD:
                                    mCollectAdapter.initData(collectBeanArrayList);
                                    mCollectAdapter.setFooterText(getResources().getString(R.string.load_more));
                                    break;
                                case I.ACTION_PULL_DOWN:
                                    mCollectAdapter.initData(collectBeanArrayList);
                                    tvHint.setVisibility(View.GONE);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mCollectAdapter.setFooterText(getResources().getString(R.string.load_more));
                                    break;
                                case I.ACTION_PULL_UP:
                                    if (collectBeanArrayList.size() < I.PAGE_SIZE_DEFAULT) {
                                        mCollectAdapter.setMore(false);
                                        mCollectAdapter.setFooterText(getResources().getString(R.string.no_more));
                                    }
                                    mCollectAdapter.addData(collectBeanArrayList);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_red,
                R.color.google_green,
                R.color.google_yellow
        );
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_new_goods);
        // 设置布局每行的数量
        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        // 设置布局的方向
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mCollectAdapter = new CollectAdapter(mContext, mGoodList);
        mRecyclerView.setAdapter(mCollectAdapter);
        tvHint = (TextView) findViewById(R.id.tv_refresh_hint);
        tvTitle = (TextView) findViewById(R.id.tv_person_title);
        tvTitle.setText("收藏的宝贝");
    }
    public void onPersonBack(View view) {
        finish();
    }
}
