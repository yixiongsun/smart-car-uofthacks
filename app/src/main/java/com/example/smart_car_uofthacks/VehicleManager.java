package com.example.smart_car_uofthacks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


import android.content.Context;

public class VehicleManager implements Serializable {

    // improvements -> create a vehicle class
    ArrayList<Vehicle> vehicles;
    private Vehicle current;
    Context context;
    IntentManager intentManager;
    Listener listener;

    // Constructor for the Vehicle manager class
    // Initializes vehicles array and loads the data from storage
    public VehicleManager(Context context) {
        this.context = context;

        loadAll();
        if (vehicles == null) {
            vehicles = new ArrayList<Vehicle>();

        }

        intentManager = new IntentManager(context);
    }

    public boolean hasVehicles() {
        if (vehicles.isEmpty()) {
            return false;
        }

        return true;
    }


    public void setListener(Listener listener) {
        this.listener = listener;
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
                /*
                if (listener != null) {
                    listener.onEvent(out);
                }*/
                intentManager.openWebView(out);
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
            final String access = jsonObject.getString("accessToken");
            final String refresh = jsonObject.getString("refreshToken");
            String route = "/vehicle/vehicles";
            Requester.setListener(new Listener() {
                @Override
                public void onEvent(String out) {
                    saveNewVehicles(out, access, refresh);

                }
            });
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("token", access);
            Requester.urlInfo(ParseInput.makeUrl(route, parameters));
        } catch (Exception  e){
            Log.d("Error", e.toString());
        }


    }

    private void saveNewVehicles(String vehicles, String access, String refresh) {
        // we get stuffs
        try {
            JSONArray vehicleIds = new JSONArray(vehicles);
            for (int i = 0; i < vehicleIds.length(); i++) {
                String id = vehicleIds.getString(i);
                boolean found = false;
                for (Vehicle vehicle: this.vehicles ) {
                    // if vehicle already exists, update the values
                    if (vehicle.getId().equals(id)) {
                        vehicle.setAccess_token(access);
                        vehicle.setRefresh_token(refresh);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    this.vehicles.add(new Vehicle(id, access, refresh));
                }

            }
            saveAll();
            if (current == null) {
                current = this.vehicles.get(0);
            }

        } catch (Exception e) {
            Log.d("JSON Error", e.toString());
        }
    }




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

    public int getCurrentVehicleIndex() {
        for (int i = 0; i < this.vehicles.size(); i++) {
            if (current.equals(vehicles)) {
                return i;
            }
        }
        return -1;
    }


    public void locationVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();
        String route = "/vehicle/location";
        Requester.setListener(new Listener() {
            @Override
            public void onEvent(String out) {
                saveLocation(out);
            }
        });
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);

        Requester.urlInfo(ParseInput.makeUrl(route, parameters));

        // request with this token




    }

    private void saveLocation(String location) {
        try {
            JSONObject jsonObject = new JSONObject(location);
            double latitude = jsonObject.getDouble("latitude");
            double longitude = jsonObject.getDouble("longitude");
            current.setLatitude(latitude);
            current.setLongitide(longitude);
            intentManager.openMaps(latitude, longitude);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }


    public void aboutVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();
        String route = "/vehicle/info";
        Requester.setListener(new Listener() {
            @Override
            public void onEvent(String out) {
                saveInfo(out);
            }
        });
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);

        Requester.urlInfo(ParseInput.makeUrl(route, parameters));
    }

    private void saveInfo(String info){
        try{
            JSONObject jsonObject = new JSONObject(info);
            String make = jsonObject.getString("make");
            String model = jsonObject.getString("make");
            int year = jsonObject.getInt("year");
            current.setMake(make);
            current.setModel(model);
            current.setYear(year);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    public void disconnectVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();

        String route = "vehicle/disconnect";

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);

        Requester.setListener(new Listener() {
            @Override
            public void onEvent(String out) {

            }
        });

        Requester.urlInfo(ParseInput.makeUrl(route, parameters));
    }

    public void unlockVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();

        String route = "vehicle/unlock";

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);

        Requester.setListener(new Listener() {
            @Override
            public void onEvent(String out) {
            }
        });

        Requester.urlInfo(ParseInput.makeUrl(route, parameters));
    }

    public void lockVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();

        String route = "vehicle/lock";

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);

        Requester.setListener(new Listener() {
            @Override
            public void onEvent(String out) {
            }
        });

        Requester.urlInfo(ParseInput.makeUrl(route, parameters));
    }

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
