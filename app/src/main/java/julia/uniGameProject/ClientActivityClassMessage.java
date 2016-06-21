package julia.uniGameProject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Point;
import android.view.Display;

import java.util.Date;

import julia.connectivity.Connection;
import julia.connectivity.client.Client;
import julia.connectivity.communication.CoordinateMessage;
import julia.connectivity.communication.SimpleMessage;


public class ClientActivityClassMessage extends AppCompatActivity {

    private int[] resolution;

    private Button mButtonSendClass;

    private View.OnClickListener mButtonSendClassListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Client client = Connection.getConnection().getClientInstance();
            if (client == null) {
                throw new RuntimeException("Client must be sat first!");
            }

            CoordinateMessage coordinateMessage = new CoordinateMessage(9, 13, resolution);
            coordinateMessage.setSendTime(new Date());
            client.sendMessage(coordinateMessage);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_activity_class_message);
        mButtonSendClass = (Button) findViewById(R.id.button_send_class_message);
        mButtonSendClass.setEnabled(false);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        resolution = new int[]{size.x, size.y};
    }

    @Override
    protected void onResume() {
        super.onResume();
        mButtonSendClass.setOnClickListener(mButtonSendClassListener);
    }

    @Override
    protected void onPause() {
        mButtonSendClass.setOnClickListener(null);
        super.onPause();
    }
}
