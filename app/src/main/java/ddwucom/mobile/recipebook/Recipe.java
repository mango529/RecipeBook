package ddwucom.mobile.recipebook;

import java.util.ArrayList;

public class Recipe {
    private int _id;
    private String name;
    private String type;
    private int cal;
    private String imageLink;
    private String ingredient;
    private ArrayList<String> manuals;

    public Recipe() {
        manuals = new ArrayList<>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public ArrayList<String> getManuals() {
        return manuals;
    }

    public void setManuals(ArrayList<String> manuals) {
        this.manuals = manuals;
    }
}
