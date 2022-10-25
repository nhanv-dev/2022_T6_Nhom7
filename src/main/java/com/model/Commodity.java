package com.model;

import java.util.Date;

public class Commodity {
    private String name;
    private double price, daily, weekly, monthly, yearly;
    private Date createdDate;

    public Commodity() {
    }

    public Commodity(String name, double price, double daily, double weekly, double monthly, double yearly) {
        this.name = name;
        this.price = price;
        this.daily = daily;
        this.weekly = weekly;
        this.monthly = monthly;
        this.yearly = yearly;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", daily=" + daily +
                ", weekly=" + weekly +
                ", monthly=" + monthly +
                ", yearly=" + yearly +
                ", createdDate=" + createdDate +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDaily() {
        return daily;
    }

    public void setDaily(double daily) {
        this.daily = daily;
    }

    public double getWeekly() {
        return weekly;
    }

    public void setWeekly(double weekly) {
        this.weekly = weekly;
    }

    public double getMonthly() {
        return monthly;
    }

    public void setMonthly(double monthly) {
        this.monthly = monthly;
    }

    public double getYearly() {
        return yearly;
    }

    public void setYearly(double yearly) {
        this.yearly = yearly;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
