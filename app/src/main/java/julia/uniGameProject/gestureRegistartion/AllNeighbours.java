package julia.uniGameProject.gestureRegistartion;

import java.util.ArrayList;

/**
 * Created by julia on 23.06.16.
 */
public class AllNeighbours {

    private SideNeighbours[] allNeighbours = new SideNeighbours[4];

    public AllNeighbours(){
    }

    public AllNeighbours (SideNeighbours down, SideNeighbours left, SideNeighbours up, SideNeighbours right) {
        this.allNeighbours[0] = down;
        this.allNeighbours[1] = left;
        this.allNeighbours[2] = up;
        this.allNeighbours[3] = right;
    }




}
