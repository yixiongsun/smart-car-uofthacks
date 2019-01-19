package com.example.smart_car_uofthacks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleManager {

    // improvements -> create a vehicle class
    private ArrayList<Vehicle> vehicles;
    private Vehicle current;
    Context context;

    // Constructor for the Vehicle manager class
    // Initializes vehicles array and loads the data from storage
    public VehicleManager(Context context) {
        this.context = context;
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
        openAuthLink();


        return true;
    }

    private void openAuthLink() {
        Requester.setListener(new Listener() {
            @Override
            public void onEvent(String out) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", out);
                context.startActivity(intent);
            }
        });
        Requester.urlInfo(ParseInput.makeUrl("/login", new HashMap<String, String>()));
    }

    public void exchangeAuth(String url) {
        Requester.setListener(new Listener() {
            @Override
            public void onEvent(String out) {
                // we get stuffs
                try {
                    JSONObject jsonObject = new JSONObject(out);
                    addVehicle(jsonObject);
                } catch (Exception e) {
                    Log.d("JSON Error", e.toString());
                }

            }
        });
        Requester.urlInfo(url);
    }

    private void addVehicle(JSONObject jsonObject) {
        try {
            final String access = jsonObject.getString("access_token");
            final String refresh = jsonObject.getString("refresh_token");
            String route = "/vehicle/vehicles";
            Requester.setListener(new Listener() {
                @Override
                public void onEvent(String out) {
                    // we get stuffs
                    try {
                        JSONObject jsonObject = new JSONObject(out);
                        JSONArray vehicleIds = jsonObject.getJSONArray("vehicles");
                        for (int i = 0; i < vehicleIds.length(); i++) {
                            String id = vehicleIds.getString(i);
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

                    } catch (Exception e) {
                        Log.d("JSON Error", e.toString());
                    }

                }
            });
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("token", access);
            Requester.urlInfo(ParseInput.makeUrl(route, new HashMap<String, String>()));
        } catch (Exception  e){
            Log.d("Error", e.toString());
        }


    }



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
