package romain.guarnotta.channelmessaging.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
/**
 * Created by romain on 08/03/16.
 */
public class GPSActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mIsUpdateLocation = false;
    protected static Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //GoogleApiClient.OnConnectionFailedListener
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    //this
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    //this
    private void updateLocation() {
        checkPermission();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mIsUpdateLocation = true;
        }
    }

    //this
    private void removeLocation() {
        checkPermission();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mIsUpdateLocation = false;
        }
    }

    //GoogleApiClient.ConnectionCallbacks
    @Override
    public void onConnected(Bundle bundle) {
        checkPermission();
        if (mGoogleApiClient.isConnected()) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            mLocationRequest = new LocationRequest();
            //Correspond à l’intervalle moyen de temps entre chaque mise à jour des coordonnées
            mLocationRequest.setInterval(10000);
            //Correspond à l’intervalle le plus rapide entre chaque mise à jour des coordonnées
            mLocationRequest.setFastestInterval(5000);
            //Définit la demande de mise à jour avec un niveau de précision maximal
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            updateLocation();
        }
    }

    //GoogleApiClient.ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "connection suspended", Toast.LENGTH_LONG).show();
    }

    //AppCompactActivity
    @Override
    protected void onStop() {
        super.onStop();
        removeLocation();
    }

    //LocationListener
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    //FragmentActivity
    @Override
    protected void onResume() {
        super.onResume();
        updateLocation();
    }

    public static Location getCurrentLocation() {
        Location loc = null;
        if (null != mCurrentLocation) {
            loc = mCurrentLocation;
//            double lat = mCurrentLocation.getLatitude();
//            double lng = mCurrentLocation.getLongitude();
//            Toast.makeText(this, ("Latitude  = "+lat+" | Longitude = "+lng), Toast.LENGTH_LONG).show();
        }


        return loc;
    }
}
