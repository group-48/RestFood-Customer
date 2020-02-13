package com.dash.restfood_customer.models;

import java.sql.Time;
import java.util.Date;

public class Reserve {

    private String userId;
    private String shopId;
    private Integer guestno;
    private String date;
    private String time;

    public Reserve(String userId, String shopId, Integer guestno, String date, String time) {
        this.userId = userId;
        this.shopId = shopId;
        this.guestno = guestno;
        this.date = date;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Integer getGuestno() {
        return guestno;
    }

    public void setGuestno(Integer guestno) {
        this.guestno = guestno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public  Reserve(String shopId, String uid, String bdate, String btime, int guestno){}
}