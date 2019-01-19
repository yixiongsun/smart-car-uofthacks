package com.example.smart_car_uofthacks;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleManager {

    // improvements -> create a vehicle class
    private ArrayList<HashMap<String, String>> vehicles;
    private HashMap<String, String> current;

    // Constructor for the Vehicle manager class
    // Initializes vehicles array and loads the data from storage
    public VehicleManager() {

    }

    // Getter for vehicles -> eventually be improved to make a copy/clone
    public ArrayList<HashMap<String, String>> getVehicles() {
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

        // Request vehicles



        ArrayList<String> vehicleIds = new ArrayList<String>();
        for (String id: vehicleIds) {
            boolean found = false;
            for (HashMap vehicle: vehicles ) {
                // if vehicle already exists, update the values
                if (vehicle.get("vehicle_id").equals(id)) {
                    vehicle.put("access_token", access);
                    vehicle.put("refresh_token", refresh);
                    found = true;
                    break;
                }
            }
            if (!found) {
                HashMap<String, String> v = new HashMap<String, String>();
                v.put("vehicle_id", id);
                v.put("access_token", access);
                v.put("refresh_token", refresh);
                vehicles.add(v);
            }

        }


        return true;
    }

    //
    public void setCurrentVehicle(int index) {
        current = vehicles.get(index);
    }

    public HashMap<String, String> getCurrentVehicle() {
        return current;
    }


    public void aboutVehicle(int index) {}

    public void locationVehicle(int index) {}

    public void disconnectVehicle(int index) {}

    public void unlockVehicle(int index) {}

    public void lockVehicle(int index) {}
}
