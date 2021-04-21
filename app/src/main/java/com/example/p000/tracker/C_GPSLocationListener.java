package com.example.p000.tracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class C_GPSLocationListener implements LocationListener
{

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}
