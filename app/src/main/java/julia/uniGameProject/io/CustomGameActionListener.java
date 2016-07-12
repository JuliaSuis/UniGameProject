package julia.uniGameProject.io;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import julia.connectivity.client.ClientActionListener;
import julia.connectivity.communication.BallMessage;
import julia.connectivity.communication.Message;
import julia.connectivity.communication.StatusMessage;
import julia.connectivity.communication.StitchMessage;
import julia.connectivity.serialization.Parser;
import julia.uniGameProject.GameActivity;
import julia.uniGameProject.R;
import julia.uniGameProject.SampleApplication;
import julia.uniGameProject.game.GameView;
import julia.uniGameProject.game.SmthForGame;
import julia.uniGameProject.gestureRegistartion.MessageProcessing;
import julia.uniGameProject.gestureRegistartion.StitchEvaluations;

/**
 * Created by julia on 10.07.16.
 */
public class CustomGameActionListener implements ClientActionListener {
    private static final String DEBUG_TAG = CustomGameActionListener.class.getName();
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    static public String myIp;

    private void setMyIp(String myIp) {
        this.myIp = myIp;
    }

    private List<String> cachedMessages = new ArrayList<>();


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

                    }
                });
            }  else if (message instanceof BallMessage) {
                Log.i(DEBUG_TAG, "Tuta" + msg);
                final BallMessage ballMessage = ((BallMessage) message);
                final TextView serverOut = (TextView) current.findViewById(R.id.list_view_coordinate_receive);
                //if (serverOut == null) {
               // } else {
                    current.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!cachedMessages.isEmpty()) {
                                for (String cachedMessage : cachedMessages) {
                                    serverOut.append(cachedMessage);
                                    serverOut.append("\n");
                                }
                            }
                            if (SmthForGame.checkTargetNeighbour(ballMessage)) {
                                SmthForGame.saveBallM(SmthForGame.processBallMessage(ballMessage));
                                GameView.setAmIServer(true);
                                GameView.setIsMessage(true);
                                GameView.setH(ballMessage.getHeight());
                                GameView.setW(ballMessage.getWidth());
                                GameView.setSpeed(ballMessage.getSpeed());
                                Log.i(DEBUG_TAG, "I have received message and it's for meeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee: " + msg);

                            } else {
                                Log.i(DEBUG_TAG, "NOT mine, nothing to do" + msg);
                            }

                        }
                    });
                }
           // }
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
