package com.example.joao.constrainttest.maps;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by joao on 3/15/17.
 */

public class MapsFirebaseRepository implements MapsInterface {

    private DatabaseReference mapsReference;
    private DatabaseReference placesReference;
    private DatabaseReference campusesReferece;
    private DatabaseReference regionsReference;

    public MapsFirebaseRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mapsReference = database.getReference("map");
        placesReference = mapsReference.child("places");
        campusesReferece = mapsReference.child("campuses");
        regionsReference = mapsReference.child("regions");
    }

    @Override
    public void queryAllPlaces(PlacesCallback callback) {

    }

    @Override
    public void queryPlaceById(String placeId, PlaceCallback callback) {

    }

    @Override
    public void queryAllRegions(RegionsCallback callback) {

    }

    @Override
    public void queryRegionById(String regionId, RegionCallback callback) {

    }

    @Override
    public void queryAllCampuses(CampusesCallback callback) {

    }

    @Override
    public void queryCampusById(String campusId, CampusCallback callback) {

    }
}
