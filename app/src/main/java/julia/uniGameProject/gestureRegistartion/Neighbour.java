package julia.uniGameProject.gestureRegistartion;

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
