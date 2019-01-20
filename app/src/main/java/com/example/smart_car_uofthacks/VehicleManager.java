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

interface VehicleListener {
    void onRefreshEvent();
    void onLocationEvent(double lat, double lon);
    void onOdometerEvent(double odometer);
}


public class VehicleManager implements Serializable {

    // improvements -> create a vehicle class
    ArrayList<Vehicle> vehicles;
    private Vehicle current;
    Context context;
    IntentManager intentManager;
    VehicleListener listener;
    private String rAccess;
    private String rRefresh;
    private final String androidId;

    // Constructor for the Vehicle manager class
    // Initializes vehicles array and loads the data from storage
    public VehicleManager(Context context, String androidId) {
        this.context = context;
        this.androidId = androidId;
        loadAll();
        if (vehicles == null) {
            vehicles = new ArrayList<Vehicle>();

        }

        intentManager = new IntentManager(context);
        Requester.setListener(new Listener() {
            @Override
            public void onRefreshEvent(String tokens) {
                // refresh token
                if (tokens == null) {
                    refreshToken();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(tokens);
                        rAccess = jsonObject.getString("accessToken");
                        rRefresh = jsonObject.getString("refreshToken");
                        updateTokens();
                    } catch (Exception e) {
                        Log.d("JSON Error", e.toString());

                    }
                }
            }

            @Override
            public void onAuthEvent(String out) {
                intentManager.openWebView(out);
            }

            @Override
            public void onExchangeEvent(String out) {
                try {
                    JSONObject jsonObject = new JSONObject(out);
                    addVehicle(jsonObject);
                } catch (Exception e) {
                    Log.d("JSON Error", e.toString());
                }
            }

            @Override
            public void onVehicleEvent(String out) {
                saveNewVehicles(out, rAccess, rRefresh);
            }

            @Override
            public void onOdometerEvent(String out) {
                saveOdometer(out);
            }

            @Override
            public void onLocationEvent(String out) {
                saveLocation(out);
            }
        });
    }

    private void updateTokens() {
        if (rRefresh == null) return;
        for (Vehicle v: vehicles) {
            if (v.getRefresh_token().equals(rRefresh)) {
                v.setAccess_token(rAccess);
            }
        }
        rAccess = null;
        rRefresh = null;
    }


    private void refreshToken() {
        Requester.urlInfo(ParseInput.makeUrl("/login", new HashMap<String, String>()), Req.REFRESH);
    }


    public boolean hasVehicles() {
        if (vehicles.isEmpty()) {
            return false;
        }

        return true;
    }


    public void setListener(VehicleListener listener) {
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
        Requester.urlInfo(ParseInput.makeUrl("/login", new HashMap<String, String>()), Req.AUTH);
    }

    public void exchangeAuth(String url) {
        Requester.urlInfo(url, Req.EXCHANGE);
    }

    private void addVehicle(JSONObject jsonObject) {
        try {
            rAccess = jsonObject.getString("accessToken");
            rRefresh = jsonObject.getString("refreshToken");
            String route = "/vehicle/vehicles";
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("token", rAccess);
            parameters.put("appId", androidId);
            Requester.urlInfo(ParseInput.makeUrl(route, parameters), Req.VEHICLE);
        } catch (Exception  e){
            Log.d("Error", e.toString());
        }


    }

    private void saveNewVehicles(String vehicles, String access, String refresh) {
        // we get stuffs
        try {
            JSONArray vehicleIds = new JSONArray(vehicles);
            Vehicle cVehicle = null;

            for (int i = 0; i < vehicleIds.length(); i++) {
                JSONObject jsonObject = vehicleIds.getJSONObject(i);
                String id = jsonObject.getString("id");
                String make = jsonObject.getString("make");
                String model = jsonObject.getString("model");
                int year = jsonObject.getInt("year");
                boolean found = false;
                for (Vehicle vehicle: this.vehicles ) {

                    // if vehicle already exists, update the values
                    if (vehicle.getId().equals(id)) {
                        vehicle.setAccess_token(access);
                        vehicle.setRefresh_token(refresh);
                        vehicle.setMake(make);
                        vehicle.setModel(model);
                        vehicle.setYear(year);
                        vehicle.setName();
                        found = true;
                        if (cVehicle == null) {
                            cVehicle = vehicle;
                        }
                        break;
                    }
                }
                if (!found) {
                    Vehicle nVehicle = new Vehicle(id, access, refresh, model, make, year);
                    if (cVehicle == null) {
                        cVehicle = nVehicle;
                    }
                    this.vehicles.add(nVehicle);
                }

            }

            if (cVehicle != null) {
                current = cVehicle;
            }

            if (listener != null) {
                listener.onRefreshEvent();
            }
            saveAll();
            rAccess = null;
            rRefresh = null;
        } catch (Exception e) {
            Log.d("JSON Error", e.toString());
        }
    }




    // index based
    public void setCurrentVehicle(int index) {
        current = vehicles.get(index);
    }

    // vehicle based
    public void setCurrentVehicle(Vehicle vehicle) {
        current = vehicle;
    }

    public Vehicle getCurrentVehicle() {
        return current;
    }

    public int getCurrentVehicleIndex() {
        if (current == null) {
            current = vehicles.get(0);
        }
        for (int i = 0; i < this.vehicles.size(); i++) {
            if (current.equals(vehicles.get(i))) {
                return i;
            }
        }
        return 0;
    }


    public void locationVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();
        String route = "/vehicle/location";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);
        parameters.put("appId", androidId);

        Requester.urlInfo(ParseInput.makeUrl(route, parameters), Req.LOCATION);

        // request with this token
    }

    private void saveLocation(String location) {
        try {
            JSONObject jsonObject = new JSONObject(location);
            double latitude = jsonObject.getDouble("latitude");
            double longitude = jsonObject.getDouble("longitude");
            current.setLatitude(latitude);
            current.setLongitide(longitude);
            if (listener != null) {
                listener.onLocationEvent(latitude, longitude);
            }
            saveAll();
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }


    /*
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

        Requester.urlInfo(ParseInput.makeUrl(route, parameters),);
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
    }*/

    public void vehicleOdometer() {
        String id = current.getId();
        String access = current.getAccess_token();
        String route = "/vehicle/odometer";

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);
        parameters.put("appId", androidId);

        Requester.urlInfo(ParseInput.makeUrl(route, parameters), Req.ODOMETER);
    }

    public void saveOdometer(String distance) {
        try {
            JSONObject jsonObject = new JSONObject(distance);
            double odometer = jsonObject.getDouble("distance");
            current.setOdometer(odometer);
            if (listener != null) {
                listener.onOdometerEvent(odometer);
            }
            saveAll();
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }



    public void disconnectVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();

        String route = "/vehicle/disconnect";

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);

        Requester.urlInfo(ParseInput.makeUrl(route, parameters), Req.DISCONNECT);

        vehicles.remove(getCurrentVehicleIndex());

        if (vehicles.isEmpty()) {
            current = null;
            addVehicle();
        } else {
            current = vehicles.get(0);
            if (listener != null) {
                listener.onRefreshEvent();
            }
        }
        saveAll();


    }

    public void unlockVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();

        String route = "/vehicle/unlock";

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);
        Requester.urlInfo(ParseInput.makeUrl(route, parameters), Req.UNLOCK);
    }

    public void lockVehicle() {
        String id = current.getId();
        String access = current.getAccess_token();

        String route = "/vehicle/lock";

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("token", access);
        parameters.put("vehicleId", id);
        Requester.urlInfo(ParseInput.makeUrl(route, parameters), Req.LOCK);
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
