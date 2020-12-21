package ddwu.mobile.finalproject.ma01_20180988;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private static final String TAG = "Product";

    private int _id;
    private String name;
    private int goodId;
    private String detail;
    private List<Store> storeList;

    public Product() {
        storeList = new ArrayList<>();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }
}
