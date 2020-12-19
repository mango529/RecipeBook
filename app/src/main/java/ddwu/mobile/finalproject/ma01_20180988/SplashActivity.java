package ddwu.mobile.finalproject.ma01_20180988;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    NetworkManager networkManager;
    ArrayList<Product> productList;
    ProductXmlParser parser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences("config", 0);

        if (pref.getBoolean("first", true)) {
            networkManager = new NetworkManager(this);
            productList = new ArrayList<>();
            parser = new ProductXmlParser();
        }

        Handler hd = new Handler();
        hd.postDelayed(new SplashHandler(), 3000);
        getSupportActionBar().hide();
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
        ProgressDialog progressDlg = new ProgressDialog(SplashActivity.this, R.style.AppCompatAlertDialogStyle);

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

            progressDlg.dismiss();
            Handler hd = new Handler();
            hd.postDelayed(new SplashHandler(), 1000);
            getSupportActionBar().hide();
        }
    }
}
