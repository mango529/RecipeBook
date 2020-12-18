package ddwu.mobile.finalproject.ma01_20180988;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailActivity";

    ImageView ivDetailRcpImg;
    TextView tvDetailRcpName;
    ListView lvDetailIngre;
    ViewPager2 vpManual;

    ArrayAdapter ingredientAdapter;
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
        lvDetailIngre = findViewById(R.id.lvDetailIngre);
        vpManual = findViewById(R.id.vpManual);

        Log.d("goeun", "사이즈 " + recipe.getIngredients().size());
        ingredientAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipe.getIngredients());
        manualAdapter = new ManualAdapter(this, recipe.getManuals(), recipe.getMImageLinks());
        imageFileManager = new ImageFileManager(this);
        networkManager = new RecipeNetworkManager(this);

        lvDetailIngre.setAdapter(ingredientAdapter);
        tvDetailRcpName.setText(recipe.getName());
        vpManual.setAdapter(manualAdapter);

        Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(recipe.getImageLink());

        if (savedBitmap != null) {
            ivDetailRcpImg.setImageBitmap(savedBitmap);
            Log.d(TAG, "Image loading from file");
        } else {
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
