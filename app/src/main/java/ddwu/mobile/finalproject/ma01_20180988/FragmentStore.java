package ddwu.mobile.finalproject.ma01_20180988;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FragmentStore extends Fragment implements OnMapReadyCallback {
    final static int PERMISSION_REQ_CODE = 100;

    private View view;
    private SearchView svStore;
    private MapView mapView;
    private ConstraintLayout clSelGood;
    private TextView tvSelGoodName, tvSelGoodDetail;
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private SimpleCursorAdapter cursorAdapter;
    private Cursor cursor;
    private LocationRequest locationRequest;
    private NetworkManager networkManager;
    private String apiAddress, apiKey;
    private String areaCode;

    public FragmentStore() {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store, container, false);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

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
        mGoogleMap.setPadding(0, 200, 0, 0);

        locationUpdate();

        if (checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
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
               LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
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
