package com.example.cycle_tour_helper.ui.main;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cycle_tour_helper.R;
import com.example.cycle_tour_helper.ViewModelProviderFactory;
import com.example.cycle_tour_helper.models.Route;
import com.example.cycle_tour_helper.viewmodels.RouteViewModel;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.matching.v5.MapboxMapMatching;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.HillshadeLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterDemSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_CYCLING;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.hillshadeHighlightColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.hillshadeShadowColor;

public class MapFragment extends DaggerFragment {

    private static final String TAG = "MapFragment";

    @Inject
    ViewModelProviderFactory providerFactory;
    RouteViewModel routeViewModel;

    private MapView mapView;
    private MapboxMap mMapboxMap;
    private MapboxNavigation navigation;

    private LatLng startPoint;
    private static final String LAYER_ID = "hillshade-layer";
    private static final String SOURCE_ID = "hillshade-source";
    private static final String SOURCE_URL = "mapbox://mapbox.terrain-rgb";
    private static final String HILLSHADE_HIGHLIGHT_COLOR = "#008924";
    private EditText originLat;
    private EditText originLong;
    private EditText destinationLat;
    private EditText destinationLong;
    private Button searchButton;
    private Button startNavigationBtn;

    private List<Point> customRoutePoints;


    public MapFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        originLat = view.findViewById(R.id.origin_latitude);
        originLong = view.findViewById(R.id.origin_longitude);
        destinationLat = view.findViewById(R.id.destination_latitude);
        destinationLong = view.findViewById(R.id.destination_longitude);
        searchButton = view.findViewById(R.id.search_button);
        startNavigationBtn = view.findViewById(R.id.start_navigation);

        navigation = new MapboxNavigation(getActivity(), getString(R.string.mapbox_access_token));

        routeViewModel = ViewModelProviders.of(getActivity(), providerFactory).get(RouteViewModel.class);

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
             //   if(stringIsNotEmpty(originLat.getText().toString()) && stringIsNotEmpty(originLong.getText().toString()) &&
             //   stringIsNotEmpty(destinationLat.getText().toString()) && stringIsNotEmpty(destinationLong.getText().toString())){

