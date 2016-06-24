package julia.connectivity.server;

import android.util.Log;
import android.util.Pair;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerSendingRunnable implements Runnable {
    public static final String DEBUG_TAG = ServerSendingRunnable.class.getName();

    private final Server server;
    private BlockingQueue<Pair<String, Socket>> sendMessageQueue;
    private Map<String, PrintWriter> channels = new HashMap<>();

    public ServerSendingRunnable(Server server) {
        this.server = server;
        sendMessageQueue = new ArrayBlockingQueue<>(1024);
    }

    public void sendMessageToClient(String msg, Socket client) {
        try {
            sendMessageQueue.put(new Pair<>(msg, client));
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error when putting into Queue", e);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if(!sendMessageQueue.isEmpty()) {
                    Pair<String, Socket> took = sendMessageQueue.take();
                    String msg = took.first;
                    Socket client = took.second;
                    PrintWriter writer = channels.get(client.getInetAddress().getHostName());
                    if(writer == null) {
                        writer = new PrintWriter(
                                new BufferedWriter(
                                        new OutputStreamWriter(client.getOutputStream())
                                )
                        );
                        channels.put(client.getInetAddress().getHostName(), writer);
                    }
                    writer.println(msg);
                    writer.flush();
                    server.onSend(msg);
                }
                Thread.sleep(Server.WAIT_TIMEOUT);
            }
            shutdownWriters();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error in ServerSendingThread", e);
            shutdownWriters();
            Thread.currentThread().interrupt();
        }
    }

    public void shutdownWriters() {
        for (Map.Entry<String, PrintWriter> entry : channels.entrySet()) {
            entry.getValue().close();
        }
    }

}
