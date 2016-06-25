package julia.uniGameProject.gestureRegistartion;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import julia.connectivity.communication.StitchMessage;

/**
 * Created by julia on 24.06.16.
 */
public class MessageProcessing {
    private static final String DEBUG_TAG = MessageProcessing.class.getName();
    public static Neighbour neighbourForDraw = new Neighbour();

    public static Neighbour getNeighbourForDraw() {
        return neighbourForDraw;
    }

    private MessageProcessing(){
    }

    static private ArrayList<SavedMessage> savedMessages = new ArrayList<SavedMessage>();

    public static void addSavedMessage(SavedMessage savedMessage){
        savedMessages.add(savedMessage);
    }

    public static void messageProcessing(StitchMessage stitchMessage) {
        Log.i(DEBUG_TAG, "NEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW ");
        int[] numAndSide = StitchEvaluations.getStitchNumAndSide(stitchMessage);
        Log.i(DEBUG_TAG, "Side " + numAndSide[1]);
        boolean owner = StitchEvaluations.checkOwner(stitchMessage);
        Log.i(DEBUG_TAG, "Am I owner? " + owner);
        if (numAndSide[0] == 1) {
            if (savedMessages.isEmpty()) {
                addSavedMessage(new SavedMessage(stitchMessage, numAndSide[0], owner, numAndSide[1]));
                Log.i(DEBUG_TAG, "We've saved 1 stitch message and the weren't any previous ones");
            } else {
                if (savedMessages.get(savedMessages.size()-1).isStitchNum() == 1) {
                    savedMessages.clear();
                    addSavedMessage(new SavedMessage(stitchMessage, numAndSide[0], owner, numAndSide[1]));
                    Log.i(DEBUG_TAG, "We've saved 1 stitch message and cleaned previous");
                } else {
                    savedMessages.clear();
                    Log.i(DEBUG_TAG, "??????????How about 1 NO??????????? ");
                }
            }
        } else if (numAndSide[0] == 2) {
              if (savedMessages.isEmpty()) {
                  Log.i(DEBUG_TAG, "We've received 2 stitch message, but there are no previous 1 stitck messages Or First stitch was mine! Wrong! ");
              } else {
                  if (owner == true) {
                      if (StitchEvaluations.checkDelay(savedMessages.get(savedMessages.size()-1).getStitchMessage().getSendTime().getTime(), stitchMessage.getSendTime().getTime())) {
                          neighbourForDraw =  Neighbour.createNeighbour(savedMessages.get(savedMessages.size()-1), new SavedMessage(stitchMessage, numAndSide[0], owner, numAndSide[1]));
                          Log.i(DEBUG_TAG, "We've received my own 2 stitch message and delay is ok!  ");
                          savedMessages.clear();
                      } else {
                            savedMessages.clear();
                          Log.i(DEBUG_TAG, "We've received my own 2 stitch message but delay is NOT ok!  ");
                          }
                      } else {
                      if (savedMessages.get(savedMessages.size()-1).isStitchOwner() == true) {
                          if (StitchEvaluations.checkDelay(savedMessages.get(savedMessages.size()-1).getStitchMessage().getSendTime().getTime(), stitchMessage.getSendTime().getTime())) {
                              neighbourForDraw =  Neighbour.createNeighbour(savedMessages.get(savedMessages.size()-1), new SavedMessage(stitchMessage, numAndSide[0], owner, numAndSide[1]));
                              Log.i(DEBUG_TAG, "We've received 2 stitch message, the previous 1 stitch message was mine and time daelay is ok!  ");
                              savedMessages.clear();
                          } else {
                              savedMessages.clear();
                              Log.i(DEBUG_TAG, "We've received 2 stitch message, the previous 1 stitch message was mine and time daelay is NOT ok!  ");
                          }
                      } else {
                          savedMessages.clear();
                          Log.i(DEBUG_TAG, "The both messages are not mine ");
                      }
                  }
              }
        }
    }
}
