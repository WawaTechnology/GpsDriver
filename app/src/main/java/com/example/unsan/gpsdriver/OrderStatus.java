package com.example.unsan.gpsdriver;

/**
 * Created by Unsan on 30/5/18.
 */

public class OrderStatus {
    String engName;
    String status;
    int order;

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public OrderStatus() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderStatus(String engName, String status, int order) {
        this.engName = engName;
        this.status = status;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
