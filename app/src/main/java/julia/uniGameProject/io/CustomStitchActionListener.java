package julia.uniGameProject.io;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import julia.connectivity.client.ClientActionListener;
import julia.connectivity.communication.Message;
import julia.connectivity.communication.StatusMessage;
import julia.connectivity.communication.StitchMessage;
import julia.connectivity.serialization.Parser;
import julia.uniGameProject.R;
import julia.uniGameProject.SampleApplication;
import julia.uniGameProject.gestureRegistartion.SavedMessage;
import julia.uniGameProject.gestureRegistartion.StitchEvaluations;

/**
 * Created by julia on 20.06.16.
 */
public class CustomStitchActionListener implements ClientActionListener {
    private static final String DEBUG_TAG = CustomClientActionListener.class.getName();
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    static public String myIp;

    private void setMyIp(String myIp) {
        this.myIp = myIp;
    }

    private List<String> cachedMessages = new ArrayList<>();

    private ArrayList<SavedMessage> savedMessages = new ArrayList<SavedMessage>();

    //public ArrayList<SavedMessage> getStitchMessages() {
    //    return savedMessages;
    //}

    public void addSavedMessage(SavedMessage savedMessage){
        this.savedMessages.add(savedMessage);
    }



    public void onReceive(final String msg) {
        try {
            Log.i(DEBUG_TAG, "Received message from server: " + msg);
            final Activity current = SampleApplication.getActivity();
            final Message message = Parser.parse(msg, Message.class);
            if (message instanceof StatusMessage) {
                final StatusMessage status = ((StatusMessage) message);
                current.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SampleApplication.getActivity().getApplication(),
                                String.format("Your connection status: [%s]", status.getStatus()),
                                Toast.LENGTH_SHORT).show();
                                setMyIp(message.getClientId());

                            current.findViewById(R.id.button_send_class_message).setEnabled(true);
                    }
                });
            }  else if (message instanceof StitchMessage) {
                final StitchMessage stitchMessage = ((StitchMessage) message);
                final TextView serverOut = (TextView) current.findViewById(R.id.list_view_coordinate_receive);
                if (serverOut == null) {
                    cachedMessages.add(String.format("[%s ; %s]: xStart, yStart: %s , %s ; xEnd, y End: %s, %s " +
                                    "Display resolution:  %s", formatTime(stitchMessage.getSendTime()), stitchMessage.getClientId(),
                            stitchMessage.getxStart(), stitchMessage.getyStart(), stitchMessage.getxEnd(), stitchMessage.getyEnd(),
                            Arrays.toString(stitchMessage.getDisplayResolution())));
                            //check owner(0-1) and number of stitch (0-1)
                            int stitchNum = StitchEvaluations.getStitchNumAndSide(stitchMessage)[0];
                            boolean owner = StitchEvaluations.checkOwner(stitchMessage);
                            SavedMessage msg1 = new SavedMessage(stitchMessage, stitchNum, owner);
                            addSavedMessage(msg1);
                            Log.i(DEBUG_TAG, "Saved1 message to savedMessages " + msg1.getSavedMessage());
                } else {
                    current.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!cachedMessages.isEmpty()) {
                                for (String cachedMessage : cachedMessages) {
                                    serverOut.append(cachedMessage);
                                    serverOut.append("\n");
                                }
                            }
                            serverOut.append(String.format("[%s ; %s]: xStart, yStart: %s , %s ; xEnd, y End: %s, %s " +
                                            "Display resolution:  %s", formatTime(stitchMessage.getSendTime()), stitchMessage.getClientId(),
                                    stitchMessage.getxStart(), stitchMessage.getyStart(), stitchMessage.getxEnd(), stitchMessage.getyEnd(),
                                    Arrays.toString(stitchMessage.getDisplayResolution())));
                            serverOut.append("\n");
                            //check owner(0-1) and number of stitch (0-1)
                            int stitchNum = StitchEvaluations.getStitchNumAndSide(stitchMessage)[0];
                            boolean owner = StitchEvaluations.checkOwner(stitchMessage);
                            SavedMessage msg2 = new SavedMessage(stitchMessage, stitchNum, owner);
                            addSavedMessage(msg2);
                            Log.i(DEBUG_TAG, "Saved2 message to savedMessages " + msg2.getSavedMessage());
                        }
                    });
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String formatTime(Date date) {
        return DATE_FORMATTER.format(date);
    }

    public void onSend(String msg) {
        Log.i(DEBUG_TAG, "Sent message to server: " + msg);
    }

}

