package com.alphalabz.familyapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public construct
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        String profileID = getArguments().getString("profile_id");


        //Person rms = new Person();

        //setPersonalData(v, rms);
        //setContactDetails(v, rms);
        return v;
    }
/*
    public void setPersonalData(View v, Person p) {
        ImageView userIcon = (ImageView) v.findViewById(R.id.user_icon);
        userIcon.setImageResource(R.drawable.user1);

        TextView nameT = (TextView) v.findViewById(R.id.profile_name);
        String newName = p.getGender() == 'M' ? "Mr " + p.getName() : "Miss " + p.getName();
        nameT.setText(newName);

        TextView firstNameT = (TextView) v.findViewById(R.id.profile_firstname);
        firstNameT.setText(p.getName());

        TextView familyNameT = (TextView) v.findViewById(R.id.profile_familyname);
        familyNameT.setText(p.getFamilyName());

        TextView fatherNameT = (TextView) v.findViewById(R.id.profile_fathername);
        fatherNameT.setText(p.getFatherID());

        TextView motherNameT = (TextView) v.findViewById(R.id.profile_mothername);
        motherNameT.setText(p.getMotherName());

        TextView birthdateT = (TextView) v.findViewById(R.id.profile_birthdate);
        birthdateT.setText(p.getBirthDate());

        TextView emailT = (TextView) v.findViewById(R.id.profile_email);
        emailT.setText(p.getEmail());

    }

    public void setContactDetails(View v, Person p) {
        TextView mobileT = (TextView) v.findViewById(R.id.profile_mobile);
        mobileT.setText(p.getMobile());

        TextView phoneT = (TextView) v.findViewById(R.id.profile_phone);
        phoneT.setText(p.getPhone());

        TextView addressT = (TextView) v.findViewById(R.id.profile_address);
        addressT.setText(p.getAddress());

    }

*/

}
