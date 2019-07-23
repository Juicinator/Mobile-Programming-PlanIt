package com.example.planit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mClient = new GoogleApiClient() {
        @Override
        public boolean hasConnectedApi(@NonNull Api<?> api) {
            return false;
        }

        @NonNull
        @Override
        public ConnectionResult getConnectionResult(@NonNull Api<?> api) {
            return null;
        }

        @Override
        public void connect() {

        }

        @Override
        public ConnectionResult blockingConnect() {
            return null;
        }

        @Override
        public ConnectionResult blockingConnect(long l, @NonNull TimeUnit timeUnit) {
            return null;
        }

        @Override
        public void disconnect() {

        }

        @Override
        public void reconnect() {

        }

        @Override
        public PendingResult<Status> clearDefaultAccountAndReconnect() {
            return null;
        }

        @Override
        public void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {

        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public boolean isConnecting() {
            return false;
        }

        @Override
        public void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

        }

        @Override
        public boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks) {
            return false;
        }

        @Override
        public void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

        }

        @Override
        public void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

        }

        @Override
        public boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
            return false;
        }

        @Override
        public void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

        }

        @Override
        public void dump(String s, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strings) {

        }
    };
    private FusedLocationProviderClient client;
    private double latitude, longitude;
    LocationListener locationListener;
    Criteria criteria = new Criteria();
    String bestProvider;

    LocationManager mLocationManager;
    Location l;
    private int ACCESS_LOCATION_CODE = 1;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public synchronized void onLocationChanged(Location l) {
            l = getLastKnownLocation();
            latitude = l.getLatitude();
            longitude = l.getLongitude();
            LatLng x = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(x).title("Current Location"));

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //check permissions
            return;
        }
        mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        latitude = 33.129368;
        longitude = -117.159179;

        //THIS WORKS HERE. GETS GPS COORDINATES FROM THE GPS_PROVIDER ON THE PHONE AND SETS IT = TO LOCATION 'l'
        //IF THE GPS IS OFF IT WILL DEFAULT TO THE LOCATION OF THE SCHOOL
        l = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(l != null) {
            //this checks if the location is null, if not it comes into this section and sets latitude/longitude
            //but it never reaches here it gets skipped
            latitude = l.getLatitude();
            longitude = l.getLongitude();

        }

        LatLng x = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(x).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(x, 10F));


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                Location l = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return l;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_CODE);
            }
            bestProvider = mLocationManager.getBestProvider(criteria, true);

            Location l = mLocationManager.getLastKnownLocation(bestProvider);

            if (l == null) {
                Log.d("null", "nullllll");
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }


}
