package com.example.unsan.gpsdriver;

/**
 * Created by Unsan on 2/5/18.
 */

public class TodayDriverObject {
    String customerName;
    String image;
    String time;

    public String getCustomerName() {
        return customerName;
    }


    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getImage() {
        return image;
    }

    public TodayDriverObject(String customerName, String image, String time) {
        this.customerName = customerName;
        this.image = image;
        this.time = time;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
