package com.example.unsan.gpsdriver;

/**
 * Created by Unsan on 30/4/18.
 */

public class DriverCar {

    String carNumber;
    String carInfo;
    long phone;

    public DriverCar(String carNumber, String carInfo, long phone) {
        this.carNumber = carNumber;
        this.carInfo = carInfo;
        this.phone = phone;
    }

    public DriverCar() {
    }

    public String getCarNumber() {
        return carNumber;

    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
