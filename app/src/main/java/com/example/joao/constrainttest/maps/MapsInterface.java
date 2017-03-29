package com.example.joao.constrainttest.maps;


import com.example.joao.constrainttest.maps.model.Campus;
import com.example.joao.constrainttest.maps.model.Place;
import com.example.joao.constrainttest.maps.model.Region;

import java.util.ArrayList;

public interface MapsInterface {

    void queryAllPlaces(PlacesCallback callback);
    void queryPlaceById(String placeId, PlaceCallback callback);

    void queryAllRegions(RegionsCallback callback);
    void queryRegionById(String regionId, RegionCallback callback);

    void queryAllCampuses(CampusesCallback callback);
    void queryCampusById(String campusId, CampusCallback callback);

    interface PlaceCallback{
        void onSuccess(Place place);
        void onError();
    }

    interface PlacesCallback {
        void onSuccess(ArrayList<Place> places);
        void onError();
    }

    interface RegionsCallback{
        void onSuccess(ArrayList<Region> regions);
        void onError();
    }

    interface RegionCallback {
        void onSuccess(Region region);
        void onError();
    }

    interface CampusesCallback {
        void onSuccess(ArrayList<Campus> campuses);
        void onError();
    }

    interface CampusCallback {
        void onSuccess(Campus campus);
        void onError();
    }

}
