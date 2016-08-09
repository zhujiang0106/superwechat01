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
import com.easemob.fulicenter.DemoHXSDKHelper;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.Albums;
import com.easemob.fulicenter.bean.GoodDetailsBean;
import com.easemob.fulicenter.bean.MessageBean;
import com.easemob.fulicenter.data.OkHttpUtils2;
import com.easemob.fulicenter.task.DownloadCollectCountTask;
import com.easemob.fulicenter.utils.I;
import com.easemob.fulicenter.viewholder.FlowIndicator;
import com.easemob.fulicenter.viewholder.SlideAutoLoopView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class GoodDetailsActivity extends Activity {
    ImageView ivShare,ivCollect, ivCart;
    TextView tvCartCount;

    TextView tvGoodNameEng,tvGoodNameChi,tvGoodPriceShop, tvGoodPriceCurrent;

    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;

    WebView wvGoodBrief;

    int mGoodId;
    GoodDetailsActivity mContext;

    boolean isCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_details);
        initView();
        initData();
        initCollect();
        setListener();
    }

    private void setListener() {
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
    }

    private void initCollect() {
        if (DemoHXSDKHelper.getInstance().isLogined()) {
            OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
            utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                    .addParam(I.Collect.USER_NAME,FuliCenterApplication.getInstance().getUserName())
                    .addParam(I.Collect.GOODS_ID, String.valueOf(mGoodId))
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                isCollect = true;
                                ivCollect.setImageResource(R.drawable.bg_collect_out);
                            } else {
                                isCollect = false;
                                ivCollect.setImageResource(R.drawable.bg_collect_in);
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }

    private void addCollectListener(final GoodDetailsBean result) {
        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = FuliCenterApplication.getInstance().getUserName();
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    if (!isCollect) {
                        addCollct(userName, result);
                    } else {
                        Toast.makeText(mContext,"该商品已经在收藏列表",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext,"请先登录！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addCollct(String userName,GoodDetailsBean result) {
        if (userName != null) {
            OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
            utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                    .addParam(I.Collect.USER_NAME, userName)
                    .addParam(I.Collect.GOODS_ID, String.valueOf(result.getGoodsId()))
                    .addParam(I.Collect.GOODS_NAME, result.getGoodsName())
                    .addParam(I.Collect.GOODS_ENGLISH_NAME, result.getGoodsEnglishName())
                    .addParam(I.Collect.GOODS_THUMB, result.getGoodsThumb())
                    .addParam(I.Collect.GOODS_IMG, result.getGoodsImg())
                    .addParam(I.Collect.ADD_TIME, String.valueOf(result.getAddTime()))
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result!=null&&result.isSuccess()) {
                                new DownloadCollectCountTask(mContext, FuliCenterApplication.getInstance().getUserName()).getCollectCount();
                                ivCollect.setImageResource(R.drawable.bg_collect_out);
                                isCollect = true;
                                Toast.makeText(mContext, "添加收藏成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "添加收藏失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mContext, "添加收藏失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
                            addCollectListener(result);
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

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    public void onBack(View view) {
        finish();
    }
}
