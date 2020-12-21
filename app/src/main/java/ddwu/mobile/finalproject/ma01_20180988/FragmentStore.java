package ddwu.mobile.finalproject.ma01_20180988;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;


public class FragmentStore extends Fragment implements OnMapReadyCallback {
    final static int PERMISSION_REQ_CODE = 100;

    private View view;
    private MapView mapView;
    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private LocationManager locationManager;
    private List<String> placeTypes;
    private PlacesClient placesClient;
    private LatLng currentLoc;

    public FragmentStore() {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store, container, false);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        Places.initialize(getActivity().getApplicationContext(), getString(R.string.map_api_key));
        placesClient = Places.createClient(getContext());

        placeTypes = new ArrayList<>();
        placeTypes.add(PlaceType.DEPARTMENT_STORE);
        placeTypes.add(PlaceType.STORE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        locationUpdate();

        if (checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
        }
        markerOptions = new MarkerOptions();

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location==null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(location!=null) {
            findStore(location);
        }

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeId = marker.getTag().toString();
                List<Place.Field> placeFields  = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse response) {
                        Place place = response.getPlace();
                        openPlaceDetail(place);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e("goeun", apiException.getMessage() + statusCode);
                        }
                    }
                });
            }
        });
    }

    private void getPlaceDetail(String placeId) {

    }

    private void openPlaceDetail(Place place) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_place_detail);
        TextView tvPlaceName = dialog.findViewById(R.id.tvPlaceName);
        TextView tvPlacePhoneNumber = dialog.findViewById(R.id.tvPlacePhoneNumber);
        TextView tvPlaceAddr = dialog.findViewById(R.id.tvPlaceAddr);
        tvPlaceName.setText(place.getName());
        if (place.getPhoneNumber() != null) tvPlacePhoneNumber.setText(place.getPhoneNumber());
        tvPlaceAddr.setText(place.getAddress());
        dialog.show();
    }

    PlacesListener placesListener = new PlacesListener() {
        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (noman.googleplaces.Place place : places) {
                        markerOptions.title(place.getName());
                        markerOptions.snippet(place.getTypes()[0]);
                        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                        ));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        newMarker.setTag(place.getPlaceId());
                    }
                }
            });
        }

        @Override
        public void onPlacesFailure(PlacesException e) {}

        @Override
        public void onPlacesStart() {}

        @Override
        public void onPlacesFinished() {}
    };

    private void findStore(Location location) {
        for (String type : placeTypes) {
            new NRPlaces.Builder().listener(placesListener)
                    .key(getString(R.string.map_api_key))
                    .latlng(location.getLatitude(), location.getLongitude())
                    .radius(300)
                    .type(type)
                    .build()
                    .execute();
        }
    }

    private  void locationUpdate() {
        if (checkPermission()) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
         }
     }

    LocationListener locationListener = new LocationListener() {
           @Override
           public void onLocationChanged(Location location) {
               currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
               mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
           }

           @Override
           public void onStatusChanged(String provider, int status, Bundle extras) {       }

           @Override
           public void onProviderEnabled(String provider) {       }

            @Override
            public void onProviderDisabled(String provider) {       }
      };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationUpdate();
            } else {
                Toast.makeText(getContext(), "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
