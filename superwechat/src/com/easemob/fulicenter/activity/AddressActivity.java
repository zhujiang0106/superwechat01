package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.fulicenter.R;

public class AddressActivity extends Activity {
    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_person_title);
        tvTitle.setText("填写收货地址");
    }

    public void onPersonBack(View view) {
        finish();
    }
}
