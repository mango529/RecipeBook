package ddwu.mobile.finalproject.ma01_20180988;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class MyRecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = "MyRecipeDetailActivity";

    ImageView ivDetailMyRcpImg;
    TextView tvMyRcpStep, tvDetailMyRcpName, tvMyRcpDate, tvMyRcpMemo, tvMyRcpHashtag;
    ListView lvDetailMyIngre;
    ViewPager vpMyManual;
    RatingBar rbRating;

    ArrayAdapter ingredientAdapter;
    ManualAdapter manualAdapter;
    NetworkManager networkManager;
    Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_detail);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        ivDetailMyRcpImg = findViewById(R.id.ivDetailMyRcpImg);
        tvDetailMyRcpName = findViewById(R.id.tvDetailMyRcpName);
        tvMyRcpStep = findViewById(R.id.tvMyRcpStep);
        tvMyRcpDate = findViewById(R.id.tvMyRcpDate);
        tvMyRcpMemo = findViewById(R.id.tvMyRcpMemo);
        tvMyRcpHashtag = findViewById(R.id.tvMyRcpHashtag);
        lvDetailMyIngre = findViewById(R.id.lvDetailMyIngre);
        vpMyManual = findViewById(R.id.vpMyManual);
        rbRating = findViewById(R.id.rbRating);

        ingredientAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipe.getIngredients());
        manualAdapter = new ManualAdapter(this, recipe.getManuals());
        networkManager = new NetworkManager(this);

        lvDetailMyIngre.setAdapter(ingredientAdapter);
        vpMyManual.setAdapter(manualAdapter);
        vpMyManual.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvMyRcpStep.setText(String.valueOf(recipe.getManuals().get(position).getStep()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tvDetailMyRcpName.setText(recipe.getName());
        new GetImageAsyncTask().execute(recipe.getImageLink());

        rbRating.setRating(recipe.getRating());
        if (recipe.getDate() != null) {
            tvMyRcpDate.setText(recipe.getDate());
        }
        if (recipe.getMemo() != null) {
            tvMyRcpMemo.setText(recipe.getMemo());
        }
        if (recipe.getHashtag() != null) {
            tvMyRcpHashtag.setVisibility(View.VISIBLE);
            tvMyRcpHashtag.setText("#" + recipe.getHashtag());
        }
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        String imageAddress;

        public GetImageAsyncTask() {
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result;
            result = networkManager.downloadImage(imageAddress);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ivDetailMyRcpImg.setImageBitmap(bitmap);
            }
        }
    }
}
