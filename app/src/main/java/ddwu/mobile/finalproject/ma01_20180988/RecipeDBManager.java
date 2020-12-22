package ddwu.mobile.finalproject.ma01_20180988;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecipeDBManager {
    private static final String TAG = "RecipeDBManager";
    RecipeDBHelper recipeDBHelper = null;

    public RecipeDBManager(Context context) {
        this.recipeDBHelper = new RecipeDBHelper(context);
    }

    public boolean addNewRecipe(Recipe newRecipe) {
        SQLiteDatabase db = recipeDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(RecipeDBHelper.COL_NAME, newRecipe.getName());
        value.put(RecipeDBHelper.COL_IMAGELINK, newRecipe.getImageLink());
        value.put(RecipeDBHelper.COL_MEMO, newRecipe.getMemo());
        value.put(RecipeDBHelper.COL_HASHTAG, newRecipe.getHashtag());
        value.put(RecipeDBHelper.COL_DATE, newRecipe.getDate());
        value.put(RecipeDBHelper.COL_RATING, newRecipe.getRating());

        int id = (int) db.insert(RecipeDBHelper.R_TABLE_NAME, null, value);

        value.clear();
        for (Manual m : newRecipe.getManuals()) {
            value.put(RecipeDBHelper.R_COL_ID, id);
            Log.d(TAG, "recipe id " + id);
            value.put(RecipeDBHelper.COL_STEP, m.getStep());
            Log.d(TAG, "COL_STEP " + m.getStep());
            value.put(RecipeDBHelper.COL_CONTENT, m.getContent());
            Log.d(TAG, "COL_CONTENT " + m.getContent());
            if (m.getImageLink() != null) {
                value.put(RecipeDBHelper.COL_IMAGELINK, m.getImageLink());
                Log.d(TAG, "COL_IMAGELINK " + m.getImageLink());
            }
            db.insert(RecipeDBHelper.M_TABLE_NAME, null, value);
        }

        value.clear();
        for (String s : newRecipe.getIngredients()) {
            value.put(RecipeDBHelper.R_COL_ID, id);
            value.put(RecipeDBHelper.COL_NAME, s);
            db.insert(RecipeDBHelper.I_TABLE_NAME, null, value);
        }

        if (id > 0) return true;
        return false;
    }
}
