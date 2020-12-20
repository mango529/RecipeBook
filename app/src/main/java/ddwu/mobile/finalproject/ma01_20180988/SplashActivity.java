package ddwu.mobile.finalproject.ma01_20180988;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    NetworkManager networkManager;
    ArrayList<Product> productList;
    ProductXmlParser parser;
    String apiAddress;
    String apiKey;

    TextView tvDownload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        SharedPreferences pref = getSharedPreferences("config", 0);

        if (pref.getBoolean("first", true)) {
            networkManager = new NetworkManager(this);
            productList = new ArrayList<>();
            parser = new ProductXmlParser();

            tvDownload = findViewById(R.id.tvDownload);
            tvDownload.setVisibility(View.VISIBLE);

            apiAddress = getResources().getString(R.string.goodId_api_url);
            apiKey = getResources().getString(R.string.product_api_key);

            new NetworkAsyncTask().execute(apiAddress + apiKey);
        }
        else {
            Handler hd = new Handler();
            hd.postDelayed(new SplashHandler(), 3000);
        }
    }

    private class SplashHandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), MainActivity.class));
            SplashActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() { }

    class NetworkAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            Log.d("goeun", address);
            String result = null;

            result = networkManager.downloadContents(address);
            if (result == null) return "Error";

            productList = parser.parse(result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            ProductDBManager productDBManager = new ProductDBManager(SplashActivity.this);
            for (Product p : productList) {
                productDBManager.insertProductInfo(p);
            }

            SharedPreferences pref = getSharedPreferences("config", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("first", false);
            editor.commit();

            Handler hd = new Handler();
            hd.postDelayed(new SplashHandler(), 1000);
        }
    }
}
