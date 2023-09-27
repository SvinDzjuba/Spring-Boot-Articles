package com.example.springbootarticles.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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

    private Date currentMonth;
}
