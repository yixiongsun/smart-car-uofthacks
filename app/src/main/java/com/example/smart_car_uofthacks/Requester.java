package com.example.smart_car_uofthacks;

import android.util.Log;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

interface Listener {
    void onEvent(String out);
}

public class Requester{
   // public String urlly = "http://smart-car-uofthacks.azurewebsites.net";
    static OkHttpClient client = new OkHttpClient();
    static Response response;
    static String answer;
    static Listener event;
   static String urlInfo(final String url){
        doSomething(url);

        return answer;
    }

   static void setListener(Listener listener) {
       event = listener;
   }

   static void doSomething(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    answer = running(url);
                    //ReturnAnswer(answer);
                    if (event != null) {
                        event.onEvent(answer);
                    }
                }
                catch (Exception e){
                    answer = null;
                }
            }
        }).start();
    }

    static String running(String url) throws Exception{
        Request request = new Request.Builder().url(url).build();
            response = client.newCall(request).execute();
            return response.body().string();

    }

    static void continuedRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // send request to exchange the auth code for the access token
                Request exchangeRequest = new Request.Builder()

                        .url(url)
                        .build();

                try {
                    client.newCall(exchangeRequest).execute();

                } catch (IOException e) {
                }
            }
        }).start();
    }

}
