package com.easemob.fulicenter.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/11.
 */
public class OrderBean implements Serializable {
    private String orderName;
    private String orderPhone;
    private String orderCity;
    private String orderStreet;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderCity() {
        return orderCity;
    }

    public void setOrderCity(String orderCity) {
        this.orderCity = orderCity;
    }

    public String getOrderStreet() {
        return orderStreet;
    }

    public void setOrderStreet(String orderStreet) {
        this.orderStreet = orderStreet;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "orderName='" + orderName + '\'' +
                ", orderPhone='" + orderPhone + '\'' +
                ", orderCity='" + orderCity + '\'' +
                ", orderStreet='" + orderStreet + '\'' +
                '}';
    }
}
