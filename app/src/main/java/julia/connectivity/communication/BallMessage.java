package julia.connectivity.communication;

/**
 * Created by julia on 10.07.16.
 */
public class BallMessage extends BaseMessage {
    private int speed;
    private int height;
    private int width;
    private String idNeighbour;

    public BallMessage(){
    }

    public BallMessage(int speed, int height, int width, String idNeighbour){
        this.speed = speed;
        this.height = height;
        this.width = width;
        this.idNeighbour = idNeighbour;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth(){
        return width;
    }

    public String getIdNeighbour() {
        return idNeighbour;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setIdNeighbour(String idNeighbour) {
        this.idNeighbour = idNeighbour;
    }
}
