package lar.com.lookaround.util;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Attila_Dan on 16. 05. 11..
 */
public class AddImageUtil {
    private int id;
    private Bitmap bitmap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public AddImageUtil(int id, Bitmap bitmap) {
        this.id = id;
        this.bitmap = bitmap;
    }

    ArrayList<AddImageUtil> images = new ArrayList<AddImageUtil>();

    public void addImage(int id, Bitmap bitmap) {
        images.add(new AddImageUtil(id, bitmap));
    }
}
