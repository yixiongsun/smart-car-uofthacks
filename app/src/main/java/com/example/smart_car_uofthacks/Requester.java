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
   // //"http://smart-car-uofthacks.azurewebsites.net";
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
       final String nUrl = url.replace("localhost:3000", "10.0.2.2:3000" );
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    answer = running(nUrl);
                    //ReturnAnswer(answer);
                    if (event != null) {
                        event.onEvent(answer);
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
            return response.body().string();

    }

}
