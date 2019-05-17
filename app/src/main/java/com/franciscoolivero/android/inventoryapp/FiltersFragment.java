package com.franciscoolivero.android.inventoryapp;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class FiltersFragment extends Fragment {

    public FiltersFragment() {
        //Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_details, container, false);


        final ArrayList<Location> locations = new ArrayList<>();
        final ArrayList<String> planetHours = new ArrayList<>();
        final ArrayList<String> botanicHours = new ArrayList<>();

//        planetHours.add(getString(R.string.planetarium_business_hours_1));
//        planetHours.add(getString(R.string.planetarium_business_hours_2));
//        locations.add(new Location(getString(R.string.planetarium_name),
//                getString(R.string.planetarium_description),
//                getString(R.string.planetarium_address),
//                planetHours,
//                R.drawable.planetario,
//                Uri.parse("http://plus.codes/" + getString(R.string.planetarium_geolocation_uri))));
//        locations.add(new Location(getString(R.string.parque_niños_name),
//                getString(R.string.parque_niños_description),
//                getString(R.string.parque_niños_address),
//                getString(R.string.parque_niños_business_hours),
//                R.drawable.parque,
//                Uri.parse("http://plus.codes/" + getString(R.string.parque_niños_geolocation_uri))));
//        locations.add(new Location(getString(R.string.rosedal_name),
//                getString(R.string.rosedal_description),
//                getString(R.string.rosedal_address),
//                getString(R.string.rosedal_business_hours),
//                Uri.parse("http://plus.codes/" + getString(R.string.rosedal_geolocation_uri))));
//        botanicHours.add(getString(R.string.botanical_business_hours_1));
//        botanicHours.add(getString(R.string.botanical_business_hours_2));
//        locations.add(new Location(getString(R.string.botanical_name),
//                getString(R.string.botanical_description),
//                getString(R.string.botanical_address),
//                botanicHours,
//                Uri.parse("http://plus.codes/" + getString(R.string.botanical_geolocation_uri))));


        //Log to check that the list was created correctly.
        for (int i = 0; i < locations.size(); i++) {
            Log.v("ShoppingActivity", "Current Location Object: " + locations.get(i));

        }

//        LocationAdapter adapter = new LocationAdapter(getActivity(), locations);
//        ListView listView = rootView.findViewById(R.id.list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                showOnMap(i, locations);
//            }
//        });

        return rootView;

    }

//    private void showOnMap(int i, ArrayList<Location> locations) {
//        Uri geoLocation = locations.get(i).getmGeoLocation();
//        if (geoLocation != null) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            //Sets URI (Uniform Resource Identifier)
//            intent.setData(locations.get(i).getmGeoLocation());
//            intent.setPackage("com.google.android.apps.maps");
//            //Checks for nullity (if there is no app that supports this intent)
//            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                startActivity(intent);
//            }
//        }
//    }
}


