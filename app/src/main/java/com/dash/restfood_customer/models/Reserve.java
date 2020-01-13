package com.dash.restfood_customer.models;

import java.sql.Time;
import java.util.Date;

public class Reserve {

    private String shopId,shopName;
    private Integer guestno ;
    private Date date;
    private Time time;

    public Reserve(String custName, String shopId, String shopName, Integer tableno, Integer guestno, Date date, Time time) {

        this.shopId = shopId;
        this.shopName = shopName;
        this.guestno = guestno;
        this.date = date;
        this.time = time;
    }
    public Reserve(){}


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
