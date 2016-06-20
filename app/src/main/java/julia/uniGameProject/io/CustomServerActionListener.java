package julia.uniGameProject.io;

import android.util.Log;

import julia.connectivity.server.ServerActionListener;

/**
 * Created by julia on 12.06.16.
 */
public class CustomServerActionListener implements ServerActionListener {
    private static final String DEBUG_TAG = CustomServerActionListener.class.getName();

    public void onReceive(String client, String msg) {
        Log.i(DEBUG_TAG,
                String.format("Server has receive a message from [%s], msg: [%s]", client, msg));
    }

    public void onSend(String msg){
        Log.i(DEBUG_TAG, "Sent message: " + msg);
    }
}
