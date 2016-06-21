package julia.connectivity.communication;


/**
 * Created by julia on 20.06.16.
 */
public class CoordinateMessage extends BaseMessage {
    private double ordinate;
    private double abscissa;
    private int[] displayResolution;


    public CoordinateMessage(){

    }

    public CoordinateMessage(double ordinate, double abscissa, int[] display_resolution){
        this.ordinate = ordinate;
        this.abscissa = abscissa;
        this.displayResolution = display_resolution;
    }

    public double getOrdinate() {
        return ordinate;
    }

    public void setOrdinate(double ordinate) {
        this.ordinate = ordinate;
    }

    public double getAbscissa() {
        return abscissa;
    }

    public void setAbscissa(double abscissa) {
        this.abscissa = abscissa;
    }

    public int[] getDisplayResolution() {
        return displayResolution;
    }

    public void setDisplayResolution(int[] displayResolution) {
        this.displayResolution = displayResolution;
    }

/*    public String getCoordinateMessage(){
        return "ordinate = " + ordinate + ", abscissa = " + abscissa + "\n" +
                    "Display resolution: " + displayResolution[0] + "; " + displayResolution[1];
    } */
}
