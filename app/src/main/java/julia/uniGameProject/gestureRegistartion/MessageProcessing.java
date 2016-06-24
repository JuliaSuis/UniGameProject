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
    private MessageProcessing(){
    }

    static private ArrayList<SavedMessage> savedMessages = new ArrayList<SavedMessage>();

    public static void addSavedMessage(SavedMessage savedMessage){
        savedMessages.add(savedMessage);
    }

    public static void messageProcessing(StitchMessage stitchMessage) {
        int[] numAndSide = StitchEvaluations.getStitchNumAndSide(stitchMessage);
        boolean owner = StitchEvaluations.checkOwner(stitchMessage);
        if (numAndSide[0] == 1) {
            if (savedMessages.isEmpty()) {
                addSavedMessage(new SavedMessage(stitchMessage, numAndSide[0], owner));
            } else {
                if (savedMessages.get(savedMessages.size()).isStitchNum() == 1) {
                    savedMessages.clear();
                    addSavedMessage(new SavedMessage(stitchMessage, numAndSide[0], owner));
                } else {
                    Log.i(DEBUG_TAG, "??????????How about 1 NO??????????? ");
                }
            }
        } else if (numAndSide[0] == 2) {
              if (savedMessages.isEmpty()) {
                  Log.i(DEBUG_TAG, "??????????How about 2 NO??????????? ");
              } else {
                  if (owner == true) {
                      if (savedMessages.get(savedMessages.size()).getStitchMessage().getSendTime().equals(Calendar.DATE)) {
                          //compare times
                          // if time is ok
                          //create neighbour and add him
                      } else {
                            savedMessages.clear();
                          }
                      } else {
                      if (savedMessages.get(savedMessages.size()).isStitchOwner() == true) {
                          if (savedMessages.get(savedMessages.size()).getStitchMessage().getSendTime().equals(new Date())) {
                            //compare time
                              // create neighbour and add him
                          } else {
                              savedMessages.clear();
                          }
                      } else {
                          savedMessages.clear();
                      }
                  }
              }
        }
    }
}
