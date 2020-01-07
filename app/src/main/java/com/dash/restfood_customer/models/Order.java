package com.dash.restfood_customer.models;

public class Order {
    private String Status,amount,state;
    private int Total;

    public Order(String oid,String amt){
        this.Status=oid;
        this.amount=amt;
    }

    public Order(int total,String oid) {
        Total = total;
        Status=oid;
    }



    public Order(){}

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String order_id) {
        this.Status = order_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
