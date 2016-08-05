package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.easemob.fulicenter.R;

public class FuliRegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_register);
    }

    public void onPersonBack(View view) {
        finish();
    }
}
