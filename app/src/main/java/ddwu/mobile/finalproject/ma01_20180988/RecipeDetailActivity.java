package ddwu.mobile.finalproject.ma01_20180988;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    NetworkManager networkManager;
    Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        ivDetailRcpImg = findViewById(R.id.ivDetailRcpImg);
        tvDetailRcpName = findViewById(R.id.tvDetailRcpName);
        lvDetailIngre = findViewById(R.id.lvDetailIngre);
        vpManual = findViewById(R.id.vpManual);

        ingredientAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipe.getIngredients());
        manualAdapter = new ManualAdapter(this, recipe.getManuals());
        imageFileManager = new ImageFileManager(this);
        networkManager = new NetworkManager(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        RecipeDBManager manager = new RecipeDBManager(this);
        manager.addNewRecipe(recipe);
        return true;
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
