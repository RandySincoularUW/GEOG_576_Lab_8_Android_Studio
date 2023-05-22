package com.example.mobile_empty_views_activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.mobile_empty_views_activity.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.example.mobile_empty_views_activity.AsyncHttpRequest;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        android.util.Log.v("INFO", "*** MapsActivity.java. onCreate()");

        System.out.println("*** MapsActivity.java: onCreate(), calling MapFilters() ....  **");

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        android.util.Log.v("INFO", "*** MapsActivity.java. onMapReady()");

        //* Comment out this default code
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        System.out.println("*** MapsActivity.java: onMapReady()  **++++++++++++++++++++++++++++++");

        AsyncHttpRequest asyncHttpRequest = new AsyncHttpRequest();

        asyncHttpRequest.setGoogleMap(googleMap);

        System.out.println("*** MapsActivity.java: onMapReady() executing asyncHttpRequest ...  **");

        // ***************************************************
        // 13-Nov-20 Add for troubleshooting
        // *Note: This try/catch does not tell us if the case or spelling of the URL is incorrect!!!!
        // ***************************************************
        try {

            // This URL must match the AWS Elastic IP Address of the EC2 Instance
            asyncHttpRequest.execute("http://34.199.78.131:8000/flights/msn");

            System.out.println("*** MapsActivity.java: onMapReady() ...  execute completed...  **");

        } catch (Exception e) {
            System.out.println("MapsActivity.java: onMapReady() **Error running asyncHttpPost.execute ...  **");

            e.printStackTrace();
        }

    }  // end onMapReady()
}