package julia.connectivity.client;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import julia.connectivity.communication.Message;
import julia.connectivity.serialization.Serializer;

public class Client {
    public static final String DEBUG_TAG = Client.class.getName();
    private static final Lock LISTENER_LOCK = new ReentrantLock();

    static final long WAIT_TIMEOUT = 300L;

    private ExecutorService sendingThreadExecutor = Executors.newSingleThreadExecutor();
    private BlockingQueue<String> sendMessageQueue;
    private Socket serverSocket;

    private final InetAddress inetAddress;
    private final int port;

    static String idMsg = "";

    static public String getIdMsg(){
        return idMsg;
    }

    private volatile ClientActionListener actionListener;

    public Client(final InetAddress inetAddress,
                  final int port) {
        sendMessageQueue = new ArrayBlockingQueue<>(164);
        this.inetAddress = inetAddress;
        idMsg = inetAddress.toString();
        this.port = port;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    serverSocket = new Socket(inetAddress, port);
                    Log.d(DEBUG_TAG, "My LOVE!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + inetAddress);
                    sendingThreadExecutor.execute(new ClientSendingRunnable(
                            Client.this,
                            serverSocket,
                            sendMessageQueue));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        }.execute();
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    /**
     * sends given message to server
     *
     * @param msg message text
     */
    public void sendMessage(Message msg) {
        try {
            sendMessageQueue.put(Serializer.serialize(msg));
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.getMessage());
        }
    }

    /**
     * closes connection to server
     */
    public void closeConnection() {
        try {
            sendingThreadExecutor.shutdownNow();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.getMessage());
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Error when closing server socket: " + e);
        }
    }

    public void onReceive(String msg) {
        LISTENER_LOCK.lock();
        try {
            if (actionListener != null) {
                actionListener.onReceive(msg);
            }
        } finally {
            LISTENER_LOCK.unlock();
        }
    }

    public void onSend(String msg) {
        LISTENER_LOCK.lock();
        try {
            if (actionListener != null) {
                actionListener.onSend(msg);
            }
        } finally {
            LISTENER_LOCK.unlock();
        }
    }

    public void resetActionListener(ClientActionListener actionListener) {
        LISTENER_LOCK.lock();
        try {
            this.actionListener = actionListener;
        } finally {
            LISTENER_LOCK.unlock();
        }
    }
}
