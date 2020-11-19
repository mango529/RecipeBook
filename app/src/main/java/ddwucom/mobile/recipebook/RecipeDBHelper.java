package ddwucom.mobile.recipebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecipeDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "RecipeDBHelper";
    final static String DB_NAME = "recipes.db";
    public final static String R_TABLE_NAME = "recipe_table";
    public final static String R_COL_ID = "recipe_id";
    public final static String COL_NAME = "name";
    public final static String COL_TYPE = "type";
    public final static String COL_CAL = "cal";
    public final static String COL_IMAGELINK = "imagelink";
    public final static String COL_INGRE = "ingredient";
    public final static String M_TABLE_NAME = "manual_table";
    public final static String M_COL_ID = "manual_id";
    public final static String COL_STEP = "step";
    public final static String COL_CONTENT = "content";

    public RecipeDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + R_TABLE_NAME + " (" + R_COL_ID + " integer primary key autoincrement, " +
                COL_NAME + " TEXT, " + COL_TYPE + " TEXT, " + COL_CAL + " integer, " + COL_IMAGELINK + " TEXT, " +
                COL_INGRE + " TEXT)";
        Log.d(TAG, sql);
        db.execSQL(sql);
        sql = "CREATE TABLE " + M_TABLE_NAME + " (" + M_COL_ID + " integer primary key autoincrement, " + R_COL_ID + " integer, " +
                COL_STEP + " integer, " + COL_CONTENT + " TEXT, " +  COL_IMAGELINK + " integer, FOREIGN KEY(" + R_COL_ID + ") REFERENCES "
                + R_TABLE_NAME + "(" +  R_COL_ID + ") ON DELETE CASCADE)" ;
        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + R_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + M_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
