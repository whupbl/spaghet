package com.oi.spaghet1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Выбор нового блюда на карте

public class SelectLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
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
        LatLng krsk = new LatLng(56.009390, 92.853491);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(krsk, 10));

        JSONObject j = new JSONObject();
        try {
            j.put("ID", 1);
            j.put("Phone", "+79589");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions()
                .title("Красноярск")
                .snippet("Ня-ня-ня")
                .position(krsk)).setTag(j);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AlertDialog.Builder ad;
                Context context = SelectLocationActivity.this;
                String title = marker.getTitle();
                String message = marker.getSnippet();

                ad = new AlertDialog.Builder(context);
                ad.setTitle(title);
                ad.setMessage(message);

                ad.setCancelable(true);

                ad.show();
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                AlertDialog.Builder ad;
                Context context = SelectLocationActivity.this;
                String title = "Внимание";
                String message = "Ваше блюдо можно забрать на " + latLng.latitude + " " + latLng.longitude + "\nВсё верно?";

                ad = new AlertDialog.Builder(context);
                ad.setTitle(title);
                ad.setMessage(message);

                ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        SelectLocationActivity.this.setResult(1);

                        Intent intent = new Intent();

                        intent.putExtra("latitude", String.valueOf(latLng.latitude));
                        intent.putExtra("longitude", String.valueOf(latLng.longitude));

                        intent.putExtra("requestCode", 1);
                        setResult(RESULT_OK, intent);

                        finish();
                    }
                });
                ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });

                ad.setCancelable(true);

                ad.show();
            }
        });
    }
}
