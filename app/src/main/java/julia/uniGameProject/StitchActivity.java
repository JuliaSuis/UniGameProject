package julia.uniGameProject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.graphics.Point;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import julia.connectivity.Connection;
import julia.connectivity.client.Client;
import julia.connectivity.communication.CoordinateMessage;
import julia.connectivity.communication.SimpleMessage;
import julia.connectivity.communication.StitchMessage;
import julia.uniGameProject.gestureRegistartion.MessageProcessing;
import julia.uniGameProject.gestureRegistartion.Neighbour;
import julia.uniGameProject.io.CustomGameActionListener;
import julia.uniGameProject.io.CustomGameActionListener;
import julia.uniGameProject.io.CustomStitchActionListener;


public class StitchActivity extends AppCompatActivity implements View.OnTouchListener{

    private static final String DEBUG_TAG = StitchActivity.class.getName();


    private int[] resolution;

    private double xStart = 0;
    private double yStart = 0;
    private double xEnd = 0;
    private double yEnd  = 0;

    private Button mButtonSendClass;
    private View view;
    private Bitmap bitmap ;
    private Canvas canvas;
    private Paint paint;
    private ImageView imageView;
    private double duration = -1l;
    private double releaseTime = -1l;
    private double pressTime =-1l;



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        double x = event.getX();
        double y = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressTime = System.currentTimeMillis();
                if (releaseTime != -1l) duration = pressTime - releaseTime;
                xStart = x;
                yStart = y;


                    Neighbour neighbourForDraw = MessageProcessing.getNeighbourForDraw();
                    if (neighbourForDraw.getId() != null) {

                        float x1 = (float) neighbourForDraw.getxUp();
                        float y1 = (float) neighbourForDraw.getyUp();
                        float x2 = (float) neighbourForDraw.getxDown();
                        float y2 = (float) neighbourForDraw.getyDown();

                        canvas.drawLine(x1, y1, x2, y2, paint);


                        Log.i(DEBUG_TAG, "x1= " + x1 + " y1= " + y1 + " x2= " + x2 + " y2= " + y2);
                        imageView.invalidate();

                }



                break;
            case MotionEvent.ACTION_MOVE: // движение
                break;
            case MotionEvent.ACTION_UP: // отпускание
                releaseTime = System.currentTimeMillis();
                duration = System.currentTimeMillis() - pressTime;
                xEnd = x;
                yEnd = y;
                //mButtonSendClass.setEnabled(true);
                Client client = Connection.getConnection().getClientInstance();
                if (client == null) {
                    throw new RuntimeException("Client must be sat first!");
                }

                StitchMessage stitchMessage = new StitchMessage(xStart, yStart, xEnd, yEnd, resolution);
                stitchMessage.setSendTime(new Date());
                client.sendMessage(stitchMessage);

                if (duration != -1l) {
                    if (duration >= 4000) {

                        Intent intent = new Intent(StitchActivity.this, GameActivity.class);
                        startActivity(intent);





                    }
                }

                //  stitchOffset();



                break;
            case MotionEvent.ACTION_CANCEL:
        }

        // how to call neighboutToDraw
        //Neighbour neighbourForDraw = MessageProcessing.getNeighbourForDraw();
        return true;
    }




    private View.OnClickListener mButtonSendClassListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
       /*     Client client = Connection.getConnection().getClientInstance();
            if (client == null) {
                throw new RuntimeException("Client must be sat first!");
            }

            StitchMessage stitchMessage = new StitchMessage(xStart, yStart, xEnd, yEnd, resolution);
            stitchMessage.setSendTime(new Date());
            client.sendMessage(stitchMessage); */


            Neighbour neighbourForDraw = MessageProcessing.getNeighbourForDraw();
            neighbourForDraw.getId().isEmpty();
            float x1 = (float) neighbourForDraw.getxUp();
            float y1 = (float) neighbourForDraw.getyUp();
            float x2 = (float) neighbourForDraw.getxDown();
            float y2 = (float) neighbourForDraw.getyDown();

            canvas.drawLine(x1,y1,x2,y2, paint);


            Log.i(DEBUG_TAG, "x1= " + x1 + " y1= " + y1 + " x2= " + x2 + " y2= " + y2);
            imageView.invalidate();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_client_activity_class_message);

        /*mButtonSendClass = (Button) findViewById(R.id.button_send_class_message);
        mButtonSendClass.setEnabled(false);*/
        view = findViewById(R.id.stich_field);
        view.setOnTouchListener(this);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        resolution = new int[]{size.x, size.y};

        int xMax = size.x;
        int yMax = size.y;

        imageView = (ImageView) this.findViewById(R.id.imageView);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(xMax,yMax);
        imageView.setLayoutParams(parms);
        bitmap = Bitmap.createBitmap((int) xMax, (int) yMax,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // mButtonSendClass.setOnClickListener(mButtonSendClassListener);
    }

    @Override
    protected void onPause() {
        //mButtonSendClass.setOnClickListener(null);
        super.onPause();
    }
}
