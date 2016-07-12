package julia.uniGameProject.game;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import julia.connectivity.communication.BallMessage;
import julia.connectivity.communication.StitchMessage;
import julia.uniGameProject.SampleApplication;
import julia.uniGameProject.gestureRegistartion.AllNeighbours;
import julia.uniGameProject.gestureRegistartion.MessageProcessing;
import julia.uniGameProject.gestureRegistartion.Neighbour;
import julia.uniGameProject.gestureRegistartion.SavedMessage;
import julia.uniGameProject.gestureRegistartion.SideNeighbours;
import julia.uniGameProject.gestureRegistartion.StitchEvaluations;

/**
 * Created by julia on 10.07.16.
 */
public class SmthForGame {
    private static final String DEBUG_TAG = SmthForGame.class.getName();
    private static AllNeighbours allNeighbours = MessageProcessing.getAllNeighbours();
    public static BallMessage toRestartView;

    public static void saveBallM (BallMessage message){
        toRestartView = message;
    }
    public static BallMessage processBallMessage(BallMessage message){

            for (int i=0; i<=3; i++) {
                if (allNeighbours.getSideNeighbours(i) != null) {
                    for (int j = 0; j <= allNeighbours.getSideNeighbours(i).getSideNeihbours().size() - 1; j++) {
                        if (allNeighbours.getSideNeighbours(i).getSideNeihbours().get(j).getId().equals(message.getClientId())) {
                            int h = Math.abs((int) allNeighbours.getSideNeighbours(i).getSideNeihbours().get(j).getyUp() - message.getHeight());
                            int w = 0;
                            if (message.getWidth()<10) { w =500;}
                            else w=500;
                            BallMessage newBall = new BallMessage(message.getSpeed(), h, w, message.getIdNeighbour());
                            return newBall;
                        }
                    }
                }
            }
        return null;
    }

    public static boolean checkTargetNeighbour(BallMessage ballMessage){
        String TargetId = ballMessage.getIdNeighbour();
        Log.i(DEBUG_TAG, "TargetId " + TargetId);
        String MyId = StitchEvaluations.getLocalIpAddress();
        Log.i(DEBUG_TAG, "MyId " + MyId);
        if (TargetId.equals(MyId)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getLocalIpAddress(){
        try {
            WifiManager wifi = (WifiManager) SampleApplication.getActivity().getSystemService(android.content.Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifi.getConnectionInfo();
            int intaddr = wifiInfo.getIpAddress();

            byte[] byteaddr = new byte[]{
                    (byte) (intaddr & 0xff),
                    (byte) (intaddr >> 8 & 0xff),
                    (byte) (intaddr >> 16 & 0xff),
                    (byte) (intaddr >> 24 & 0xff)
            };
            InetAddress addr = InetAddress.getByAddress(byteaddr);
            return addr.toString().substring(1);
        } catch (IOException e) {
            Log.d(DEBUG_TAG, "Error in JmDNS creation: " + e);
        }
        return "";
    }
}
