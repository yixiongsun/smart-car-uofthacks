package com.example.smart_car_uofthacks;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

public class IntentManager {
    Context context;
    public IntentManager(Context c) { context = c;}
    public void openMaps(double lat, double lon) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("coords", new LatLng(lat, lon));
        context.startActivity(intent);
    }
    public void openWebView(String out) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", out);
        context.startActivity(intent);
    }

}
