package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.viewholder.FlowIndicator;
import com.easemob.fulicenter.viewholder.SlideAutoLoopView;

public class GoodDetailsActivity extends Activity {
    ImageView ivShare,ivCollect, ivCart;
    TextView tvCartCount;

    TextView tvGoodNameEng,tvGoodNameChi,tvGoodPriceShop, tvGoodPriceCurrent;

    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;

    WebView wvGoodBrief;

    int mGoodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_details);
        initView();
        initData();
    }

    private void initData() {
        mGoodId = getIntent().getIntExtra("goodId", 0);
        Log.i("main", "mGoodId=" + mGoodId);
    }

    private void initView() {
        ivShare = (ImageView) findViewById(R.id.iv_good_share);
        ivCollect = (ImageView) findViewById(R.id.iv_good_collect);
        ivCart = (ImageView) findViewById(R.id.iv_good_cart);
        tvCartCount = (TextView) findViewById(R.id.tv_cart_count);

        tvGoodNameEng = (TextView) findViewById(R.id.tv_good_name_english);
        tvGoodNameChi = (TextView) findViewById(R.id.tv_good_name_chi);
        tvGoodPriceShop = (TextView) findViewById(R.id.tv_good_price_shop);
        tvGoodPriceCurrent = (TextView) findViewById(R.id.tv_good_price_current);

        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.sav);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.ti_indicator);
        wvGoodBrief = (WebView) findViewById(R.id.wv_good_brief);
    }

    public void onBack(View view) {
        finish();
    }
}
