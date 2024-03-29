package me.chrislane.weather.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import me.chrislane.weather.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapLongClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        mMap.setOnMapLongClickListener(this);

        // Add a marker in current location and move the camera
        double latitude = getIntent().getExtras().getDouble("latitude");
        double longitude = getIntent().getExtras().getDouble("longitude");
        LatLng current = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(current).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

        Toast.makeText(this, "Long press a location", 8).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("latitude", latLng.latitude);
        resultIntent.putExtra("longitude", latLng.longitude);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
