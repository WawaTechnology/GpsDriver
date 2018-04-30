package com.example.unsan.gpsdriver;

import java.io.Serializable;

/**
 * Created by Unsan on 12/4/18.
 */

public class Customer implements Serializable {

    String contactPerson;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getZip() {
        return zip;
    }

    public void setZip(Object zip) {
        this.zip = zip;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(long contactNumber) {
        this.contactNumber = contactNumber;
    }

    Object zip;
    String city;
    public Customer()
    {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String address;
    long contactNumber;




    public Customer(String Address,String City,long ContactNumber,String ContactPerson,Object Zip)
    {
        this.address=Address;
        this.city=City;
        this.contactNumber=ContactNumber;
        this.contactPerson=ContactPerson;
        this.zip=Zip;

    }


}
