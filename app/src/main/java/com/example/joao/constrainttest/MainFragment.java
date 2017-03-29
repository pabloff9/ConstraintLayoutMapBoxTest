package com.example.joao.constrainttest;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static final String TAG_MAIN_FRAGMENT = "main_fragment_tag";

    // JSON encoding/decoding
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

    private MapView mapView;
    private MapboxMap map;
    private OfflineManager offlineManager;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapview);

        mapView.onCreate(savedInstanceState);


        return rootView;
    }

    private void updateMap(double latitude, double longitude) {
        // Build marker
        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Geocoder result"));

        // Animate camera to geocoder result location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(15)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                //downloadOfflineRegion();
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void downloadOfflineRegion() {
        offlineManager = OfflineManager.getInstance(getContext());


        // Define the offline region
        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                mapView.getStyleUrl(),
                getSamambaiaRegionBounds(),
                14,    // min zoom
                18,   // max zoom
                MainFragment.this.getResources().getDisplayMetrics().density);

        // Set the metadata
        byte[] metadata;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_FIELD_REGION_NAME, "Campus Samambaia");
            String json = jsonObject.toString();
            metadata = json.getBytes(JSON_CHARSET);
        } catch (Exception exception) {
            Log.e("Tag", "Failed to encode metadata: " + exception.getMessage());
            metadata = null;
        }


        // Create the region asynchronously
        offlineManager.createOfflineRegion(
                definition,
                metadata,
                new OfflineManager.CreateOfflineRegionCallback() {
                    @Override
                    public void onCreate(OfflineRegion offlineRegion) {
                        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);

                        // Display the download progress bar

                        // Monitor the download progress using setObserver
                        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                            @Override
                            public void onStatusChanged(OfflineRegionStatus status) {

                                // Compute a percentage
                                double percentage = status.getRequiredResourceCount() >= 0
                                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                        0.0;

                                if (status.isComplete()) {
                                    // Download complete
                                    Log.d("Tag", "Region downloaded successfully.");
                                    return;
                                } else if (status.isRequiredResourceCountPrecise()) {
                                    // Switch to determinate state
                                    Log.d("Tag", "Percentage: " + (int) Math.round(percentage));
                                }

                                // Log what is being currently downloaded
                                Log.d("Tag", String.format(" %s/%s resources; %s bytes downloaded.",
                                        String.valueOf(status.getCompletedResourceCount()),
                                        String.valueOf(status.getRequiredResourceCount()),
                                        String.valueOf(status.getCompletedResourceSize())));
                            }

                            @Override
                            public void onError(OfflineRegionError error) {
                                // If an error occurs, print to logcat
                                Log.e("Tag", "onError reason: " + error.getReason());
                                Log.e("Tag", "onError message: " + error.getMessage());
                            }

                            @Override
                            public void mapboxTileCountLimitExceeded(long limit) {
                                // Notify if offline region exceeds maximum tile count
                                Log.e("Tag", "Mapbox tile count limit exceeded: " + limit);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("Tag", "Error: " + error);
                    }
                });
    }

    public void deleteRegions() {
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(OfflineRegion[] offlineRegions) {

                for (final OfflineRegion region : offlineRegions) {
                    region.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                        @Override
                        public void onDelete() {
                            Log.d("Tag", "Deleting Region" + region.getID());
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("Tag", "Error deleting region " + error);
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                Log.e("Tag", "onListError: " + error);
            }
        });
    }

    public void addMarker() {
        LatLng ufgSamambaia = new LatLng();
        ufgSamambaia.setLatitude(-16.60498);
        ufgSamambaia.setLongitude(-49.262245);

        MarkerViewOptions marker = new MarkerViewOptions()
                .position(ufgSamambaia);

        map.addMarker(marker);
    }

    public void removeMarkers() {
        map.clear();
    }

    public void moveToRegion() {
        // move camera to selected position in 1 secs
        // 50 padding
        map.easeCamera(CameraUpdateFactory.newLatLngBounds(getSamambaiaRegionBounds(), 50), 1000);

    }

    private LatLngBounds getYosemiteParkRegionBounds() {
        // Create a bounding box for the offline region

        return new LatLngBounds.Builder()
                .include(new LatLng(37.7897, -119.5073)) // Northeast
                .include(new LatLng(37.6744, -119.6815)) // Southwest
                .build();
    }

    public void listRegionsSize() {
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(OfflineRegion[] offlineRegions) {
                for (final OfflineRegion region : offlineRegions) {

                    region.getStatus(new OfflineRegion.OfflineRegionStatusCallback() {
                        @Override
                        public void onStatus(OfflineRegionStatus status) {
                            // Log what is being currently downloaded
                            Log.d("Tag", String.format("id: %s - %s/%s resources; %s bytes downloaded.",
                                    region.getID(),
                                    String.valueOf(status.getCompletedResourceCount()),
                                    String.valueOf(status.getRequiredResourceCount()),
                                    String.valueOf(status.getCompletedResourceSize())));
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                Log.e("Tag", "" + error);
            }
        });
    }

    private LatLngBounds getSamambaiaRegionBounds() {

        return new LatLngBounds.Builder()
                .include(new LatLng(-16.6022881, -49.267271)) // Northeast
                .include(new LatLng(-16.6098476, -49.2571529)) // Southwest
                .build();
    }

    public void drawRegion() {
        List<LatLng> polygon = new ArrayList<>();

        polygon.add(new LatLng(-16.6030059835967, -49.270033836364746));
        polygon.add(new LatLng(-16.603787377905718, -49.27046298980713));
        polygon.add(new LatLng(-16.604733272028298, -49.27076339721679));
        polygon.add(new LatLng(-16.605884789022788, -49.27076339721679));
        polygon.add(new LatLng(-16.606789547533847, -49.27037715911865));
        polygon.add(new LatLng(-16.607324175560272, -49.26969051361084));
        polygon.add(new LatLng(-16.607858802098843, -49.26878929138183));
        polygon.add(new LatLng(-16.608228927292398, -49.26801681518555));
        polygon.add(new LatLng(-16.60872242644112, -49.26741600036621));
        polygon.add(new LatLng(-16.609174799546995, -49.26651477813721));
        polygon.add(new LatLng(-16.6095037975003, -49.26582813262939));
        polygon.add(new LatLng(-16.609873919524176, -49.26492691040039));
        polygon.add(new LatLng(-16.610202916279988, -49.264326095581055));
        polygon.add(new LatLng(-16.610490787979053, -49.26351070404053));
        polygon.add(new LatLng(-16.61065528589907, -49.262866973876946));
        polygon.add(new LatLng(-16.61081978367822, -49.262094497680664));
        polygon.add(new LatLng(-16.611025405704016, -49.26127910614014));
        polygon.add(new LatLng(-16.610984281316455, -49.26042079925537));
        polygon.add(new LatLng(-16.610984281316455, -49.259562492370605));
        polygon.add(new LatLng(-16.610984281316455, -49.25848960876465));
        polygon.add(new LatLng(-16.610696410357075, -49.25767421722412));
        polygon.add(new LatLng(-16.610531912472258, -49.25694465637206));
        polygon.add(new LatLng(-16.610202916279988, -49.25625801086426));
        polygon.add(new LatLng(-16.609668296265625, -49.256043434143066));
        polygon.add(new LatLng(-16.608928050712233, -49.25595760345458));
        polygon.add(new LatLng(-16.608146677311023, -49.25591468811035));
        polygon.add(new LatLng(-16.607283050380296, -49.25595760345458));
        polygon.add(new LatLng(-16.606583920973662, -49.25595760345458));
        polygon.add(new LatLng(-16.605884789022788, -49.256043434143066));
        polygon.add(new LatLng(-16.605103403243454, -49.256343841552734));
        polygon.add(new LatLng(-16.604486517488716, -49.256558418273926));
        polygon.add(new LatLng(-16.603993007458907, -49.25703048706055));
        polygon.add(new LatLng(-16.603170487925816, -49.25767421722412));
        polygon.add(new LatLng(-16.602471343554456, -49.25823211669922));
        polygon.add(new LatLng(-16.601813322998943, -49.258832931518555));
        polygon.add(new LatLng(-16.60103192066285, -49.25939083099365));
        polygon.add(new LatLng(-16.60062065499928, -49.259819984436035));
        polygon.add(new LatLng(-16.600209388455617, -49.26024913787841));
        polygon.add(new LatLng(-16.599962628106994, -49.2606782913208));
        polygon.add(new LatLng(-16.599839247813883, -49.26149368286133));
        polygon.add(new LatLng(-16.60004488159175, -49.26248073577881));
        polygon.add(new LatLng(-16.60004488159175, -49.2632532119751));
        polygon.add(new LatLng(-16.60033276851112, -49.263811111450195));
        polygon.add(new LatLng(-16.600579528384504, -49.26475524902344));
        polygon.add(new LatLng(-16.600744034790754, -49.26548480987549));
        polygon.add(new LatLng(-16.60094966760054, -49.266042709350586));
        polygon.add(new LatLng(-16.601278679638543, -49.26638603210449));
        polygon.add(new LatLng(-16.601360932560038, -49.26685810089111));
        polygon.add(new LatLng(-16.60152543829739, -49.267587661743164));
        polygon.add(new LatLng(-16.601813322998943, -49.268145561218255));
        polygon.add(new LatLng(-16.601566564709742, -49.26921844482422));
        polygon.add(new LatLng(-16.6030059835967, -49.270033836364746));


        map.addPolygon(new PolygonOptions()
                .addAll(polygon)
                .fillColor(Color.parseColor("#3bb2d0")));

    }

    public void searchParams(String query) {

    }
}
