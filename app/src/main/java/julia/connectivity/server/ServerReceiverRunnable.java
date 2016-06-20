package julia.connectivity.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by julia on 11.06.16.
 */
public class ServerReceiverRunnable implements Runnable {
    private static final String DEBUG_TAG = ServerReceiverRunnable.class.getName();

    private final Server server;
    private final Socket client;

    public ServerReceiverRunnable(Server server, Socket client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        try (BufferedReader streamReader =
                     new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            while (!Thread.currentThread().isInterrupted()) {
                if (streamReader.ready()) {
                    String msg = streamReader.readLine();
                    if (msg != null) {
                        server.onReceive(client.getInetAddress().getHostName(), msg);
                    } else {
                        Log.e(DEBUG_TAG, "Server have read a NULL from the client!");
                    }
                }
                Thread.sleep(Server.WAIT_TIMEOUT);
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error at receiving message", e);
            Thread.currentThread().interrupt();
        }
    }

}
