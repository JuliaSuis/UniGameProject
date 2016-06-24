package julia.uniGameProject.gestureRegistartion;

import java.util.ArrayList;

import julia.connectivity.communication.StitchMessage;

/**
 * Created by julia on 23.06.16.
 */
public class Neighbour {
    String id;
    double xUp;
    double yUp;
    double xDown;
    double yDown;

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

        if (side == 3 || side == 1) {
            if (y1s <= y2s) yUp = 0;
            else yUp = y1s - y2s;
            if (myResolution[1] - y1s <= secondResolution[1] - y2s) yDown = 0;
            else yDown = y1s + secondResolution[1] - y2s;
            if (firstMsg.isStitchOwner()) {
                xUp = myResolution[0];
                xDown = myResolution[0];
            } else if (secondMsg.isStitchOwner()) {
                xUp = 0;
                xDown = 0;
            }
        } else if (side == 2 || side == 0) {
            if (x1s <= x2s) xUp = 0;
            else xUp = x1s - x2s;
            if (myResolution[0] - x1s <= secondResolution[0] - x2s) xDown = 0;
            else xDown = x1s + secondResolution[0] - x2s;
            if (firstMsg.isStitchOwner()) {
                yUp = myResolution[0];
                yDown = myResolution[0];
            } //stitch from up to down
            else if (secondMsg.isStitchOwner()) {
                yUp = 0;
                yDown = 0;
            }
        }
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
