package ddwu.mobile.finalproject.ma01_20180988;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "ProductDBHelper";
    final static String DB_NAME = "products.db";
    public final static String TABLE_NAME = "product_table";
    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name";
    public final static String COL_GOOD_ID = "goodId";
    public final static String COL_DETAIL = "detail";

    public ProductDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_NAME + " TEXT, " + COL_DETAIL + " TEXT, " + COL_GOOD_ID + " integer)";
        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
