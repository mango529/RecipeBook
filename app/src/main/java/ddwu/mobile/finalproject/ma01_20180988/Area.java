package ddwu.mobile.finalproject.ma01_20180988;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Area {
    private static final String TAG = "Area";

    private int code;
    private String name;
    private List<Area> detailAreas;

    public Area() {
        detailAreas = new ArrayList<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Area> getDetailAreas() {
        return detailAreas;
    }

    public void setDetailAreas(List<Area> detailAreas) {
        this.detailAreas = detailAreas;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
