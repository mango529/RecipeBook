package ddwu.mobile.finalproject.ma01_20180988;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProductDBManager {
    private static final String TAG = "ProductDBManager";
    ProductDBHelper productDBHelper = null;

    public ProductDBManager(Context context) {
        this.productDBHelper = new ProductDBHelper(context);
    }

    public boolean insertProductInfo(Product product) {
        SQLiteDatabase db = productDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(productDBHelper.COL_NAME, product.getName());
        values.put(productDBHelper.COL_GOOD_ID, product.getGoodId());
        int id = (int) db.insert(productDBHelper.TABLE_NAME, null, values);
        if (id > 0) return true;
        return false;
    }

    public Cursor findProductsByName(String name) {
        Cursor cursor;
        SQLiteDatabase db = productDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + productDBHelper.TABLE_NAME + " where name like '%" + name + "%';", null);
        return cursor;
    }
}
