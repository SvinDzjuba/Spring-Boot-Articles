package com.example.springbootarticles.models;

import java.util.Date;

public class Subscription {
    private int counter;
    private Date expDate;
    private String plan;

    public Subscription(int counter, Date expDate, String plan, Date currentMonth) {
        this.counter = counter;
        this.expDate = expDate;
        this.plan = plan;
        this.currentMonth = currentMonth;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Date getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(Date currentMonth) {
        this.currentMonth = currentMonth;
    }

    private Date currentMonth;
}
