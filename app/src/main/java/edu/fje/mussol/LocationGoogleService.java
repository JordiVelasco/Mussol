package edu.fje.mussol;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationGoogleService extends Service {
    private final String TAG = "PosicionServicio";
    private FusedLocationProviderClient miFusedLocationCliente;

    public LocationGoogleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        miFusedLocationCliente = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(20 * 1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            miFusedLocationCliente.requestLocationUpdates(locationRequest, new LocationLlamada(), null);
        }
    }

    private class LocationLlamada extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(locationResult != null){
                Log.v(TAG, "onLocationResult ejecutada");
                for( Location location : locationResult.getLocations() ){
                    Toast.makeText(getApplicationContext(), "Posicion recivida", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "iteracion de un ciclo "+location.getLatitude()+", "+location.getLongitude());
                    Intent intent = new Intent("NuevaPosicion");
                    intent.putExtra("latitud", location.getLatitude());
                    intent.putExtra("longitud", location.getLongitude());
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}