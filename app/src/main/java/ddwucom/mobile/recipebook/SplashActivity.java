package ddwucom.mobile.recipebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {
    final String PREF_FIRST_START = "AppFirstLaunch";
    private static final String TAG = "SplashActivity";
    private ProgressBar progressBar;
    private TextView tvSplashDown;
    RecipeDBManager recipeDBManager;
    RecipeNetworkManager networkManager;
    RecipeXmlParser parser;
    ArrayList<Recipe> recipeList;
    String apiAddress;
    AsyncTask task;
    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        apiAddress = getResources().getString(R.string.api_url);
        recipeDBManager = new RecipeDBManager(this);
        networkManager = new RecipeNetworkManager(this);
        parser = new RecipeXmlParser();
        recipeList = new ArrayList<>();
        tvSplashDown = findViewById(R.id.tvSplashDown);
        progressBar = findViewById(R.id.pbSplash);
        progressBar.setMax(1000);

        pref = getSharedPreferences(PREF_FIRST_START, 0);
        if (pref.getBoolean(PREF_FIRST_START, true)) {
            pref.edit().putBoolean(PREF_FIRST_START, false).commit();
            tvSplashDown.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            for (int i = 1; i <= 1000; ) {
                String count = i + "/" + (i + 39);
                String result = null;
                try {
                    result =  new NetworkAsyncTask().execute(apiAddress + count).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (result != null) {
                    i += 40;
                }
            }

        }
        else if (!pref.getBoolean(PREF_FIRST_START, true)){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];

            String result = null;
            result = networkManager.downloadContents(address);
            if (result == null) return "Error";

            recipeList = parser.parse(result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            for (Recipe r : recipeList) {
                boolean addResult = recipeDBManager.addNewRecipe(r);
                if (addResult ) {
//                    Log.d(TAG, "DB에 레시피 저장 성공!");
                } else {
//                    Log.d(TAG, "DB에 레시피 저장 실패!");
                }
            }
        }
    }
}
