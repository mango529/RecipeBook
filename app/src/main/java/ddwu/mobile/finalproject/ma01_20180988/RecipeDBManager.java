package ddwu.mobile.finalproject.ma01_20180988;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecipeDBManager {
    private static final String TAG = "RecipeDBManager";
    Context context;

    public RecipeDBManager(Context context) {
        this.context = context;
    }

    public boolean addNewRecipe(Recipe newRecipe) {
        RecipeDBHelper recipeDBHelper = new RecipeDBHelper(context);
        SQLiteDatabase db = recipeDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(RecipeDBHelper.COL_NAME, newRecipe.getName());
        value.put(RecipeDBHelper.COL_IMAGELINK, newRecipe.getImageLink());
        value.put(RecipeDBHelper.COL_MEMO, newRecipe.getMemo());
        value.put(RecipeDBHelper.COL_HASHTAG, newRecipe.getHashtag());
        value.put(RecipeDBHelper.COL_DATE, newRecipe.getDate());
        value.put(RecipeDBHelper.COL_RATING, newRecipe.getRating());

        int id = (int) db.insert(RecipeDBHelper.R_TABLE_NAME, null, value);

        for (Manual m : newRecipe.getManuals()) {
            value.clear();
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

        for (String s : newRecipe.getIngredients()) {
            value.clear();
            value.put(RecipeDBHelper.R_COL_ID, id);
            value.put(RecipeDBHelper.COL_NAME, s);
            db.insert(RecipeDBHelper.I_TABLE_NAME, null, value);
        }

        recipeDBHelper.close();
        if (id > 0) return true;
        return false;
    }

    public ArrayList<Recipe> getMyRecipe() {
        RecipeDBHelper recipeDBHelper = new RecipeDBHelper(context);
        SQLiteDatabase db = recipeDBHelper.getWritableDatabase();

        ArrayList<Recipe> result = new ArrayList<>();

        Cursor recipeCursor = db.rawQuery("SELECT * FROM recipe_table", null);

        while (recipeCursor.moveToNext()) {
            Recipe recipe = new Recipe();
            recipe.setRecipe_id(recipeCursor.getInt(recipeCursor.getColumnIndex(RecipeDBHelper.R_COL_ID)));
            recipe.setName(recipeCursor.getString(recipeCursor.getColumnIndex(RecipeDBHelper.COL_NAME)));
            recipe.setMemo(recipeCursor.getString(recipeCursor.getColumnIndex(RecipeDBHelper.COL_MEMO)));
            recipe.setHashtag(recipeCursor.getString(recipeCursor.getColumnIndex(RecipeDBHelper.COL_HASHTAG)));
            recipe.setDate(recipeCursor.getString(recipeCursor.getColumnIndex(RecipeDBHelper.COL_DATE)));
            recipe.setRating(recipeCursor.getInt(recipeCursor.getColumnIndex(RecipeDBHelper.COL_RATING)));
            recipe.setImageLink(recipeCursor.getString(recipeCursor.getColumnIndex(RecipeDBHelper.COL_IMAGELINK)));

            Cursor manualCursor = db.rawQuery("SELECT * FROM manual_table WHERE recipe_id=?;", new String[] {String.valueOf(recipe.getRecipe_id())});
            while (manualCursor.moveToNext()) {
                Manual manual = new Manual();
                manual.setStep(manualCursor.getInt(manualCursor.getColumnIndex(RecipeDBHelper.COL_STEP)));
                manual.setContent(manualCursor.getString(manualCursor.getColumnIndex(RecipeDBHelper.COL_CONTENT)));
                manual.setImageLink(manualCursor.getString(manualCursor.getColumnIndex(RecipeDBHelper.COL_IMAGELINK)));
                recipe.getManuals().add(manual);
            }
            manualCursor.close();

            Cursor ingredientCursor = db.rawQuery("SELECT * FROM ingredient_table WHERE recipe_id=?;", new String[] {String.valueOf(recipe.getRecipe_id())});
            while (ingredientCursor.moveToNext()) {
                recipe.getIngredients().add(ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeDBHelper.COL_NAME)));
            }
            result.add(recipe);
            ingredientCursor.close();
        }
        recipeCursor.close();
        recipeDBHelper.close();
        return result;
    }
}
