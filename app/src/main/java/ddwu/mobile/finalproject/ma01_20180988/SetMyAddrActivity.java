package ddwu.mobile.finalproject.ma01_20180988;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SetMyAddrActivity extends AppCompatActivity {
    private static final String TAG = "SetMyAddrActivity";
    private ListView lvArea, lvDetailArea;

    private NetworkManager networkManager;
    private AreaXmlParser parser;
    private List<Area> areaList;
    private ArrayAdapter<Area> areaAdapter, detailAreaAdapter;
    private String apiAddress, apiKey;
    private Area area, detailArea;
    private View selAreaView, selDetailAreaView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_addr);

        lvArea = findViewById(R.id.lvArea);
        lvDetailArea = findViewById(R.id.lvDetailArea);

        networkManager = new NetworkManager(this);
        parser = new AreaXmlParser();
        areaList = new ArrayList<>();
        areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, areaList);
        lvArea.setAdapter(areaAdapter);

        apiAddress = getResources().getString(R.string.std_info_api_url);
        apiKey = getResources().getString(R.string.product_api_key);

        new NetworkAsyncTask().execute(apiAddress + "AR&" + apiKey);

        lvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selAreaView != null) {
                    selAreaView.setBackgroundColor(Color.WHITE);
                }
                if (detailArea != null) {
                    detailArea = null;
                }
                selAreaView = view;
                selAreaView.setBackgroundColor(getResources().getColor(R.color.pink_50));

                detailAreaAdapter = new ArrayAdapter<>(SetMyAddrActivity.this, android.R.layout.simple_selectable_list_item, areaList.get(position).getDetailAreas());
                lvDetailArea.setAdapter(detailAreaAdapter);

                area = areaList.get(position);
            }
        });

        lvDetailArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selDetailAreaView != null) {
                    selDetailAreaView.setBackgroundColor(Color.WHITE);
                }
                selDetailAreaView = view;
                selDetailAreaView.setBackgroundColor(getResources().getColor(R.color.pink_50));

                detailArea = area.getDetailAreas().get(position);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveMyAddr:
                if (area == null || detailArea == null) {
                    Toast.makeText(this, "지역, 상세 지역을 모두 선택해야합니다!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences pref = getSharedPreferences("config", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("areaName", area.getName() + " " + detailArea.getName());
                editor.putString("areaCode", detailArea.getCode());
                editor.commit();
                break;
        }
        finish();
    }

    class NetworkAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            Log.d("goeun", address);
            String result = null;

            result = networkManager.downloadContents(address);
            if (result == null) return "Error";

            areaList = parser.parse(result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            areaAdapter.addAll(areaList);
        }
    }
}
