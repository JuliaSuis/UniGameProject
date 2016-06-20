package julia.connectivity.client;

import julia.connectivity.communication.CoordinateMessage;

/**
 * Created by julia on 18.06.16.
 */
public interface ClientActionListener {

    void onReceive(String msg);
    void onSend(String msg);

    //void onSend(CoordinateMessage msg);

}
