package ddwu.mobile.finalproject.ma01_20180988;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private int recipe_id;
    private String name;
    private String memo;
    private String hashtag;
    private String date;
    private float rating;
    private String imageLink;
    private ArrayList<String>  ingredients;
    private ArrayList<Manual> manuals;
    private String type;

    public Recipe() {
        ingredients = new ArrayList<>();
        manuals = new ArrayList<>();
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<Manual> getManuals() {
        return manuals;
    }

    public void setManuals(ArrayList<Manual> manuals) {
        this.manuals = manuals;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
