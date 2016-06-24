package julia.connectivity.communication;

/**
 * Created by julia on 23.06.16.
 */
public class StitchMessage extends BaseMessage{
    private double xStart;
    private double yStart;
    private double xEnd;
    private double yEnd;
    private int[] displayResolution;

    public StitchMessage(){

    }

    public StitchMessage(double xStart, double yStart, double xEnd, double yEnd, int[] displayResolution){
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        this.displayResolution = displayResolution;
    }

    public double getxStart() {
        return xStart;
    }

    public void setxStart(double xStart) {
        this.xStart = xStart;
    }

    public double getyStart() {
        return yStart;
    }

    public void setyStart(double yStart) {
        this.yStart = yStart;
    }

    public double getxEnd() {
        return xEnd;
    }

    public void setxEnd(double xEnd) {
        this.xEnd = xEnd;
    }

    public double getyEnd() {
        return yEnd;
    }

    public void setyEnd(double yEnd) {
        this.yEnd = yEnd;
    }

    public int[] getDisplayResolution() {
        return displayResolution;
    }

    public void setDisplayResolution(int[] displayResolution) {
        this.displayResolution = displayResolution;
    }
}



