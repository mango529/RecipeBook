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
        value.put(recipeDBHelper.COL_NAME, newRecipe.getName());
        value.put(recipeDBHelper.COL_TYPE, newRecipe.getType());
        value.put(recipeDBHelper.COL_CAL, newRecipe.getCal());
        value.put(recipeDBHelper.COL_IMAGELINK, newRecipe.getImageLink());
        value.put(recipeDBHelper.COL_INGRE, newRecipe.getIngredient());

        int id = (int) db.insert(RecipeDBHelper.R_TABLE_NAME, null, value);

        for (int i = 0; i < newRecipe.getManuals().size(); i ++) {
            value = new ContentValues();
            value.put(recipeDBHelper.R_COL_ID, id);
            Log.d(TAG, "recipe id " + id);
            value.put(recipeDBHelper.COL_STEP, i + 1);
            Log.d(TAG, "COL_STEP " + (i + 1));
            value.put(recipeDBHelper.COL_CONTENT, newRecipe.getManuals().get(i));
            Log.d(TAG, "COL_CONTENT " +newRecipe.getManuals().get(i) );
            if (newRecipe.getMImageLinks().containsKey(i + 1)) {
                value.put(recipeDBHelper.COL_IMAGELINK, newRecipe.getMImageLinks().get(i + 1));
                Log.d(TAG, "COL_IMAGELINK " + newRecipe.getMImageLinks().get(i + 1));
            }
            db.insert(RecipeDBHelper.M_TABLE_NAME, null, value);
        }

        if (id > 0) return true;
        return false;
    }
}
