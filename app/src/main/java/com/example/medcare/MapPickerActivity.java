package com.example.medcare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapPickerActivity extends FragmentActivity {

    private MapView map;
    private MyLocationNewOverlay mLocationOverlay;
    private Marker pickMarker;
    private Button confirmButton;
    private String selectedAddress = "";
    private GeoPoint selectedPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_map_picker);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setVisibility(View.GONE);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        setupMyLocationOverlay();

        map.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                GeoPoint tappedPoint = (GeoPoint) map.getProjection()
                        .fromPixels((int) event.getX(), (int) event.getY());

                selectedPoint = tappedPoint;
                reverseGeocodeAndSetMarker(tappedPoint);
                return true;
            }
            return false;
        });

        confirmButton.setOnClickListener(v -> {
            if (selectedPoint != null && !selectedAddress.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("location", selectedAddress);
                resultIntent.putExtra("latitude", selectedPoint.getLatitude());
                resultIntent.putExtra("longitude", selectedPoint.getLongitude());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void reverseGeocodeAndSetMarker(GeoPoint point) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                selectedAddress = addresses.get(0).getAddressLine(0);

                if (pickMarker != null) {
                    map.getOverlays().remove(pickMarker);
                }

                pickMarker = new Marker(map);
                pickMarker.setPosition(point);
                pickMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                pickMarker.setTitle(selectedAddress);

                // Set custom marker icon
                pickMarker.setIcon(getResources().getDrawable(R.drawable.location)); // replace with your icon
                map.getOverlays().add(pickMarker);
                map.invalidate();

                confirmButton.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error fetching address", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMyLocationOverlay() {
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        mLocationOverlay.enableMyLocation();

        mLocationOverlay.runOnFirstFix(() -> runOnUiThread(() -> {
            Location loc = mLocationOverlay.getLastFix();
            if (loc != null) {
                GeoPoint startPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
                map.getController().setZoom(18.0);
                map.getController().animateTo(startPoint);
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
            }
        }));

        map.getOverlays().add(mLocationOverlay);
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupMyLocationOverlay();
        }
    }
}
