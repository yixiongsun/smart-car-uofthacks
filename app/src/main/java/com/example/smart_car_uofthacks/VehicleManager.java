package com.example.smart_car_uofthacks;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleManager {

    // improvements -> create a vehicle class
    private ArrayList<Vehicle> vehicles;
    private Vehicle current;

    // Constructor for the Vehicle manager class
    // Initializes vehicles array and loads the data from storage
    public VehicleManager() {

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
}
