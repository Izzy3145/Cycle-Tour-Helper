package com.example.cycle_tour_helper.ui.main;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cycle_tour_helper.R;
import com.example.cycle_tour_helper.ViewModelProviderFactory;
import com.example.cycle_tour_helper.models.Route;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.Mapbox;
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

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Scanner;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.hillshadeHighlightColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.hillshadeShadowColor;

public class MapFragment extends DaggerFragment {

    private static final String TAG = "MapFragment";

    @Inject
    ViewModelProviderFactory providerFactory;
    RouteViewModel routeViewModel;

    private MapView mapView;

    private MapboxMap mMapboxMap;
    private static final String LAYER_ID = "hillshade-layer";
    private static final String SOURCE_ID = "hillshade-source";
    private static final String SOURCE_URL = "mapbox://mapbox.terrain-rgb";
    private static final String HILLSHADE_HIGHLIGHT_COLOR = "#008924";


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
        routeViewModel = ViewModelProviders.of(getActivity(), providerFactory).get(RouteViewModel.class);

        return view;
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
                Log.e(TAG, "doInBackground: String built = " + sb.toString());

                MapFragment mapFragment = weakReference.get();
                if (mapFragment != null) {
                    InputStream inputStream = mapFragment.getActivity().getAssets().open(sb.toString());
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
            }
        }
    }
}
