package julia.connectivity.client;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

class ReceivingThreadRunnable implements Runnable {

    public static final String DEBUG_TAG = ReceivingThreadRunnable.class.getName();

    private final Client client;
    private final Socket serverSocket;

    public ReceivingThreadRunnable(Client client,
                                   Socket serverSocket) {
        this.client = client;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(serverSocket.getInputStream()))
        ) {
            while (!Thread.currentThread().isInterrupted()) {
                String messageStr = input.readLine();
                if (messageStr != null) {
                    client.onReceive(messageStr);
                } else {
                    Log.d(DEBUG_TAG, "Null message");
                    break;
                }
                Thread.sleep(Client.WAIT_TIMEOUT);
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Client loop error: ", e);
            Thread.currentThread().interrupt();
        }
    }

}
