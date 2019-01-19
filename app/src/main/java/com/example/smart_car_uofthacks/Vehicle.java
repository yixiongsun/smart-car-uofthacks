package com.example.smart_car_uofthacks;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private String id;
    private String VIN;
    private String name;
    private String make;
    private String model;
    private int year;
    private double odometer;
    private String access_token;
    private String refresh_token;

    public Vehicle(String id, String access, String refresh) {
        this.access_token = access;
        this.id = id;
        this.refresh_token = refresh;
    }

    // setters + getters
    public String getId() {
        return this.id;
    }

    public void setAccess_token(String access) {
        this.access_token = access;
    }

    public void setRefresh_token(String refresh) {
        this.refresh_token = refresh;
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }



}
