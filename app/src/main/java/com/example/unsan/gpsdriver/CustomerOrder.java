package com.example.unsan.gpsdriver;

/**
 * Created by Unsan on 30/5/18.
 */

public class CustomerOrder {
    String customerChinese;
    OrderStatus orderStatus;

    public String getCustomerChinese() {
        return customerChinese;
    }

    public void setCustomerChinese(String customerChinese) {
        this.customerChinese = customerChinese;
    }

    public CustomerOrder() {
    }

    public CustomerOrder(String customerChinese, OrderStatus orderStatus) {
        this.customerChinese = customerChinese;
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
