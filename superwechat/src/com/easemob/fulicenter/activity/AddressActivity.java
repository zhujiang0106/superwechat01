package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.fulicenter.R;

public class AddressActivity extends Activity {
    Context mContext;
    EditText etOrderName,etOrderPhone,etOrderStreet;
    Spinner spinCity;
    TextView tvTitle;
    Button btnBuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_address);
        initView();
        setListener();
    }

    private void setListener() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderName = etOrderName.getText().toString();
                String orderPhone = etOrderPhone.getText().toString();
                String orderStreet = etOrderStreet.getText().toString();
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
                if (TextUtils.isEmpty(orderStreet)) {
                    Toast.makeText(mContext,"街道地址不能为空！",Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(mContext,"亲，购买成功了哟！",Toast.LENGTH_LONG).show();
            }
        });
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
}
