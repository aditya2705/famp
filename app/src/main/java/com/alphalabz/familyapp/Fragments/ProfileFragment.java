package com.alphalabz.familyapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphalabz.familyapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements OnMapReadyCallback {

    LatLng locationGlobal;
    private GoogleMap mMap;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        setPersonalData(v, "Rushabh", "Shah", "Manoj", "Swati", "10/07/1994", "rms@gmail.com", 'M');
        final LatLng location = new LatLng(-37.813, 144.962);
        String address = "Sardar Patel Institute of Technoogy,\nAndheri W,\nMumbai";
        setContactDetails(v, "+919855453344", "+912225435442", address, location);
        return v;
    }

    public void setPersonalData(View v, String name, String familyName, String fatherName, String motherName, String birthdate, String email, char gender) {
        ImageView userIcon = (ImageView) v.findViewById(R.id.user_icon);
        userIcon.setImageResource(R.drawable.user1);

        TextView nameT = (TextView) v.findViewById(R.id.profile_name);
        String newName = gender == 'M' ? "Mr " + name : "Miss " + name;
        nameT.setText(newName);

        TextView firstNameT = (TextView) v.findViewById(R.id.profile_firstname);
        firstNameT.setText(name);

        TextView familyNameT = (TextView) v.findViewById(R.id.profile_familyname);
        familyNameT.setText(familyName);

        TextView fatherNameT = (TextView) v.findViewById(R.id.profile_fathername);
        fatherNameT.setText(fatherName);

        TextView motherNameT = (TextView) v.findViewById(R.id.profile_mothername);
        motherNameT.setText(motherName);

        TextView birthdateT = (TextView) v.findViewById(R.id.profile_birthdate);
        birthdateT.setText(birthdate);

        TextView emailT = (TextView) v.findViewById(R.id.profile_email);
        emailT.setText(email);

    }

    public void setContactDetails(View v, String mobile, String phone, String address, LatLng location) {
        TextView mobileT = (TextView) v.findViewById(R.id.profile_mobile);
        mobileT.setText(mobile);

        TextView phoneT = (TextView) v.findViewById(R.id.profile_phone);
        phoneT.setText(phone);

        TextView addressT = (TextView) v.findViewById(R.id.profile_address);
        addressT.setText(address);

        setUpMap(location);
    }

    public void setUpMap(LatLng location) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationGlobal = location;


    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(locationGlobal)
                .title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(locationGlobal)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
