package julia.connectivity.server;

/**
 * Created by julia on 18.06.16.
 */
public interface ServerActionListener {
    void onReceive(String client, String msg);
    void onSend(String msg);
}
