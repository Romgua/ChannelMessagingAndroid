package romain.guarnotta.channelmessaging.Activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import romain.guarnotta.channelmessaging.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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
        Double dLatitude  = Double.valueOf(getIntent().getStringExtra("latitude"));
        Double dLongitude = Double.valueOf(getIntent().getStringExtra("longitude"));
        String sUsername  = getIntent().getStringExtra("username");
        String sMessage   = getIntent().getStringExtra("message");

        // Add a marker to location and move the camera
        setMarker(dLatitude, dLongitude, sUsername, sMessage);
    }

    private void setMarker(Double dLatitude, Double dLongitude, String sUsername, String sMessage) {
        LatLng position = new LatLng(dLatitude, dLongitude);
        // Add marker
        mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(sUsername)
                        .snippet(sMessage)
        );
        // Move camera to the position and zoom 14
        CameraPosition cameraPosition = new CameraPosition(position, 14, 0, 0);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


}
