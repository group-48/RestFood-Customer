package com.dash.restfood_customer.models;

public class Customer {
    private String fName,lName,email;
    private int phone;
    private String DOB;


    public Customer(String email,String fName, String lName, int phone, String DOB) {
        this.email=email;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.DOB = DOB;
    }


    public Customer(){

    }

    public String getfName() {
        return fName;
    }


    public String getlName() {
        return lName;
    }




}
