package julia.uniGameProject.gestureRegistartion;

/**
 * Created by julia on 23.06.16.
 */

import android.util.Log;

import julia.connectivity.client.Client;
import julia.connectivity.communication.StitchMessage;

public class StitchEvaluations {

    private static final String DEBUG_TAG = StitchEvaluations.class.getName();

    private StitchEvaluations(){
    }

    //WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    //int intaddr = wifi.getConnectionInfo().getIpAddress();

    public static boolean checkDelay(long msgSentTime, long MsgReceivedDateMS){
        long delay = MsgReceivedDateMS - msgSentTime;
        if (delay < 5000) {
            Log.i(DEBUG_TAG, "1st Msg = " + msgSentTime + " 2nd Msg " + MsgReceivedDateMS);
            Log.i(DEBUG_TAG, "Delay is = " + delay);
            return true;
        }
        else {
            Log.i(DEBUG_TAG, "1st Msg = " + msgSentTime + " 2nd Msg " + MsgReceivedDateMS);
            Log.i(DEBUG_TAG, "Delay is = " + delay);
            return false;
        }
    }

    public static int[] getStitchNumAndSide(StitchMessage stitchMessage){
        // 0-down, 1-left, 2-up, 3-right, 4-up
        int limit = 50;
        double xStart = stitchMessage.getxStart();
        double yStart = stitchMessage.getyStart();
        double xEnd = stitchMessage.getxEnd();
        double yEnd = stitchMessage.getyEnd();
        double displayWidth = stitchMessage.getDisplayResolution()[0];
        double displayHight = stitchMessage.getDisplayResolution()[1] - 370;

        //stitchNum 0 - not appropriate stitch; 1 - first stitch; 2 - second stitch;
        int[] NumAndSide = new int[2];
        int stitchNum = 0;
        int side = 0;

        double xa = displayWidth*0.15, xb= displayWidth*0.85, ya = displayHight*0.15, yb = displayHight*0.85; // limits for stitch
        if (xa<xStart && xStart<xb && ya<yStart && yStart<yb){
            if (ya<yEnd && yEnd<yb && (xEnd>displayWidth-limit)) {stitchNum =1; side = 3;}
            else if (xa<xEnd && xEnd<xb && (yEnd>displayHight-limit)) {stitchNum = 1; side = 0;}
        }
        else if (xa<xEnd & xEnd<xb & ya<yEnd & yEnd<yb){
            if (xStart<limit & ya<yStart & yStart<yb ) {stitchNum =2; side = 1;}
            if (yStart<limit & xa<xStart & xStart<xb) {stitchNum =2; side = 2;}
        }
        NumAndSide[0] = stitchNum;
        NumAndSide[1] = side;
        return NumAndSide;
    }

    public static boolean checkOwner(StitchMessage stitchMessage){
        String SenderId = stitchMessage.getClientId();
        Log.i(DEBUG_TAG, "SenderId " + SenderId);
        String MyId = Client.getIdMsg().substring(1);
        Log.i(DEBUG_TAG, "MyId " + MyId);
        if (SenderId.equals(MyId)) {
            return true;
        } else {
            return false;
        }
    }
}
