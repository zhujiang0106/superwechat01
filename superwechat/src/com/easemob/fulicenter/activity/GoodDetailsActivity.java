package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.fulicenter.D;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.Albums;
import com.easemob.fulicenter.bean.GoodDetailsBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.utils.I;
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
    GoodDetailsActivity mContext;

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
        if (mGoodId > 0) {
            getGoodDetailsByGoodId();
        } else {
            finish();
            Toast.makeText(mContext,"获取商品详情数据失败！",Toast.LENGTH_SHORT).show();
        }
    }

    private void getGoodDetailsByGoodId() {
        OkHttpUtils2<GoodDetailsBean> utils = new OkHttpUtils2<GoodDetailsBean>();
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(mGoodId))
                .targetClass(GoodDetailsBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                    @Override
                    public void onSuccess(GoodDetailsBean result) {
                        if (result != null) {
                            showGoodDetailsData(result);
                        } else {
                            finish();
                            Log.i("main", "没有得到返回的result数据");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        finish();
                        Log.i("main", "error="+error);
                    }
                });
    }

    private void showGoodDetailsData(GoodDetailsBean result) {
        tvGoodNameEng.setText(result.getGoodsEnglishName());
        tvGoodNameChi.setText(result.getGoodsName());
        tvGoodPriceShop.setText(result.getShopPrice());
        tvGoodPriceCurrent.setText(result.getCurrencyPrice());
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator,getImageUrl(result),getImageSize(result));
        wvGoodBrief.loadDataWithBaseURL(null,result.getGoodsBrief(),D.TEXT_HTML,D.UTF_8,null);
    }

    private String[] getImageUrl(GoodDetailsBean result) {
        String[] imageUrl = new String[]{};
        if (result.getProperties() != null && result.getProperties().length > 0) {
            Albums[] albums = result.getProperties()[0].getAlbums();
            imageUrl = new String[albums.length];
            for (int i=0;i<imageUrl.length;i++) {
                imageUrl[i] = albums[i].getImgUrl();
            }
        }
        return imageUrl;
    }

    private int getImageSize(GoodDetailsBean result) {
        if (result.getProperties() != null && result.getProperties().length > 0) {
            return result.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private void initView() {
        ivShare = (ImageView) findViewById(R.id.iv_good_share);
        ivCollect = (ImageView) findViewById(R.id.iv_good_collect);
        ivCart = (ImageView) findViewById(R.id.iv_good_cart);
        tvCartCount = (TextView) findViewById(R.id.tv_cart_count);

        mContext = this;

        tvGoodNameEng = (TextView) findViewById(R.id.tv_good_name_english);
        tvGoodNameChi = (TextView) findViewById(R.id.tv_good_name_chi);
        tvGoodPriceShop = (TextView) findViewById(R.id.tv_good_price_shop);
        tvGoodPriceCurrent = (TextView) findViewById(R.id.tv_good_price_current);

        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.sav);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.ti_indicator);
        wvGoodBrief = (WebView) findViewById(R.id.wv_good_brief);
        WebSettings settings = wvGoodBrief.getSettings();
        //设置列数（单列）
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }

    public void onBack(View view) {
        finish();
    }
}