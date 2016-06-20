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
import julia.connectivity.communication.Message;
import julia.connectivity.communication.StatusMessage;
import julia.connectivity.communication.TalkMessage;
import julia.connectivity.serialization.Parser;
import julia.uniGameProject.R;
import julia.uniGameProject.SampleApplication;

/**
 * Created by julia on 12.06.16.
 */
public class CustomClientActionListener implements ClientActionListener {
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

                            current.findViewById(R.id.button_send_message).setEnabled(true);
                    }
                });
            } else if (message instanceof TalkMessage) {
                final TalkMessage talkMessage = ((TalkMessage) message);
                final TextView serverOut = (TextView) current.findViewById(R.id.list_view_message_from_server);
                if (serverOut == null) {
                    //that means that that TextView not available now, cache it and show it later...
                    cachedMessages.add(String.format("[%s]: %s",
                            currentTime(),
                            talkMessage.getMessage()));
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
                            serverOut.append(String.format("[%s]: %s", currentTime(),
                                    talkMessage.getMessage()));
                            serverOut.append("\n");
                        }
                    });
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String currentTime() {
        return DATE_FORMATTER.format(new Date());
    }

    public void onSend(String msg) {
        Log.i(DEBUG_TAG, "Sent message to server: " + msg);
    }

}
