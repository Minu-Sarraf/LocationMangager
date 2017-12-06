package com.example.leapfrog.locationmanager;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import pub.devrel.easypermissions.EasyPermissions;

public class LocationUtils implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private final Context context;
    private final LocInterface locInterface;

    public LocationUtils(Context context) {
        this.context = context;
        this.locInterface = (LocInterface) context;
    }

    public void callForLocation() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

            googleApiClient.connect();
    }

    private boolean checkPermission() {
        if (EasyPermissions.hasPermissions(context, Constants.PERMISSIONS_REQ)) {
            return true;
        } else {
            EasyPermissions.requestPermissions((Activity)context,"Allow application to access Location?", Constants.LOCATION_CODE, Constants.PERMISSIONS_REQ);
           return false;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (checkPermission()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } else {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                locInterface.getLocation(currentLatitude, currentLongitude);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult((Activity) context, Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "LocationUtils services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        locInterface.getLocation(currentLatitude, currentLongitude);
    }

}
