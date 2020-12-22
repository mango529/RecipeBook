package ddwu.mobile.finalproject.ma01_20180988;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyRecipeActivity extends AppCompatActivity {
    private static final String TAG = "MyRecipeActivity";
    private ListView lvMyRecipe;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvMyRecipe = findViewById(R.id.lvMyRecipe);
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, R.layout.recipe_adapter_view, recipeList);
    }
}
