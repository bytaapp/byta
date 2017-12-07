package ml.byta.byta.Tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Fermin on 03/11/2017.
 */

public class GetLocation {



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static String coords;

    public static String getCoords(Activity activity) {


        verifyStoragePermissions(activity);

        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);


        try {

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location==null) {
                Toast.makeText(activity, "Por favor, activa la ubicación", Toast.LENGTH_LONG).show();
                activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                return null;
            }

            Double longitude = location.getLongitude();
            Double latitude = location.getLatitude();

            coords = latitude+", "+longitude;

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return coords;

    }

    private static final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            try {
                Double longitude = location.getLongitude();
                Double latitude = location.getLatitude();
                coords = longitude+", "+latitude;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("ubicacion deshabilitada");
        }
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}

