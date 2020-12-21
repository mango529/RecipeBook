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
    private StoreInfoXmlParser storeInfoParser;
    private ProductPriceXmlParser productPriceParser;
    private String apiAddress, apiKey;
    private String areaCode;
    private List<Store> storeList;
    private Product selProduct;

    public FragmentStore() {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store, container, false);

        networkManager = new NetworkManager(getContext());
        apiKey = getString(R.string.product_api_key);
        selProduct = new Product();

        SharedPreferences pref = getActivity().getSharedPreferences("config", 0);
        areaCode = pref.getString("areaCode", null);
        if (areaCode == null) {
            Toast.makeText(getContext(), "MyPage에서 나의 지역을 먼저 설정해주세요!", Toast.LENGTH_SHORT).show();
        }
        else {
//            storeList = new ArrayList<>();
//            parser = new StoreInfoXmlParser();
//            apiAddress = getString(R.string.store_info_api_url);
//            new NetworkAsyncTask().execute(apiAddress + apiKey);
        }

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        svStore = view.findViewById(R.id.svStore);
        tvSelGoodName = view.findViewById(R.id.tvSelGoodName);
        tvSelGoodDetail = view.findViewById(R.id.tvSelGoodDetail);
        clSelGood = view.findViewById(R.id.clSelGood);

        cursorAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1, null,
                new String[] {"name"}, new int[] {android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        svStore.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        svStore.setIconifiedByDefault(false);
        svStore.setSuggestionsAdapter(cursorAdapter);
        svStore.setQueryHint("재료를 입력하세요.");

        int id = svStore.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.notosanskr_regular);
        TextView searchText = (TextView) svStore.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setIncludeFontPadding(false);

        svStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ProductDBHelper productDBHelper = new ProductDBHelper(getContext());
                SQLiteDatabase db = productDBHelper.getReadableDatabase();
                cursor = db.rawQuery("select _id, name, detail, goodId from " + productDBHelper.TABLE_NAME + " where name like '%" + newText + "%';", null);
                cursorAdapter.swapCursor(cursor);
                return false;
            }
        });

        svStore.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                clSelGood.setVisibility(View.VISIBLE);
                Cursor c = (Cursor) cursorAdapter.getItem(position);

                String inspectDay = findInspectDay();
                String goodId = c.getString(c.getColumnIndex(ProductDBHelper.COL_GOOD_ID));

                productPriceParser = new ProductPriceXmlParser();
                apiAddress = getString(R.string.product_price_api_url);
                new NetworkAsyncTask().execute(apiAddress + "goodInspectDay=" + inspectDay + "&goodId=" + goodId + "&" + apiKey);

                tvSelGoodName.setText(c.getString(c.getColumnIndex(ProductDBHelper.COL_NAME)));
                if (c.getString(c.getColumnIndex(ProductDBHelper.COL_DETAIL)) != null) {
                    tvSelGoodDetail.setVisibility(View.VISIBLE);
                    tvSelGoodDetail.setText(c.getString(c.getColumnIndex(ProductDBHelper.COL_DETAIL)));
                }
                return false;
            }
        });

        return view;
    }

    private String findInspectDay() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        System.out.println("입력된 날짜 : " + cal.getTime());
        if (dayOfWeek == 7) {
            cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
            System.out.println("입력된 날짜의 일요일  : " + cal.getTime());
        }
        else {
            cal.add(Calendar.DATE, -7);
            cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
            System.out.println("입력된 날짜의 이전주의 일요일 : " + cal.getTime());
        }
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
        return fm.format(cal.getTime());
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

    class NetworkAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDlg = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg.setTitle("Wait");
            progressDlg.setMessage("Downloading...");
            progressDlg.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            Log.d("goeun", address);
            String result = null;

            result = networkManager.downloadContents(address);
            if (result == null) return "Error";

            //storeList = parser.parse(result, areaCode);
            selProduct.getStoreList().addAll(productPriceParser.parse(result));

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //adapter.setList(recipeList);
            Log.d("goeun", result);
            //Log.d("goeun", "store 개수: " + storeList.size());
            Log.d("goeun", "굿즈 판매 정보 개수: " + selProduct.getStoreList().size());
            progressDlg.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
    }
}

