package ddwu.mobile.finalproject.ma01_20180988;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

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
        tvMyRcpStep.setText(String.valueOf(recipe.getManuals().get(0).getStep()));

        tvDetailMyRcpName.setText(recipe.getName());
        if (recipe.getImageLink().contains("http"))  {
            new GetImageAsyncTask().execute(recipe.getImageLink());
        }
        else {
            setPic(ivDetailMyRcpImg, recipe.getImageLink());
        }

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

    public void kakaolink() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("나의 레시피 공유",
                        recipe.getImageLink(),
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption(recipe.getName())
                        .build())
                .addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl("'https://developers.kakao.com").setMobileWebUrl("'https://developers.kakao.com").build()))
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_recipe_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        kakaolink();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateRcp:
                Intent intent = new Intent(this, UpdateRecipeActivity.class);
                intent.putExtra("recipe", recipe);
                startActivity(intent);
                finish();
                break;
            case R.id.btnDeleteRcp:
                RecipeDBManager manager = new RecipeDBManager(this);
                manager.deleteRecipe(recipe.getRecipe_id());
                finish();
                break;
        }
    }

    private void setPic(ImageView view, String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(path, options);
        view.setImageBitmap(originalBm);
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
