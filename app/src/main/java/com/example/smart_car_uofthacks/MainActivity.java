package com.example.smart_car_uofthacks;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    VehicleManager vehicleManager;
    NavigationView navigationView;
    GoogleMap mMap;
    LatLng latLng;
    TextView odometerView;
    TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        odometerView = (TextView) findViewById(R.id.tv_odometer);
        nameView = (TextView) findViewById(R.id.tv_vehicle_info);

        vehicleManager = new VehicleManager(this, androidId);
        vehicleManager.setListener(new VehicleListener() {
            @Override
            public void onRefreshEvent() {
                setNameView();
                menuSetup();
                refreshOdometer();
                //vehicleLoad();
                refreshMaps();
            }

            @Override
            public void onLocationEvent(double lat, double lon) {
                latLng = new LatLng(lat, lon);
                setUpMap(mMap);
            }

            @Override
            public void onOdometerEvent(double odometer) {
                int km = (int) odometer;
                setOdometerView(km);
            }
        });

        if (!vehicleManager.hasVehicles()) {
            addVehicle();
        } else {
            setNameView();
            menuSetup();
            //vehicleLoad();
            refreshOdometer();
            refreshMaps();
        }

        SupportMapFragment supportMapFragment = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setUpMap(googleMap);
            }
        });

    }


    private void addVehicle() {
        vehicleManager.addVehicle();
    }


    private void setUpMap(GoogleMap googleMap) {
        if (googleMap == null) {
            return;
        }
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (latLng != null) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(vehicleManager.getCurrentVehicle().getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );                }
            });


        }

    }

    private void menuSetup() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Array list of maps that contain data about the vehicle
                ArrayList<Vehicle> vehicles = vehicleManager.getVehicles();

                // Get reference to menu and add objects from the list in the order of the list
                Menu menu = navigationView.getMenu();
                menu.clear();
                for (int i = 0; i < vehicles.size(); i++) {
                    menu.add(0, i, 0, vehicles.get(i).getName());
                }
                menu.add(0, -1, 0, "Add new Car");
                if (!vehicles.isEmpty()) {
                    menu.getItem(vehicleManager.getCurrentVehicleIndex()).setChecked(true);
                }
            }
        });

    }

    private void refreshMaps() {
        vehicleManager.locationVehicle();
    }

    public void locationClick(View view) {
        refreshMaps();
    }

    public void odometerClick(View view) {
        refreshOdometer();
    }



    private void setOdometerView(final int km) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                odometerView.setText(Integer.toString(km) + " KM");
            }
        });
    }


    private void setNameView() {
        final String name = vehicleManager.getCurrentVehicle().getName();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameView.setText(name);
            }
        });
    }


    // load odometer
    private void refreshOdometer() {
        vehicleManager.vehicleOdometer();
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

            // disconnect
            vehicleManager.disconnectVehicle();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        // id is
        int id = item.getItemId();
        if (id == -1) {
            addVehicle();
            return true;
        }
        navigationView.getMenu().getItem(id).setChecked(true);
        vehicleManager.setCurrentVehicle(id);
        setNameView();

        //vehicleLoad();
        refreshOdometer();
        refreshMaps();


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
