package com.example.asistmed;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.example.asistmed.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import org.json.JSONException;
import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Definimos variables

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Marker marcadorCarlosIII;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Definimos una imagen para el marcador
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.carlosiii_png); // Añadimos la imagen del nuevo marcador
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 63, 100, false);

        // Add a marker in Murcia and move the camera
        LatLng llCarlosIII = new LatLng(37.6057886, -0.9901911);
        marcadorCarlosIII = mMap.addMarker(new MarkerOptions().position(llCarlosIII).title("CIFP Carlos III").icon(BitmapDescriptorFactory
                .fromBitmap(smallMarker)));//USo el smallmarker creado con la imagen para añadirla;

        //Habilita lo botones de zoom y de navegación
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Definimos el tipo de vista del mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(llCarlosIII));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 10000, null);//Altura de zoom (1 mundo, 5 continente) y 10 segundos

        try {
            //Creamos una capa de tipo GeoJson para cargar nuestros datos de ubicación de farmacias desde el archivo tipo GeoJson
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.farmacias, this);

            //A la capa layer, le aplicamos estilo de Point para el mapa
            GeoJsonPointStyle pointstyle = layer.getDefaultPointStyle();
            //A los points les asignamos color y titulo, que se mostrará al hacer click en ellos
            pointstyle.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            pointstyle.setTitle("Farmacia");

            //Finalmente, una vez configurada la capa, la añadimos al mapa
            layer.addLayerToMap();


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MapsActivity.this, "Fallo Lectura JSON",
                    Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MapsActivity.this, "Fallo JSON",
                    Toast.LENGTH_SHORT).show();
        }

        //Comprobamos con el if si los permisos para la Geolocalización están revocados, y si es así, dentro del if se solicitan

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //En el caso de que los permisos esténrevocados, los solicitamos.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,}, 1000);

        }

        //Habilitamos nuestro posicionamiento GPS
        mMap.setMyLocationEnabled(true);

        //Mostrmos el botón que nos posicionará en el mapa en nuestra ubicación
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


    }//Fin onMapReady


}//Fin MapsActivity