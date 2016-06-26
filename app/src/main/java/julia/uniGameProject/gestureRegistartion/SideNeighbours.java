package julia.uniGameProject.gestureRegistartion;

import java.util.ArrayList;

/**
 * Created by julia on 23.06.16.
 */
public class SideNeighbours {
    private ArrayList<Neighbour> sideNeighbours = new ArrayList<>();

    public SideNeighbours(){
    }


    public void addNewNeighbour(Neighbour neighbour){
       this.sideNeighbours.add(neighbour);
    }

    public ArrayList<Neighbour> getSideNeihbours() {
        return sideNeighbours;
    }
}

