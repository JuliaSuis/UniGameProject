package julia.uniGameProject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.graphics.Point;
import android.view.Display;
import android.widget.TextView;

import java.util.Date;

import julia.connectivity.Connection;
import julia.connectivity.client.Client;
import julia.connectivity.communication.CoordinateMessage;
import julia.connectivity.communication.SimpleMessage;
import julia.connectivity.communication.StitchMessage;


public class StitchActivity extends AppCompatActivity implements View.OnTouchListener{

    private int[] resolution;

    private double xStart = 0;
    private double yStart = 0;
    private double xEnd = 0;
    private double yEnd  = 0;

    private Button mButtonSendClass;
    private View view;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        double x = event.getX();
        double y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
               // pressTime = System.currentTimeMillis();
               // if (releaseTime != -1l) duration = pressTime - releaseTime;
                xStart = x;
                yStart = y;
                break;
            case MotionEvent.ACTION_MOVE: // движение
                break;
            case MotionEvent.ACTION_UP: // отпускание
              //  releaseTime = System.currentTimeMillis();
               // duration = System.currentTimeMillis() - pressTime;
                xEnd = x;
                yEnd = y;
                mButtonSendClass.setEnabled(true);

                //  stitchOffset();
                //    dataProcessing();
                break;
            case MotionEvent.ACTION_CANCEL:
        }


        return true;
    }




    private View.OnClickListener mButtonSendClassListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Client client = Connection.getConnection().getClientInstance();
            if (client == null) {
                throw new RuntimeException("Client must be sat first!");
            }

            StitchMessage stitchMessage = new StitchMessage(xStart, yStart, xEnd, yEnd, resolution);
            stitchMessage.setSendTime(new Date());
            client.sendMessage(stitchMessage);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_client_activity_class_message);
        mButtonSendClass = (Button) findViewById(R.id.button_send_class_message);
        mButtonSendClass.setEnabled(false);
        view = findViewById(R.id.stich_field);
        view.setOnTouchListener(this);


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
