package julia.uniGameProject.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import julia.uniGameProject.R;

/**
 * Created by julia on 10.07.16.
 */
public class Scene {
    private Bitmap bitmap_hor;
    private Bitmap bitmap_ver;
    private Bitmap resized_hor_bitmap;
    private Bitmap resized_ver_bitmap;
    private int verX,verY,horX,horY;

    int width=200;
    int height=15;


    public Bitmap getBitmap_hor() {
        return bitmap_hor;
    }

    public Bitmap getBitmap_ver() {
        return bitmap_ver;
    }

    public Scene(Context context){
        bitmap_hor= BitmapFactory.decodeResource(context.getResources(), R.drawable.border_horizantal);
        bitmap_ver= BitmapFactory.decodeResource(context.getResources(), R.drawable.border_vertical);


    }

    public Bitmap getBitmap_hor(int d_width, int d_height){
        resized_hor_bitmap=Bitmap.createScaledBitmap(bitmap_hor, d_width, (int)(d_height*0.009), true);
        return resized_hor_bitmap;
    }

    public Bitmap getBitmap_ver(int d_height,int d_width){
        resized_ver_bitmap=Bitmap.createScaledBitmap(bitmap_ver, (int)(d_width*0.011), d_height, true);
        return resized_ver_bitmap;
    }

    public int getVerX() {
        return verX;
    }

    public int getVerY() {
        return verY;
    }

    public int getHorX() {
        return horX;
    }

    public int getHorY() {
        return horY;
    }

    public void setVerX(int verX) {
        this.verX = verX;
    }

    public void setVerY(int verY) {
        this.verY = verY;
    }

    public void setHorX(int horX) {
        this.horX = horX;
    }

    public void setHorY(int horY) {
        this.horY = horY;
    }
}
