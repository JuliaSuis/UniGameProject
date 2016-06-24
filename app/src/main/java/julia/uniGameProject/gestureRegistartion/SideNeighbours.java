package julia.uniGameProject.gestureRegistartion;

import java.util.ArrayList;

/**
 * Created by julia on 23.06.16.
 */
public class SideNeighbours {
    private ArrayList<Neighbour> sideNeighbours;
    int side;

    public SideNeighbours(){
    }

    public void addNewNeighbour(Neighbour neighbour){
        sideNeighbours.add(neighbour);
    }

    public int getSide() {
        return side;
    }

    public ArrayList<Neighbour> getSideNeihbours() {
        return sideNeighbours;
    }
}

