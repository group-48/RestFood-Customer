package com.dash.restfood_customer.models;

import java.sql.Time;
import java.util.Date;

public class Reserve {

    private String custName,shopId,shopName;
    private Integer tableno, guestno ;
    private Date date;
    private Time time;

    public Reserve(String custName, String shopId, String shopName, Integer tableno, Integer guestno, Date date, Time time) {
        this.custName = custName;
        this.shopId = shopId;
        this.shopName = shopName;
        this.tableno = tableno;
        this.guestno = guestno;
        this.date = date;
        this.time = time;
    }
    public Reserve(){}

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

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

    public Integer getTableno() {
        return tableno;
    }

    public void setTableno(Integer tableno) {
        this.tableno = tableno;
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
