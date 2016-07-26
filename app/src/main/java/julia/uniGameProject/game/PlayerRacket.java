package julia.uniGameProject.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import julia.uniGameProject.R;

/**
 * Created by julia on 10.07.16.
 */
public class PlayerRacket {
    private Bitmap bitmap;
    private Bitmap resized_bitmap;
    private int x,y;
    private int lives;
    private Bitmap livesBitmap;
    private Bitmap resizedHeart;
    private int r;


    public PlayerRacket(Context context){
        x=0;
        y=0;
        lives=3;
        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.racket2);
        livesBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.heart);

    }

    public void update(){

    }
    //Getters
    public Bitmap getBitmap(int d_height,int d_width){

        resized_bitmap=Bitmap.createScaledBitmap(bitmap, (int)(d_width*0.14), (int)(d_height*0.22), true);
        return resized_bitmap;
    }

    public int getRadius(int d_height,int d_width){

        int r = getBitmap(d_height,d_width).getWidth()/2;
        return r;
    }

    public Bitmap getLivesBitmap(int d_height,int d_width){
        resizedHeart=Bitmap.createScaledBitmap(livesBitmap,(int)(d_width*0.04),(int)(d_height*0.07),true);
        return resizedHeart;

    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int X){
        this.x=X;
    }

    public void setY(int Y){
        this.y = Y;
    }

    public void verticalMov(){

    }

    public int getLives(){
        return lives;
    }
    public void setLives(){

        lives= lives -1;
    }

}