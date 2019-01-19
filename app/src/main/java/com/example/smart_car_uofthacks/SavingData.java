package com.example.smart_car_uofthacks;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/*
 * The purpose of this class is to provide methods to read and write data
 * to files.
 */
class SavingData {
    final static String VEHICLE_LIST = "vehicle_list.ser";
    final static String CURRENT_VEHICLE = "current_vehicle.ser";


    /**
     *
     * @param fileName the file where T is being loaded from
     * @param context the context in which T is being loaded
     * @param <T> the type of the objet being loaded
     * @return the object being stored in the file
     */
    static <T> T loadFromFile(String fileName, Context context) {

        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                T temp = (T) input.readObject();
                inputStream.close();
                return temp;
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        return null;
    }

    /**
     *
     * @param fileName the file the object is being saved to
     * @param context the context in which the object is being saved
     * @param object the object being saved
     * @param <T> the type of the object being saved
     */
    static <T> void  saveToFile(String fileName, Context context, T object) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(object);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}