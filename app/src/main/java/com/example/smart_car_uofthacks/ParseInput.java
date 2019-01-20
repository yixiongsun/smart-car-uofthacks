package com.example.smart_car_uofthacks;

import java.util.HashMap;


public class ParseInput {

    public static final String URL= "https://smartcarconnect.net";
            //"http://smart-car-uofthacks.azurewebsites.net";
    //url is http://smart-car-uofthacks.azurewebsites.net
    //ending is /login
    //map key is key1, map value is 1
    public static String makeUrl(String ending, HashMap<String, String> map){
        StringBuilder ret = new StringBuilder(URL).append(ending);
        ret.append("?");
        for (String key : map.keySet()){
            String value = map.get(key);
            ret.append(key + "=" + value + "&");

        }
        ret.setLength(ret.length() -1);
        return ret.toString();
    }
}
