package julia.uniGameProject.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import julia.uniGameProject.R;

/**
 * Created by julia on 10.07.16.
 */
public class Ball {
    private Bitmap bitmap;
    private int x,y;
    private int speedX=25;
    private int speedY=25;
    private int r;
    private Bitmap resized_bitmap;



    public Ball(Context context){

        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.ball2);

    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void update(){

        this.x=this.x+speedX;
        this.y=this.y+speedY;

    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public Bitmap getBitmap(int d_height,int d_width){
        resized_bitmap=Bitmap.createScaledBitmap (bitmap, (int)(d_width*0.05), (int)(d_height*0.025), true);
        return resized_bitmap;
    }

    public int getRadius(int d_height,int d_width){
        r=getBitmap(d_height,d_width).getWidth()/2;
        return r;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


}