package com.utkusenocak.travelbook;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Geocoder geocoder;
    SQLiteDatabase database;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        intent = getIntent();
        try {
            database = this.openOrCreateDatabase("Locations", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS locations (address VARCHAR, latitude DOUBLE, longitude DOUBLE)");
        } catch (Exception e){
            e.printStackTrace();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };



        //Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            intent = getIntent();
            String info = intent.getStringExtra("info");


            if (info.matches("newLocation")){
                mMap.setOnMapLongClickListener(this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location userLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LatLng userLastKnownLatLong = new LatLng(userLastKnownLocation.getLatitude(), userLastKnownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastKnownLatLong, 15));
            }else{

                String userSavedAddress = intent.getStringExtra("address");
                Double latitude = intent.getDoubleExtra("lat", 0);
                Double longitude = intent.getDoubleExtra("long", 0);
                LatLng markerLatLong = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().title(userSavedAddress).position(markerLatLong));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLong, 15));

            }

        }


    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);


            if (addressList != null && addressList.size() > 0){
                if (addressList.get(0).getThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare();

                    if (addressList.get(0).getSubThoroughfare() != null){
                        address += addressList.get(0).getSubThoroughfare();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (!address.matches("")){
            mMap.addMarker(new MarkerOptions().title(address).position(latLng));
            try {
                String query = "INSERT INTO locations (address, latitude, longitude) VALUES (?, ?, ?)";
                SQLiteStatement statement = database.compileStatement(query);
                statement.bindString(1, address);
                statement.bindDouble(2, latLng.latitude);
                statement.bindDouble(3, latLng.longitude);
                statement.execute();
                Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0){
            if (requestCode == 1){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location userLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng userLastKnownLatLong = new LatLng(userLastKnownLocation.getLatitude(), userLastKnownLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastKnownLatLong, 15));
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
