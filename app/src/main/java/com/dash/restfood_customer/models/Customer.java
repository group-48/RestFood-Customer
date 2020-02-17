package com.dash.restfood_customer.models;

public class Customer {
    public String fName,lName,email;
    public int phone;
    public String DOB;

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
