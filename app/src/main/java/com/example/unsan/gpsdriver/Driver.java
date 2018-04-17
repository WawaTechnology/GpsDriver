package com.example.unsan.gpsdriver;

/**
 * Created by Unsan on 16/4/18.
 */

public class Driver {
    String email;
    String password;
    public Driver()
    {

    }

    public Driver(String email, String password, long phone) {
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    long phone;

}
