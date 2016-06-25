package julia.uniGameProject.gestureRegistartion;

import android.util.Log;
import android.view.Display;

import java.util.ArrayList;

import julia.connectivity.communication.StitchMessage;
import julia.uniGameProject.io.CustomClientActionListener;

/**
 * Created by julia on 23.06.16.
 */
public class Neighbour {
    String id;
    double xUp;
    double yUp;
    double xDown;
    double yDown;

    private static final String DEBUG_TAG = Neighbour.class.getName();


    public Neighbour(){
    }

    public Neighbour(String id, double xUp, double yUp, double xDown, double yDown){
        this.id = id;
        this.xUp = xUp;
        this.yUp = yUp;
        this.xDown = xDown;
        this.yDown = yDown;
    }

    // x1s,y1s - last coord of 1st stitch, x2s,y2s - first coord of 2nd stitch
    public static Neighbour createNeighbour(SavedMessage firstMsg, SavedMessage secondMsg) {

        double xUp=0, yUp=0, xDown=0, yDown=0;
        String idNeighbour;
        double x1s = firstMsg.getStitchMessage().getxEnd();
        double y1s = firstMsg.getStitchMessage().getyEnd();
        double x2s = secondMsg.getStitchMessage().getxStart();
        double y2s = secondMsg.getStitchMessage().getyStart();

        Log.i(DEBUG_TAG, "x1: " + x1s + "y1: " + y1s + "x2: " + x2s + "y2: " + y2s);

        int[] myResolution = new int[2];
        int[] secondResolution = new int[2];
        int side;
        if (firstMsg.isStitchOwner()) {
            side = firstMsg.getSide();
            myResolution = firstMsg.getStitchMessage().getDisplayResolution();
            secondResolution = secondMsg.getStitchMessage().getDisplayResolution();
            idNeighbour = secondMsg.getStitchMessage().getClientId();
        } else {
            side = secondMsg.getSide();
            secondResolution = firstMsg.getStitchMessage().getDisplayResolution();
            myResolution = secondMsg.getStitchMessage().getDisplayResolution();
            idNeighbour = firstMsg.getStitchMessage().getClientId();
        }
        //myResolution[0] - my width (prev.xMax)
        //myResolution[1] - my height (prev.yMax)
        //secondResolution[0] - second width (prev.x2max)
        //secondResolution[1] - second height (prev.y2max)



        if (side == 2) {
            if (x1s <= x2s) xUp = 0;
            else xUp = x1s - x2s;
            if (myResolution[0] - x1s <= secondResolution[0] - x2s) xDown = myResolution[0];
            else xDown = x1s + secondResolution[0] - x2s;
            //   if (firstMsg.isStitchOwner()) {
        }

        if (side == 0) {
            if (x2s <= x1s) xUp = 0;
            else xUp = x2s - x1s;
            if (myResolution[0] - x2s <= secondResolution[0] - x1s) xDown = myResolution[0];
            else xDown = x2s + myResolution[0] - x1s;
            //   if (firstMsg.isStitchOwner()) {
        }
        if (side == 0) {
            yUp = myResolution[1];
            yDown = myResolution[1];
        }
        //   else if (secondMsg.isStitchOwner()) {
        else if (side == 2) {
            yUp = 20;
            yDown = 20;
        }

        if (side == 3) {
            if (y1s <= y2s) yUp = 0;
            else yUp = y1s - y2s;
            if (myResolution[1] - y1s <= secondResolution[1] - y2s) yDown = myResolution[1];
            else yDown = y1s + secondResolution[1] - y2s;
            //   if (firstMsg.isStitchOwner()) {
            }

            if (side == 1) {
                if (y2s <= y1s) yUp = 0;
                else yUp = y2s - y1s;
                if (myResolution[1] - y2s <= secondResolution[1] - y1s) yDown = myResolution[1];
                else yDown = y2s + myResolution[1] - y1s;
                //   if (firstMsg.isStitchOwner()) {
            }

        if (side == 3) {
            xUp = myResolution[0];
            xDown = myResolution[0];
        }
        //   else if (secondMsg.isStitchOwner()) {
        else if (side == 1) {
            xUp = 0;
            xDown = 0;
        }


        Log.i(DEBUG_TAG, "x1: " + x1s + "y1: " + y1s + "x2: " + x2s + "y2: " + y2s);

        Neighbour newNeighbour = new Neighbour(idNeighbour, xUp, yUp, xDown, yDown);
        return newNeighbour;
    }

    public String getId() {
        return id;
    }

    public double getxUp() {
        return xUp;
    }

    public double getyUp() {
        return yUp;
    }

    public double getxDown() {
        return xDown;
    }

    public double getyDown() {
        return yDown;
    }
}
