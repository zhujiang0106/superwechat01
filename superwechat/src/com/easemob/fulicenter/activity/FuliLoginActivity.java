package com.easemob.fulicenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.fulicenter.R;
import com.easemob.fulicenter.utils.CommonUtils;

public class FuliLoginActivity extends Activity {
    private EditText usernameEditText;
    private EditText passwordEditText;

    private String currentUsername;
    private String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_login);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
    }

    public void onFuliLogin(View view) {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        // 获取当前用户输入的用户名和密码
        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();
        // 如果用户名或者密码为空，则提示用户
        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "恭喜！登陆成功!", Toast.LENGTH_SHORT).show();

    }
    public void onFuliRegister(View view) {
        startActivityForResult(new Intent(this, FuliRegisterActivity.class), 0);
    }

    public void onPersonBack(View view) {
        Intent intent = new Intent(FuliLoginActivity.this, FuliCenterMainActivity.class);
        startActivity(intent);
        finish();
    }
}
