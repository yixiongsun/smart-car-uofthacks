package com.example.smart_car_uofthacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    VehicleManager vehicleManager;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        vehicleManager = new VehicleManager(this);
        vehicleManager.setListener(new Listener() {
            @Override
            public void onEvent(String out) {
                menuSetup();
                refreshOdometer();
                vehicleLoad();
            }
        });

        if (!vehicleManager.hasVehicles()) {
            vehicleManager.addVehicle();
        } else {
            menuSetup();
            vehicleLoad();
            refreshOdometer();
        }


    }

    private void menuSetup() {
        // Array list of maps that contain data about the vehicle
        ArrayList<Vehicle> vehicles = vehicleManager.getVehicles();

        // Get reference to menu and add objects from the list in the order of the list
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < vehicles.size(); i++) {
            menu.add(0, i, 0, vehicles.get(i).getName());
        }
        if (!vehicles.isEmpty()) {
            menu.getItem(vehicleManager.getCurrentVehicleIndex()).setChecked(true);
        }

    }

    // load odometer
    private void refreshOdometer() {

    }

    // load static vehicle data
    private void vehicleLoad() {
        Vehicle current = vehicleManager.getCurrentVehicle();
        String name = current.getName();
        String id = current.getId();
        String vin = current.getVIN();

        // SET UP LABELS!!!
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        // id is
        int id = item.getItemId();
        vehicleManager.setCurrentVehicle(id);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void disconnectVehicle(View view){

        vehicleManager.disconnectVehicle();
    }

    public void lockVehicle(View view) {
        vehicleManager.lockVehicle();
    }

    public void unlockVehicle(View view) {
        vehicleManager.unlockVehicle();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String url = data.getStringExtra("exchange");
            if (url != null) {
                vehicleManager.exchangeAuth(url);
            }

        }
    }


}
