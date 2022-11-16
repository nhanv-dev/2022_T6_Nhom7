package com.model;

import com.util.LoggerUtil;
import com.util.SlugGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Commodity {
    private String naturalKey, name, unit, category;
    private double price, percent;
    private Date createdDate, expiredDate;

    public Commodity() {
    }

    public Commodity(String naturalKey, String name, String unit, double price, double percent, Date createdDate, Date expiredDate) {
        this.naturalKey = naturalKey;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.percent = percent;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
    }

    public void setValue(String key, String value) {
        try {
            if (key.equalsIgnoreCase("1")) {
                this.name = value;
            } else if (key.equalsIgnoreCase("2")) {
                this.price = Double.parseDouble(value);
            } else if (key.equalsIgnoreCase("3")) {
                this.percent = Double.parseDouble(value);
            } else if (key.equalsIgnoreCase("4")) {
                this.category = value;
            } else if (key.equalsIgnoreCase("5")) {
                this.unit = value;
            }
        } catch (Exception e) {
            LoggerUtil.getInstance(Commodity.class).error(e);
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return naturalKey + "," + name + "," + price + "," + percent + "," + category + "," + unit + "," + dateFormat.format(expiredDate) + "," + dateFormat.format(createdDate);
    }

    public String print() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return generateNaturalKey() + "," + name + "," + price + "," + percent + "," + category + "," + unit + "," + dateFormat.format(createdDate);
    }

    public String generateNaturalKey() {
        return SlugGenerator.toSlug(category + " " + name);
    }

    public String getNaturalKey() {
        return naturalKey;
    }

    public void setNaturalKey(String naturalKey) {
        this.naturalKey = naturalKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
