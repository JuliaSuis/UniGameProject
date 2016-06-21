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
import julia.connectivity.communication.CoordinateMessage;
import julia.connectivity.communication.Message;
import julia.connectivity.communication.StatusMessage;
import julia.connectivity.serialization.Parser;
import julia.uniGameProject.R;
import julia.uniGameProject.SampleApplication;

/**
 * Created by julia on 20.06.16.
 */
public class CustomClassMessageClientActionListener implements ClientActionListener {
    private static final String DEBUG_TAG = CustomClientActionListener.class.getName();
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    private List<String> cachedMessages = new ArrayList<>();

    public void onReceive(final String msg) {
        try {
            Log.i(DEBUG_TAG, "Received message from server: " + msg);
            final Activity current = SampleApplication.getActivity();
            Message message = Parser.parse(msg, Message.class);
            if (message instanceof StatusMessage) {
                final StatusMessage status = ((StatusMessage) message);
                current.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SampleApplication.getActivity().getApplication(),
                                String.format("Your connection status: [%s]", status.getStatus()),
                                Toast.LENGTH_SHORT).show();

                            current.findViewById(R.id.button_send_class_message).setEnabled(true);
                    }
                });
            }  else if (message instanceof CoordinateMessage) {
                final CoordinateMessage coordinateMessage = ((CoordinateMessage) message);
                final TextView serverOut = (TextView) current.findViewById(R.id.list_view_coordinate_receive);
                if (serverOut == null) {
                    cachedMessages.add(String.format("[%s ; %s]: Abscissa: %s , Ordinate: %s, " +
                                    "Display resolution:  %s", formatTime(coordinateMessage.getSendTime()), coordinateMessage.getClientId(),
                            coordinateMessage.getAbscissa(), coordinateMessage.getOrdinate(),
                            Arrays.toString(coordinateMessage.getDisplayResolution())));
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
                            serverOut.append(String.format("[%s ; %s]: Abscissa: %s , Ordinate: %s, " +
                                    "Display resolution:  %s", formatTime(coordinateMessage.getSendTime()), coordinateMessage.getClientId(),
                                    coordinateMessage.getAbscissa(), coordinateMessage.getOrdinate(),
                                    Arrays.toString(coordinateMessage.getDisplayResolution())));
                            serverOut.append("\n");
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

