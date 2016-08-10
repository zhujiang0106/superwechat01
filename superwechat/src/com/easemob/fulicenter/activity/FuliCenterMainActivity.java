package com.easemob.fulicenter.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easemob.fulicenter.DemoHXSDKHelper;
import com.easemob.fulicenter.FuliCenterApplication;
import com.easemob.fulicenter.R;
import com.easemob.fulicenter.bean.CartBean;
import com.easemob.fulicenter.utils.UserUtils;

import java.util.ArrayList;

public class FuliCenterMainActivity extends BaseActivity implements View.OnClickListener{
    Button btnNewGoods,btnBoutique,btnCategory,btnCart,btnPersonal;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    PersonFragment mPersonFragment;
    CartFragment mCartFragment;

    FragmentTransaction mTransaction;
    private Fragment[] fragments;

    TextView tvCartCount;

    private int index;
    private int currentIndex;

    public static final int ACTION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_center_main);
        initView();
        initData();
        setDrawable(btnNewGoods, R.drawable.menu_item_new_good_selected, Color.BLACK);
        setListener();
    }

    private void initData() {
        int count = UserUtils.getCartCount();
        Log.i("main", "购物车的数量是："+count);
        if (DemoHXSDKHelper.getInstance().isLogined() && count > 0) {
            tvCartCount.setText(String.valueOf(count));
        } else {
            tvCartCount.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        btnNewGoods.setOnClickListener(this);
        btnBoutique.setOnClickListener(this);
        btnCategory.setOnClickListener(this);
        btnCart.setOnClickListener(this);
        btnPersonal.setOnClickListener(this);
    }

    private void initView() {
        btnNewGoods = (Button) findViewById(R.id.btnNewGoods);
        btnBoutique = (Button) findViewById(R.id.btnBoutique);
        btnCategory = (Button) findViewById(R.id.btnCategory);
        btnCart = (Button) findViewById(R.id.btnCart);
        btnPersonal = (Button) findViewById(R.id.btnPersonal);

        tvCartCount = (TextView) findViewById(R.id.tv_main_cart_count);

        mNewGoodsFragment = new NewGoodsFragment();
        mBoutiqueFragment = new BoutiqueFragment();
        mCategoryFragment = new CategoryFragment();
        mPersonFragment = new PersonFragment();
        mCartFragment = new CartFragment();
        fragments = new Fragment[]{mNewGoodsFragment, mBoutiqueFragment, mCategoryFragment,mCartFragment,mPersonFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mNewGoodsFragment)
                .add(R.id.fragment_container, mBoutiqueFragment).hide(mBoutiqueFragment)
                .show(mNewGoodsFragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        initDrawable();
        switch (v.getId()) {
            case R.id.btnNewGoods:
                setDrawable(btnNewGoods, R.drawable.menu_item_new_good_selected, Color.BLACK);
                index = 0;
                break;
            case R.id.btnBoutique:
                setDrawable(btnBoutique, R.drawable.boutique_selected, Color.BLACK);
                index = 1;
                break;
            case R.id.btnCategory:
                setDrawable(btnCategory, R.drawable.menu_item_category_selected, Color.BLACK);
                index = 2;
                break;
            case R.id.btnCart:
                index = 3;
                setDrawable(btnCart, R.drawable.menu_item_cart_selected, Color.BLACK);
                break;
            case R.id.btnPersonal:
                index = 4;
                setDrawable(btnPersonal, R.drawable.menu_item_personal_center_selected, Color.BLACK);
                /*if (DemoHXSDKHelper.getInstance().isLogined()) {
                } else {
//                    startActivity(new Intent(this, FuliLoginActivity.class));
                    startActivityForResult(new Intent(this,FuliLoginActivity.class),ACTION);
                }*/
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if (currentIndex != index) {
            mTransaction = getSupportFragmentManager().beginTransaction();
            mTransaction.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                mTransaction.add(R.id.fragment_container, fragments[index]);
            }
            mTransaction.show(fragments[index]).commit();
            currentIndex = index;
        }
    }

    private void initDrawable() {
        setDrawable(btnNewGoods, R.drawable.menu_item_new_good_normal, Color.GRAY);
        setDrawable(btnBoutique, R.drawable.boutique_normal, Color.GRAY);
        setDrawable(btnCategory, R.drawable.menu_item_category_normal, Color.GRAY);
        setDrawable(btnCart, R.drawable.menu_item_cart_normal, Color.GRAY);
        setDrawable(btnPersonal, R.drawable.menu_item_personal_center_normal, Color.GRAY);
    }

    private void setDrawable(Button button, int id, int color) {
        button.setTextColor(color);
        Drawable drawable = ContextCompat.getDrawable(this, id);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        button.setCompoundDrawables(null,drawable,null,null);
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION) {
            if (DemoHXSDKHelper.getInstance().isLogined()) {

            } else {
//                currentIndex = 3;
            }
        }
    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        if (DemoHXSDKHelper.getInstance().isLogined()) {

        } else {
            index = currentIndex;
            if (index == 3) {
                index = 0;
            }
            setFragment();
        }
    }*/
}
