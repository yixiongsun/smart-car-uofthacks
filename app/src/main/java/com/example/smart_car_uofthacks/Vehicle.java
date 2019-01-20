package com.example.smart_car_uofthacks;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private String id;
    private String VIN;
    private String name;
    private String make;
    private String model;
    private int year;

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    private double odometer;
    private double latitude;
    private double longitide;
    private String access_token;
    private String refresh_token;

    public Vehicle(String id, String access, String refresh, String model, String make, int year) {
        this.access_token = access;
        this.id = id;
        this.refresh_token = refresh;
        this.model = model;
        this.make = make;
        this.year = year;
        this.setName();
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

    public void setLatitude(double l) { latitude = l;}
    public void setLongitide(double l) { longitide = l;}

    public void setName() {
        if (make == null || model == null || year == 0) {
            return;
        }
        name = year + " " + make + " " + model;
    }


    public String getVIN() {
        return VIN;
    }

    public String getName() {
        return name;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }


    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }


    @Override
    public boolean equals(Object b) {
        if (b == this) return true;
        if (!(b instanceof Vehicle)) return false;
        if (this.id.equals(((Vehicle)b).getId())) {
            return true;
        }
        return false;
    }

}
