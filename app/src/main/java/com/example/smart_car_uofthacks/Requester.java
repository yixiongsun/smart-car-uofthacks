package com.example.smart_car_uofthacks;

import android.util.Log;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

enum Req {
    AUTH, VEHICLE, ODOMETER, LOCATION,
    LOCK, UNLOCK, DISCONNECT, EXCHANGE, REFRESH
}

interface Listener {
    void onRefreshEvent(String out);
    void onAuthEvent(String out);
    void onExchangeEvent(String out);
    void onVehicleEvent(String out);
    void onOdometerEvent(String out);
    void onLocationEvent(String out);
}

public class Requester{
   // //"http://smart-car-uofthacks.azurewebsites.net";
    static OkHttpClient client = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
       @Override
       public boolean verify(String hostname, SSLSession session) {
           return true;
       }
   }).build();
    static Response response;
    static String answer;
    static Listener event;

   static void setListener(Listener listener) {
       event = listener;
   }

   static void urlInfo(final String url, final Req req){
       final String nUrl = url.replace("localhost:3000", "10.0.2.2:3000" );
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    answer = running(url);
                    //ReturnAnswer(answer);
                    if (event != null) {
                        if (answer == null || req == Req.REFRESH) {
                            event.onRefreshEvent(answer);
                        } else if (req == Req.AUTH) {
                            event.onAuthEvent(answer);
                        } else if (req == Req.EXCHANGE) {
                            event.onExchangeEvent(answer);
                        } else if (req == Req.VEHICLE) {
                            event.onVehicleEvent(answer);
                        } else if (req == Req.ODOMETER) {
                            event.onOdometerEvent(answer);
                        } else if (req == Req.LOCATION) {
                            event.onLocationEvent(answer);
                        }
                    }
                }
                catch (Exception e){
                    Log.e("error", e.toString());
                    answer = null;
                }
            }
        }).start();
    }


    static String running(String url) throws Exception{


        Request request = new Request.Builder().url(url).build();
            response = client.newCall(request).execute();
            if (response.code() == 401) {
                return null;
            }
            return response.body().string();

    }

}
