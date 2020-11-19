package ddwucom.mobile.recipebook;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ddwucom.mobile.recipebook.R;

public class FragmentRecipe extends Fragment {
    SearchView svRecipe;
    ListView lvSearchRecipe;
    ArrayList<Recipe> recipeList;
    ImageFileManager imgFileManager;
    RecipeAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        svRecipe = view.findViewById(R.id.svRecipe);
        lvSearchRecipe = view.findViewById(R.id.lvSearchRecipe);

        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), R.layout.recipe_adapter_view, recipeList);
        lvSearchRecipe.setAdapter(adapter);



        imgFileManager = new ImageFileManager(getContext());

        svRecipe.setQueryHint("요리 이름을 입력하세요.");
        int id = svRecipe.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.notosanskr_regular);
        TextView searchText = (TextView) svRecipe.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setIncludeFontPadding(false);

        // 검색 기능 !
//        svRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.d("goeun", query);
//adapter.setList(recipeList);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imgFileManager.clearTemporaryFiles();
    }


}