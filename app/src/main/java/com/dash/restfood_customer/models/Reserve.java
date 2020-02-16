package com.dash.restfood_customer.models;

import java.sql.Time;
import java.util.Date;

public class Reserve {

    private String userId;
    private String shopName;
    private Integer guestno;
    private String date;
    private String time;
    private String bookingId;

    public Reserve(String userId, String shopName, Integer guestno, String date, String time,String bookingId) {
        this.userId = userId;
        this.shopName = shopName;
        this.guestno = guestno;
        this.date = date;
        this.time = time;
        this.bookingId=bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public  Reserve(){}
}