package ddwu.mobile.finalproject.ma01_20180988;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ProductDBManager {
    private static final String TAG = "ProductDBManager";
    ProductDBHelper productDBHelper = null;

    public ProductDBManager(Context context) {
        this.productDBHelper = new ProductDBHelper(context);
    }

    public boolean insertProductInfo(String name, int goodId) {
        SQLiteDatabase db = productDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(productDBHelper.COL_NAME, name);
        values.put(productDBHelper.COL_GOOD_ID, goodId);
        int id = (int) db.insert(productDBHelper.TABLE_NAME, null, values);
        if (id > 0) return true;
        return false;
    }
}
