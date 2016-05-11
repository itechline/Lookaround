package lar.com.lookaround.util;

import android.graphics.Bitmap;
import android.util.Log;

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
    private static ArrayList<AddImageUtil> images = new ArrayList<AddImageUtil>();

    public static void addImage(int id, Bitmap bitmap) {
        Log.d("IMAGE_", "ADDIMAGEUTIL");
        images.add(new AddImageUtil(id, bitmap));
    }

    public static ArrayList<AddImageUtil> getAllImages() {
        return images;
    }
}
