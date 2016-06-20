package julia.connectivity;

/**
 * Created by julia on 11.06.16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.InetAddress;

import julia.connectivity.client.Client;
import julia.connectivity.client.ClientActionListener;
import julia.connectivity.server.Server;
import julia.connectivity.server.ServerActionListener;


public class Connection {
    private static final String DEBUG_TAG = Connection.class.getName();
    private static Connection CURRENT_CONNECTION;

    private Server server;
    private NetworkDiscovery mNetworkDiscovery;
    private Client client;

    private Connection(final Context context) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mNetworkDiscovery = new NetworkDiscovery(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d(DEBUG_TAG, "Connection created successfully!");
            }
        }.execute((Void) null);
    }

    public static void initialize(Context context) {
        if (CURRENT_CONNECTION == null) {
            synchronized (Connection.class) {
                if (CURRENT_CONNECTION == null) {
                    CURRENT_CONNECTION = new Connection(context);
                }
            }
        }
    }

    public static Connection getConnection() {
        return CURRENT_CONNECTION;
    }

    /**
     * function to connect to server when you know IP address & port
     *
     * @param address            server address
     * @param port               server port
     */
    public void connectToServer(InetAddress address, int port) {
        client = new Client(address, port);
    }

    public boolean isServerUp() {
        return server != null;
    }

    public boolean isClientUp() {
        return client != null;
    }

    /**
     * creates server & connects to it
     */
    private void createServer() {
        if (isServerUp() || isClientUp()) {
            Log.d(DEBUG_TAG, "NOT POSSIBLE TO CREATE MORE THAN ONE SERVER OR WHEN YOU ARE A CLIENT");
        } else {
            Log.d(DEBUG_TAG, "Trying to init Server...");
            server = new Server();
            Log.d(DEBUG_TAG, "Server successfully inited!");
        }
    }

    /**
     * starts server
     * you need to use this function only for phone you need to register as server
     */
    public void startServer() {
        createServer();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mNetworkDiscovery.startServer(getServerPort());
                return null;
            }
        }.execute((Void) null);
    }

    /**
     * performs servers search
     *
     * @param listener listener, that will be called after something found
     *                 (see NetworkDiscovery.OnFoundListener)
     */
    public void findServers(final NetworkDiscovery.OnFoundListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mNetworkDiscovery.findServers(listener);

                return null;
            }
        }.execute((Void) null);
    }

    /**
     * closes connection
     */
    public void reset() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                closeConnection();
                stopNetworkDiscovery();
                return null;
            }
        }.execute((Void) null);
    }

    /**
     * stops network discovery
     */
    public void stopNetworkDiscovery() {
        if (mNetworkDiscovery != null) {
            mNetworkDiscovery.reset();
        }
    }

    public interface OnCreatedListener {
        void onCreated();
    }

    /**
     * closes connection
     */
    public void closeConnection() {
        if (server != null) {
            server.closeConnection();
        }
        if (client != null) {
            client.closeConnection();
        }
    }

    /**
     * returns local port
     *
     * @return port
     */
    public int getServerPort() {
        return  server == null ? -1 : server.getPort();
    }

    public Server getServerInstance() {
        return server;
    }

    public Client getClientInstance() {
        return client;
    }

    /**
     * Resets client callback, that Client executes every time for incoming/outcoming
     * messages. Simply execute this method with <code>NULL</code> to disable message
     * handling.
     * @param clientActionListener
     */
    public void resetClientIOCallback(ClientActionListener clientActionListener) {
        if (client != null) {
            client.resetActionListener(clientActionListener);
        } else {
            throw new RuntimeException("Cannot reset Client's IO callback while client is down!" +
                    "You must init it first by calling Connection#connectToServer()");
        }
    }

    /**
     * Resets server callback, that Server executes every time for incoming/outcoming
     * messages. Simply execute this method with <code>NULL</code> to disable message
     * handling.
     * @param serverActionListener
     */
    public void resetServerIOCallback(ServerActionListener serverActionListener) {
        if (server != null) {
            server.resetActionListener(serverActionListener);
        } else {
            throw new RuntimeException("Cannot reset Server's IO callback while server is down!" +
                    "You must init it first by calling Connection#connectToServer()");
        }
    }
}
