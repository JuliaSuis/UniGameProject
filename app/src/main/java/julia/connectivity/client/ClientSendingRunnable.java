package julia.connectivity.client;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import julia.connectivity.Connection;
import julia.connectivity.communication.Status;
import julia.connectivity.communication.StatusMessage;
import julia.connectivity.serialization.Serializer;

class ClientSendingRunnable implements Runnable {
    public static final String DEBUG_TAG = ClientSendingRunnable.class.getName();

    private final Client client;
    private final BlockingQueue<String> sendMessageQueue;
    private final Socket serverSocket;
    private ExecutorService receivingThreadExecutor = Executors.newSingleThreadExecutor();
    private PrintWriter serverOut;

    public ClientSendingRunnable(Client client,
                                 Socket serverSocket,
                                 BlockingQueue<String> sendMessageQueue) {
        this.client = client;
        this.sendMessageQueue = sendMessageQueue;
        this.serverSocket = serverSocket;
    }

    private void sendMessageToServer(String msg) {
        try {
            if (serverSocket == null || serverSocket.getOutputStream() == null) {
                Log.e(DEBUG_TAG, "Socket is null");
                Log.e(DEBUG_TAG, "Socket output stream is null");
                return;
            }

            if(serverOut == null) {
                serverOut = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(serverSocket.getOutputStream())
                        ), true
                );
            }
            serverOut.println(msg);
        } catch (UnknownHostException e) {
            Log.d(DEBUG_TAG, "Unknown Host: ", e);
        } catch (IOException e) {
            Log.d(DEBUG_TAG, "I/O Exception: ", e);
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Error: ", e);
        }
    }

    @Override
    public void run() {
        receivingThreadExecutor.execute(new ReceivingThreadRunnable(client, serverSocket));
        // launch Receiving thread
        sendMessageToServer(Serializer.serialize(new Status(StatusMessage.CONNECTION_OK)));
        // take message from queue and send it
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (!sendMessageQueue.isEmpty()) {
                    String msg = sendMessageQueue.take();
                    sendMessageToServer(msg);
                    client.onSend(msg);
                }
                Thread.sleep(Client.WAIT_TIMEOUT);
            } catch (InterruptedException e) {
                Log.d(DEBUG_TAG, "Message sending loop interrupted, exiting");
                Thread.currentThread().interrupt();
            }
        }
        serverOut.close();
        receivingThreadExecutor.shutdown();
    }
}
