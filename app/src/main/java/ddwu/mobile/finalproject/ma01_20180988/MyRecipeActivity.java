package ddwu.mobile.finalproject.ma01_20180988;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyRecipeActivity extends AppCompatActivity {
    private static final String TAG = "MyRecipeActivity";
    private ListView lvMyRecipe;
    private SearchView svMyRecipe;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipeList;
    private ArrayList<Recipe> recipeAll;
    private RecipeDBManager manager;
    private FloatingActionButton fabNewRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        manager = new RecipeDBManager(this);
        recipeList = new ArrayList<>();
        recipeAll = new ArrayList<>();
        fabNewRecipe = findViewById(R.id.fabNewRecipe);
        lvMyRecipe = findViewById(R.id.lvMyRecipe);
        svMyRecipe = findViewById(R.id.svMyRecipe);
        recipeAdapter = new RecipeAdapter(this, R.layout.recipe_adapter_view, recipeList);
        lvMyRecipe.setAdapter(recipeAdapter);

        lvMyRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyRecipeActivity.this, MyRecipeDetailActivity.class);
                intent.putExtra("recipe", recipeList.get(position));
                startActivity(intent);
            }
        });

        fabNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRecipeActivity.this, NewRecipeActivity.class);
                startActivity(intent);
            }
        });

        svMyRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
    }

    public void search(String searchText) {
        recipeList.clear();

        if (searchText.length() == 0) {
            recipeList.addAll(recipeAll);
        } else {
            recipeList.addAll(manager.getMyRecipeByNameNHashtag(searchText));
        }
        recipeAdapter.setList(recipeList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recipeList.clear();
        recipeAll.clear();

        recipeList.addAll(manager.getMyRecipe());
        recipeAll.addAll(recipeList);

        recipeAdapter.setList(recipeList);
    }
}
