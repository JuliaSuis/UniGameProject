package julia.uniGameProject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import javax.jmdns.ServiceInfo;

import julia.connectivity.Connection;
import julia.connectivity.NetworkDiscovery;
import julia.connectivity.client.ClientActionListener;
import julia.connectivity.communication.Status;
import julia.connectivity.communication.StatusMessage;
import julia.uniGameProject.io.CustomStitchActionListener;
import julia.uniGameProject.io.CustomClientActionListener;
import julia.uniGameProject.io.CustomServerActionListener;

public class MainActivity extends Activity {
    private static final String DEBUG_TAG = MainActivity.class.getName();

    private Button mButtonStartServer;
    private Button mButtonConnect;
    private Button mButtonConnectSendClass;

    private View.OnClickListener mButtonStartServerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            int intaddr = wifi.getConnectionInfo().getIpAddress();
            Log.d(DEBUG_TAG, "MYYYYYYYY INTADDR IS " + intaddr);

            if (wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLED || intaddr == 0) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            } else {
                Connection connection = Connection.getConnection();
                if (!(connection.isServerUp() || connection.isClientUp())) {
                    mButtonStartServer.setEnabled(false);
                    connection.stopNetworkDiscovery();
                    connection.startServer();
                    Log.d(DEBUG_TAG, "Initializing standard server's IO callback");
                    connection.resetServerIOCallback(new CustomServerActionListener());
                    Log.d(DEBUG_TAG, "Callback initialize phase completed successfully");
                    Toast.makeText(MainActivity.this,
                            "Server successfully has started",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(DEBUG_TAG, "NOT POSSIBLE TO START ANOTHER SERVER WHILE EXISTS RUNNING ONE");
                }
            }
        }
    };

    private View.OnClickListener mButtonConnectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            connect(new CustomClientActionListener());
            Intent intent = new Intent(MainActivity.this, ClientActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener mButtonConnectSendClassOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            connect(new CustomStitchActionListener());
            Intent intent = new Intent(MainActivity.this, StitchActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonStartServer = (Button) findViewById(R.id.button_start_server);
        mButtonConnect = (Button) findViewById(R.id.button_connect);
        mButtonConnect.setEnabled(false);
        mButtonConnectSendClass = (Button) findViewById(R.id.button_connect_send_class);
        mButtonConnectSendClass.setEnabled(false);

        Connection.initialize(getApplicationContext());
        if (Connection.getConnection() != null) {
            mButtonConnect.setEnabled(true);
            mButtonConnectSendClass.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mButtonStartServer.setOnClickListener(mButtonStartServerOnClickListener);
        mButtonConnect.setOnClickListener(mButtonConnectOnClickListener);
        mButtonConnectSendClass.setOnClickListener(mButtonConnectSendClassOnClickListener);
    }

    @Override
    protected void onPause() {
        mButtonStartServer.setOnClickListener(null);
        mButtonConnect.setOnClickListener(null);
        mButtonConnectSendClass.setOnClickListener(null);

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Connection.getConnection().isClientUp()) {
            Connection.getConnection().getClientInstance().sendMessage(
                    new Status(StatusMessage.CONNECTION_DEAD)
            );
        }
        Connection.getConnection().closeConnection();
        System.gc();
        System.exit(0);
    }

    private void connect(final ClientActionListener T) {
        final Connection connection = Connection.getConnection();
        connection.findServers(new NetworkDiscovery.OnFoundListener() {
            @Override
            public void onFound(ServiceInfo info) {
                if (info != null && info.getInet4Addresses().length > 0) {
                    connection.stopNetworkDiscovery();
                    connection.connectToServer(
                            info.getInet4Addresses()[0],
                            info.getPort()
                    );
                    connection.resetClientIOCallback(T);
                }
            }
        });
    }
}

