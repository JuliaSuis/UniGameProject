package julia.uniGameProject.gestureRegistartion;

import julia.connectivity.communication.StitchMessage;

/**
 * Created by julia on 23.06.16.
 */
public class SavedMessage {
    private StitchMessage stitchMessage;
    private int stitchNum;
    private boolean stitchOwner;

    public SavedMessage(){
    }

    public SavedMessage(StitchMessage stitchMessage, int stitchNum, boolean stitchOwner){
        this.stitchMessage = stitchMessage;
        this.stitchNum = stitchNum;
        this.stitchOwner = stitchOwner;
    }

    public StitchMessage getStitchMessage() {
        return stitchMessage;
    }

    public int isStitchNum() {
        return stitchNum;
    }

    public boolean isStitchOwner() {
        return stitchOwner;
    }

    public String getSavedMessage(){
        String s = new String();
        s = "Msg: " + stitchMessage.toString() + " Num: " + stitchNum + "; Owner: " + stitchOwner;
        return s;
    }
}
