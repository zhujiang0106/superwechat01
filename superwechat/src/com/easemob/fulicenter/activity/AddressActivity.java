package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.CartBean;
import com.easemob.fulicenter.bean.GoodDetailsBean;
import com.easemob.fulicenter.utils.UserUtils;
import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddressActivity extends BaseActivity implements PaymentHandler {
    Context mContext;
    EditText etOrderName,etOrderPhone,etOrderStreet;
    Spinner spinCity;
    TextView tvTitle;
    Button btnBuy;
    private static String URL = "http://218.244.151.190/demo/charge";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_address);
        initView();
        setListener();

        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    private void setListener() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderName = etOrderName.getText().toString();
                String orderPhone = etOrderPhone.getText().toString();
                String orderStreet = etOrderStreet.getText().toString();
                String spinCity = AddressActivity.this.spinCity.getSelectedItem().toString();
                if (TextUtils.isEmpty(orderName)) {
                    Toast.makeText(mContext,"收货人不能为空！",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(orderPhone)) {
                    Toast.makeText(mContext,"手机号码不能为空！",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!orderPhone.matches("[\\d]{11}")) {
                    Toast.makeText(mContext,"手机号码格式错误！",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(spinCity)) {
                    Toast.makeText(mContext,"所在地区不能为空！",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(orderStreet)) {
                    Toast.makeText(mContext,"街道地址不能为空！",Toast.LENGTH_LONG).show();
                    return;
                }
                gotoStateMents();
            }
        });
    }

    private void gotoStateMents() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 计算总金额（以分为单位）
//        int amount = 0;
        int amount = UserUtils.getCartRankPrice();
        JSONArray billList = new JSONArray();
        ArrayList<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
        for (CartBean cart : cartList) {
            GoodDetailsBean goods = cart.getGoods();
            if (goods != null && cart.isChecked()) {
                billList.put(goods.getGoodsName() + " x " + cart.getCount());
            }
        }
        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", amount);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    private void initView() {
        btnBuy = (Button) findViewById(R.id.btn_address);
        etOrderName = (EditText) findViewById(R.id.et_address_buyer);
        etOrderPhone = (EditText) findViewById(R.id.et_address_tel);
        etOrderStreet = (EditText) findViewById(R.id.et_address_street);
        spinCity = (Spinner) findViewById(R.id.spi_address_city);
        tvTitle = (TextView) findViewById(R.id.tv_person_title);
        tvTitle.setText("填写收货地址");
    }

    public void onPersonBack(View view) {
        finish();
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {

            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果

            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    PingppLog.d("result::" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
