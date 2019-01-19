package com.example.smart_car_uofthacks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


import android.content.Context;

public class VehicleManager implements Serializable {

    // improvements -> create a vehicle class
    ArrayList<Vehicle> vehicles;
    private Vehicle current;
    Context context;

    // Constructor for the Vehicle manager class
    // Initializes vehicles array and loads the data from storage
    public VehicleManager(Context context) {
        this.context = context;
        vehicles = new ArrayList<Vehicle>();
    }

    // Getter for vehicles -> eventually be improved to make a copy/clone
    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    // Adds a vehicle to the arraylist
    // 1. Make a new auth request
    // 2. Save the access and refresh token
    // 3. Request vehicle ids and add each vehicle as an individual HashMap entry
    // If vehicle already exists, simply update the existing one
    public boolean addVehicle() {
        // Request auth



        String access = "";
        String refresh = "";
        String route = "/vehicle/vehicles";


        // Request vehicles



        ArrayList<String> vehicleIds = new ArrayList<String>();
        for (String id: vehicleIds) {
            boolean found = false;
            for (Vehicle vehicle: vehicles ) {
                // if vehicle already exists, update the values
                if (vehicle.getId().equals(id)) {
                    vehicle.setAccess_token(access);
                    vehicle.setRefresh_token(refresh);
                    found = true;
                    break;
                }
            }
            if (!found) {
                vehicles.add(new Vehicle(id, access, refresh));
            }

        }


        return true;
    }

    private void openAuthLink() {}



    //
    public void setCurrentVehicle(int index) {
        current = vehicles.get(index);
    }

    public void setCurrentVehicle(Vehicle vehicle) {
        current = vehicle;
    }

    public Vehicle getCurrentVehicle() {
        return current;
    }


    public void aboutVehicle() {
        String id = current.getId();
        String access_token = current.getAccess_token();

        // request with this token




    }

    public void locationVehicle() {}

    public void disconnectVehicle() {}

    public void unlockVehicle() {}

    public void lockVehicle() {}

    public void saveAll(){
        SavingData.saveToFile(SavingData.CURRENT_VEHICLE, this.context, current);
        SavingData.saveToFile(SavingData.VEHICLE_LIST, this.context, vehicles);
    }

    public void loadAll(){
        vehicles = SavingData.loadFromFile(SavingData.VEHICLE_LIST, this.context);
        current = SavingData.loadFromFile(SavingData.CURRENT_VEHICLE, this.context);
    }

    public String toString(Vehicle vehicle){
        return vehicle.getId();
    }

}
