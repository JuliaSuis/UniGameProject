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


    public PlayerRacket(Context context){
        x=0;
        y=0;
        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.racket);
    }

    public void update(){

    }
    //Getters
    public Bitmap getBitmap(int d_height,int d_width){

        resized_bitmap=Bitmap.createScaledBitmap(bitmap, (int)(d_width*0.2), (int)(d_height*0.023), true);
        return resized_bitmap;
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

}