                    searchRoute(originLat.getText().toString(), originLong.getText().toString(),
                            destinationLat.getText().toString(), destinationLong.getText().toString());
               // }
            }
        });

        startNavigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startNavigation();
            }
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        RasterDemSource rasterDemSource = new RasterDemSource(SOURCE_ID, SOURCE_URL);
                        style.addSource(rasterDemSource);

                        // Create and style a hillshade layer to add to the map
                        HillshadeLayer hillshadeLayer = new HillshadeLayer(LAYER_ID, SOURCE_ID)
                                .withProperties(hillshadeHighlightColor(Color.parseColor(HILLSHADE_HIGHLIGHT_COLOR)),
                                        hillshadeShadowColor(Color.BLACK)
                                );
                        // Add the hillshade layer to the map
                        style.addLayerBelow(hillshadeLayer, "aerialway");

                    }
                });
            }
        });

        return view;
    }


    private boolean stringIsNotEmpty(String s){
        if(!s.equals("") && !TextUtils.isEmpty(s)){
            return true;
        } else {
            return false;
        }
    }

    private void searchRoute(String originLat, String originLong, String destinationLat, String destinationLong){
        Point origin = Point.fromLngLat(-77.03613, 38.90992);
        Point destination = Point.fromLngLat(-77.0365, 38.8977);

        NavigationRoute.builder(getActivity())
                .accessToken(getString(R.string.mapbox_access_token))
                .origin(origin)
                .destination(destination)
                .profile(PROFILE_CYCLING)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response.body() != null){
                            boolean simulateRoute = true;
                            // Create a NavigationLauncherOptions object to package everything together
                            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                    .directionsRoute(response.body().routes().get(0))
                                    .shouldSimulateRoute(simulateRoute)
                                    .build();

                            // Call this method with Context from within an Activity
                            NavigationLauncher.startNavigation(getActivity(), options);

                            Log.i(TAG, "onResponse: " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });


    }

    private void startNavigation(){
        createNavigationObjectFromMapMatching();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        routeViewModel.getSelectedRoute().observe(getActivity(), new Observer<Route>() {
            @Override
            public void onChanged(Route selectedRoute) {
                new LoadGeoJson(MapFragment.this).execute(selectedRoute);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        routeViewModel.getSelectedRoute().removeObservers(getActivity());
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
        navigation.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void drawLines(@NonNull FeatureCollection featureCollection) {
        if (mMapboxMap != null) {
            mMapboxMap.getStyle(style -> {
                if (featureCollection.features() != null) {
                    if (featureCollection.features().size() > 0) {
                        style.addSource(new GeoJsonSource("line-source", featureCollection));

                        // The layer properties for our line. This is where we make the line dotted, set the
                        // color, etc.
                        style.addLayer(new LineLayer("linelayer", "line-source")
                                .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                        PropertyFactory.lineOpacity(.7f),
                                        PropertyFactory.lineWidth(5f),
                                        PropertyFactory.lineColor(getResources().getColor(R.color.colorDarkAccent))));
                    }
                }
            });
        }
    }

    private String loadJSONFromAsset(String fileName){
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private void getRouteStartPoint(String parsedJson){
        if(parsedJson == null) {
            //current location
            startPoint = new LatLng(mMapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(),
                    mMapboxMap.getLocationComponent().getLastKnownLocation().getLongitude());
        }
        try {
            JSONObject obj = new JSONObject(parsedJson);
            Log.i(TAG, "getRouteStartPoint: whole json object" + obj.toString());
            JSONArray coordinatesArray = obj.getJSONArray("features").getJSONObject(0)
                    .getJSONObject("geometry").getJSONArray("coordinates");

            JSONArray firstCoordinate = coordinatesArray.getJSONArray(0);
            startPoint = new LatLng((Double) firstCoordinate.get(1), (Double) firstCoordinate.get(0));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getRouteCoordinates(String parsedJson){
            customRoutePoints = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(parsedJson);
            JSONArray coordinatesArray = obj.getJSONArray("features").getJSONObject(0)
                    .getJSONObject("geometry").getJSONArray("coordinates");
            for (int i = 0; i < 15; i++) {
                LatLng coord = new LatLng((Double) coordinatesArray.getJSONArray(i).get(1),
                        (Double) coordinatesArray.getJSONArray(i).get(0));
                customRoutePoints.add(Point.fromLngLat(coord.getLongitude(), coord.getLatitude()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createNavigationObjectFromMapMatching(){
        MapboxMapMatching.builder()
                .accessToken(Mapbox.getAccessToken())
                .coordinates(customRoutePoints)
                .steps(true)
                .voiceInstructions(true)
                .bannerInstructions(true)
                .profile(PROFILE_CYCLING)
                .build()
                .enqueueCall(new Callback<MapMatchingResponse>() {
                    @Override
                    public void onResponse(Call<MapMatchingResponse> call, Response<MapMatchingResponse> response) {
                        Log.i(TAG, "onResponse: responseBody" + response.body().toString());
                        if (response.isSuccessful()) {
                            Log.i(TAG, "createNavigationObjectFromMapMatching + onResponse successful!" + response.body().toString());
                            DirectionsRoute route = response.body().matchings().get(0).toDirectionRoute();
                            navigation.startNavigation(route);

                            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                    .directionsRoute(route)
                                    .shouldSimulateRoute(true)
                                    .build();

                            // Call this method with Context from within an Activity
                            NavigationLauncher.startNavigation(getActivity(), options);
                        }
                    }

                    @Override
                    public void onFailure(Call<MapMatchingResponse> call, Throwable throwable) {
                        Log.e(TAG, "onFailure: ", throwable );
                    }
                });
    }



    private void setCameraToStartPoint(){
        //TODO: set bearing and zoom dependant on route direction and zoom
        CameraPosition position = new CameraPosition.Builder()
                .target(startPoint)
                .zoom(8)
                .bearing(0)
                .tilt(30)
                .build();

        mMapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);
    }



    private static class LoadGeoJson extends AsyncTask<Route, Void, FeatureCollection> {

        private WeakReference<MapFragment> weakReference;

        LoadGeoJson(MapFragment mapFragment) {
            this.weakReference = new WeakReference<>(mapFragment);
        }

        static String convertStreamToString(InputStream is) {
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }

        @Override
        protected FeatureCollection doInBackground(Route... routes) {
            try {
                StringBuilder sb = new StringBuilder()
                        .append(routes[0].getRouteFileName())
                        .append(".geojson");

                MapFragment mapFragment = weakReference.get();
                if (mapFragment != null) {
                    InputStream inputStream = mapFragment.getActivity().getAssets().open(sb.toString());
                    mapFragment.getRouteStartPoint(mapFragment.loadJSONFromAsset(sb.toString()));
                    mapFragment.getRouteCoordinates(mapFragment.loadJSONFromAsset(sb.toString()));
                    return FeatureCollection.fromJson(convertStreamToString(inputStream));
                }
            } catch (Exception exception) {
                Log.e(TAG, "Exception Loading GeoJSON: %s" + exception.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(@Nullable FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            MapFragment mapFragment = weakReference.get();
            if (mapFragment != null && featureCollection != null) {
                mapFragment.drawLines(featureCollection);
                mapFragment.setCameraToStartPoint();
                mapFragment.startNavigationBtn.setEnabled(true);
            }
        }
    }
}
