package com.example.medihelp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static ArrayList<Data> info;
    ArrayList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        info = new ArrayList<>();
        info = (ArrayList<Data>) getIntent().getSerializableExtra("data");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        markers = new ArrayList<>();
        // Add a marker in Sydney and move the camera
        ArrayList<Double>lat,longi;
        lat=new ArrayList<>();
        longi=new ArrayList<>();
        ArrayList<Data>new_list = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            Double lat_temp,longi_temp;
            lat_temp=covertLat(info.get(i).Googlemapcorridinatelati);
            longi_temp=convertLong(info.get(i).Googlemapcorridinatelongi);
            if(lat_temp==0 && longi_temp==0) continue;
            lat.add(lat_temp);
            longi.add(longi_temp);
            new_list.add(info.get(i));
        }
        for (int i = 0; i < lat.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat.get(i),longi.get(i))).title(new_list.get(i).Hospitalname).snippet(new_list.get(i).AddressFirstLine));
        }
        if(lat.size()!=0) {
            /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat.get(0), longi.get(0)), 10));
            CameraUpdateFactory.newLatLngZoom(new LatLng(lat.get(0), longi.get(0)), 10);
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10),2000,null);*/
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat.get(0), longi.get(0))).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }

    private double covertLat(String s) {
        return setcorrect(s);
    }

    private double setcorrect(String s) {
        if(s.equalsIgnoreCase("NA")){
            return 0.0;
        }
        Double ans=0.0;
        if (s.contains("ø")) {
            String degree="";
            String min="";
            String sec="";
            int idx;
            for(idx=0;idx<s.length();idx++){
                if(s.charAt(idx)=='ø') break;
                degree+=s.charAt(idx);
            }
            idx++;
            for(;idx<s.length();idx++){
                if(s.charAt(idx)=='\'') break;
                min+=s.charAt(idx);
            }
            idx++;
            for(;idx<s.length();idx++){
                sec+=s.charAt(idx);
            }
            ans=Double.parseDouble(degree)+Double.parseDouble(min)/60.0+Double.parseDouble(sec)/3600.0;

        }else{
            ans=Double.parseDouble(s);
        }
        return ans;
    }

    private double convertLong(String s) {
        return setcorrect(s);
    }


}
