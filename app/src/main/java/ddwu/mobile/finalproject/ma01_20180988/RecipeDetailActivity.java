package ddwu.mobile.finalproject.ma01_20180988;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailActivity";

    ImageView ivDetailRcpImg;
    TextView tvDetailRcpName;
    TextView tvDetailRcpIng;
    ViewPager2 vpManual;

    ManualAdapter manualAdapter;
    ImageFileManager imageFileManager;
    RecipeNetworkManager networkManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        ivDetailRcpImg = findViewById(R.id.ivDetailRcpImg);
        tvDetailRcpName = findViewById(R.id.tvDetailRcpName);
        tvDetailRcpIng = findViewById(R.id.tvDetailRcpIng);
        vpManual = findViewById(R.id.vpManual);

        manualAdapter = new ManualAdapter(this, recipe.getManuals(), recipe.getMImageLinks());
        imageFileManager = new ImageFileManager(this);
        networkManager = new RecipeNetworkManager(this);

        tvDetailRcpName.setText(recipe.getName());
        tvDetailRcpIng.setText(recipe.getIngredient());
        vpManual.setAdapter(manualAdapter);

        Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(recipe.getImageLink()); // 파일 이름만을 잘라 확인

        if (savedBitmap != null) {
            ivDetailRcpImg.setImageBitmap(savedBitmap);
            Log.d(TAG, "Image loading from file");
        } else {
            ivDetailRcpImg.setImageResource(R.mipmap.ic_launcher);
            new GetImageAsyncTask().execute(recipe.getImageLink());
            Log.d(TAG, "Image loading from network");
        }
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        String imageAddress;

        public GetImageAsyncTask() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result = null;
            result = networkManager.downloadImage(imageAddress);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ivDetailRcpImg.setImageBitmap(bitmap);
                imageFileManager.saveBitmapToTemporary(bitmap, imageAddress); // imageAddress로 filename 만듦
            }
        }
    }
}
