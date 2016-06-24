package julia.uniGameProject.gestureRegistartion;

import java.util.ArrayList;

/**
 * Created by julia on 23.06.16.
 */
public class SideNeighbours {
    private ArrayList<Neighbour> sideNeighbours;

    public SideNeighbours(){
    }

    public void AddNeighbour(Neighbour neighbour){
        sideNeighbours.add(neighbour);
    }

    public ArrayList<Neighbour> getSideNeihbours() {
        return sideNeighbours;
    }
}

