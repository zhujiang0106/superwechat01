package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.easemob.fulicenter.R;

public class AddressActivity extends Activity {
    EditText etOrderName,etOrderPhone,etOrderStreet;
    Spinner spinCity;
    TextView tvTitle;
    Button btnBuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
        setListener();
    }

    private void setListener() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
