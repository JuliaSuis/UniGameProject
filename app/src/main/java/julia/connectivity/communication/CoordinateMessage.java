package julia.connectivity.communication;


/**
 * Created by julia on 20.06.16.
 */
public class CoordinateMessage implements Message {
    private double ordinate;
    private double abscissa;

    public CoordinateMessage(){

    }

    public CoordinateMessage(double ordinate, double abscissa){
        this.ordinate = ordinate;
        this.abscissa = abscissa;
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

    public String getCoordinateMessage(){
        return ("ordinate = " + ordinate + ", abscissa = " + abscissa).toString();
    }
}
