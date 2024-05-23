package co.edu.unipiloto.transporteapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class ActivityRuta extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        geocoder = new Geocoder(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Obtenemos las coordenadas de Bogotá y Cúcuta
        LatLng inicio = getLocationFromAddress("Bogotá");
        LatLng destino = getLocationFromAddress("Cúcuta");

        if (inicio != null && destino != null) {
            // Agregamos marcadores en Bogotá y Cúcuta
            mMap.addMarker(new MarkerOptions().position(inicio).title("Bogotá"));
            mMap.addMarker(new MarkerOptions().position(destino).title("Cúcuta"));

            // Movemos la cámara para mostrar ambas ubicaciones
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio, 6));

            // Trazamos la ruta entre Bogotá y Cúcuta
            int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(inicio, destino)
                    .width(5)
                    .color(colorPrimary); // Utilizamos el color obtenido de los recursos
            Polyline polyline = mMap.addPolyline(polylineOptions);
        } else {
            Toast.makeText(this, "No se pudo encontrar alguna ubicación", Toast.LENGTH_SHORT).show();
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(strAddress, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


