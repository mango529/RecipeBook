package ddwu.mobile.finalproject.ma01_20180988;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FragmentRecipe extends Fragment {
    SearchView svRecipe;
    ListView lvSearchRecipe;
    ArrayList<Recipe> recipeList;
    ImageFileManager imgFileManager;
    RecipeAdapter adapter;
    String apiAddress;
    RecipeXmlParser parser;
    RecipeNetworkManager networkManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        svRecipe = view.findViewById(R.id.svRecipe);
        lvSearchRecipe = view.findViewById(R.id.lvSearchRecipe);

        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), R.layout.recipe_adapter_view, recipeList);
        lvSearchRecipe.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.recipe_api_url);
        parser = new RecipeXmlParser();
        networkManager = new RecipeNetworkManager(getContext());

        imgFileManager = new ImageFileManager(getContext());

        svRecipe.setQueryHint("요리 이름을 입력하세요.");
        int id = svRecipe.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.notosanskr_regular);
        TextView searchText = (TextView) svRecipe.findViewById(id);
        searchText.setTypeface(tf);
        searchText.setIncludeFontPadding(false);

        svRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("goeun", query);
                try {
                    new NetworkAsyncTask().execute(apiAddress
                            + URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                svRecipe.setQuery("", false);
                svRecipe.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        lvSearchRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                intent.putExtra("recipe", recipeList.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imgFileManager.clearTemporaryFiles();
    }

    class NetworkAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDlg = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);

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

            recipeList = parser.parse(result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("goeun", result);
            adapter.setList(recipeList);
            progressDlg.dismiss();
        }
    }
}