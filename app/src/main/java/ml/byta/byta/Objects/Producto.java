package ml.byta.byta.Objects;

import android.graphics.Bitmap;

/**
 * Created by ibai on 11/4/17.
 */

public class Producto {

    private Bitmap bitmap;
    private String description;
    private String location;
    private int id;

    public Producto(Bitmap bitmap, String description, String location, int id) {
        this.bitmap = bitmap;
        this.description = description;
        this.location = location;
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


