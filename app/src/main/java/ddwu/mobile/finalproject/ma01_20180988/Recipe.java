package ddwu.mobile.finalproject.ma01_20180988;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Recipe implements Serializable {
    private int recipe_id;
    private String name;
    private String type;
    private int cal;
    private String imageLink;
    private ArrayList<String>  ingredients;
    private ArrayList<String> manuals;
    private HashMap<Integer, String> mImageLinks;

    public Recipe() {
        ingredients = new ArrayList<>();
        manuals = new ArrayList<>();
        mImageLinks = new HashMap<>();
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
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

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getManuals() {
        return manuals;
    }

    public void setManuals(ArrayList<String> manuals) {
        this.manuals = manuals;
    }

    public HashMap<Integer, String> getMImageLinks() {
        return mImageLinks;
    }

    public void setMImageLinks(HashMap<Integer, String> mImageLinks) {
        this.mImageLinks = mImageLinks;
    }
}
