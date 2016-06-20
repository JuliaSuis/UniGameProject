package julia.connectivity.server;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import julia.connectivity.communication.Message;
import julia.connectivity.communication.SimpleMessage;
import julia.connectivity.communication.Status;
import julia.connectivity.communication.StatusMessage;
import julia.connectivity.communication.TalkMessage;
import julia.connectivity.serialization.Parser;
import julia.connectivity.serialization.Serializer;

public class Server {
    private static final String DEBUG_TAG = Server.class.getName();
    private static final Lock LISTENER_LOCK = new ReentrantLock();

    static final long WAIT_TIMEOUT = 150L;

    private Map<String, Socket> clients = new HashMap<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;
    private InetAddress inetAddress;
    private int port;
    private ServerSendingRunnable sendingRunnable;

    private volatile ServerActionListener actionListener;

    public Server() {
        executorService.execute(new AcceptRunnable());
        sendingRunnable = new ServerSendingRunnable(this);
        executorService.execute(sendingRunnable);
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void sendMessageToClient(Message msg, String clientId) {
        Socket target = clients.get(clientId);
        if(target == null) {
            throw new RuntimeException(String.format("No such client with id: %s, existing: %s",
                    clientId,
                    clients.values()));
        }
        sendingRunnable.sendMessageToClient(Serializer.serialize(msg), clients.get(clientId));
    }

    public void makeMulticastSend(Message msg) {
        try {
            Log.d(DEBUG_TAG, "Making multicast send for message:" + msg);
            String serialized = Serializer.serialize(msg);
            for (Socket target : clients.values()) {
                sendingRunnable.sendMessageToClient(serialized, target);
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error at multicast send!", e);
        }
    }

    public void onReceive(String clientInfo, String msg) {
        try {
            LISTENER_LOCK.lock();
            if (actionListener != null) {
                actionListener.onReceive(clientInfo, msg);
            }
        } finally {
            LISTENER_LOCK.unlock();
        }
        try {
            Message message = Parser.parse(msg, Message.class);
            if (message instanceof StatusMessage) {
                StatusMessage status = (StatusMessage)message;
                if(StatusMessage.CONNECTION_OK.equals(status.getStatus())) {
                    makeMulticastSend(
                            new SimpleMessage(
                                    String.format("Client %s successfully connected to server!",
                                            clientInfo))
                    );
                } else if (StatusMessage.CONNECTION_DEAD.equals(status.getStatus())){
                    makeMulticastSend(new SimpleMessage(
                            String.format("Client %s disconnected!",
                                    clientInfo)));
                    clients.remove(clientInfo).close();
                } else {
                    Log.d(DEBUG_TAG, String.format("Client [%s] trying to make a connect with status [%s]",
                            clientInfo, status.getStatus()));
                }
            } else {
                makeMulticastSend(message);
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Unexpected type of message", e);
        }

    }

    public void onSend(String msg) {
        try {
            LISTENER_LOCK.lock();
            if (actionListener != null) {
                actionListener.onSend(msg);
            }
        } finally {
            LISTENER_LOCK.unlock();
        }
    }

    /**
     * interrupts server and closes server socket
     */
    public void closeConnection() {
        try {
            for(Map.Entry<String, Socket> entry : clients.entrySet()) {
                entry.getValue().close();
            }
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Error when closing socket: " + e);
        }
        try {
            sendingRunnable.shutdownWriters();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.getMessage());
        }
        try {
            executorService.shutdownNow();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, e.getMessage());
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Error when closing server socket: " + e);
        }
    }

    public void resetActionListener(ServerActionListener actionListener) {
        LISTENER_LOCK.lock();
        try {
            this.actionListener = actionListener;
        } finally {
            LISTENER_LOCK.unlock();
        }
    }

    private class AcceptRunnable implements Runnable {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(0);
                inetAddress = serverSocket.getInetAddress();
                port = serverSocket.getLocalPort();
                while (!Thread.currentThread().isInterrupted()) {
                    Log.d(DEBUG_TAG, "Listening for incoming...");
                    Socket clientSocket = serverSocket.accept();
                    String clientHostName = clientSocket.getInetAddress().getHostName();
                    clients.put(clientHostName, clientSocket);
                    sendMessageToClient(new Status(Status.CONNECTION_OK), clientHostName);
                    executorService.execute(
                            new ServerReceiverRunnable(Server.this, clientSocket)
                    );
                    Log.d(DEBUG_TAG,
                            String.format("Client [%s:%d] accepted, waiting for next...",
                                    clientHostName,
                                    clientSocket.getPort()));
                }
                serverSocket.close();
            } catch (IOException e) {
                Log.d(DEBUG_TAG, "Server IOException: " + e);
            }
        }
    }

}
