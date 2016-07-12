package julia.uniGameProject.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import julia.connectivity.Connection;
import julia.connectivity.client.Client;
import julia.connectivity.communication.BallMessage;
import julia.uniGameProject.GameActivity;
import julia.uniGameProject.gestureRegistartion.AllNeighbours;
import julia.uniGameProject.gestureRegistartion.MessageProcessing;

/**
 * Created by julia on 10.07.16.
 */
public class GameViewPattern extends SurfaceView implements Runnable {
    volatile boolean playing;
    Thread gameThread = null;

    private static final String DEBUG_TAG = GameView.class.getName();

    //Game Objects
    private PlayerRacket player;
    private Scene scene;
    private Ball ball;
    private boolean continueUpdate = true;

    //Drawing objects
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    private int displayWidth;
    private int displayHeight;
    private int noOfPlayers;
    private boolean startGame=false;
    private GameActivity gameActivity;
    private AllNeighbours allNeighbours;
    private boolean[] side = {false,false,false,false}; // 0,1,2,3

    public GameViewPattern(Context context) {
        super(context);

        allNeighbours = MessageProcessing.getAllNeighbours();
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        gameActivity = new GameActivity();
        displayWidth=display.widthPixels;
        displayHeight=display.heightPixels;
        ourHolder=getHolder();
        paint=new Paint();
        player = new PlayerRacket(context);
        player.setX(displayWidth/2);
        player.setY(displayHeight/2);

        paint.setTextSize(10);
        paint.setColor(Color.RED);
        scene=new Scene(context);


        scene.setHorX(0);
        scene.setHorY(0);

        scene.setVerX(0);
        scene.setVerY(0);

        ball=new Ball(context);
        ball.setX(displayWidth/2);
        ball.setY(displayHeight/4);

    }

    public void drawAllNeighbourSides(){
        for (int i=0;i<=3; i++) {
            if (allNeighbours.getSideNeighbours(i) != null) {
                side[i]=true;
                for (int j = 0; j <= allNeighbours.getSideNeighbours(i).getSideNeihbours().size()-1; j++) {
                    int x1 = (int) allNeighbours.getSideNeighbours(i).getSideNeihbours().get(j).getxUp();
                    int y1 = (int) allNeighbours.getSideNeighbours(i).getSideNeihbours().get(j).getyUp();
                    int x2 = (int) allNeighbours.getSideNeighbours(i).getSideNeihbours().get(j).getxDown();
                    int y2 = (int) allNeighbours.getSideNeighbours(i).getSideNeihbours().get(j).getyDown();

                    paint.setColor(Color.BLACK);
                    canvas.drawLine(x1, y1, x2, y2, paint);
                }
            }

        }
    }

    @Override
    public void run() {
        while (playing) {
            //       System.out.println(noOfPlayers);
            draw();
            control();
        }
    }



    private void draw(){
        if (ourHolder.getSurface().isValid()){
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
            // Rub out the last frame
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Draw the player
            canvas.drawBitmap(
                    player.getBitmap(displayWidth,displayHeight),
                    player.getX(),
                    player.getY(),
                    paint);

            paint.setStrokeWidth(80);
            paint.setColor(Color.GREEN);
            canvas.drawLine(displayWidth,0,displayWidth, displayHeight, paint); //right
            canvas.drawLine(0,0,displayWidth, 0, paint); //top
            canvas.drawLine(0,displayHeight,displayWidth,displayHeight, paint); //bottom
            canvas.drawLine(0,0,0, displayHeight, paint); //left


            drawAllNeighbourSides();


            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);


        }
    }



    private void control(){

        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
        }

    }

    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }
    // Make a new thread and start it
// Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // There are many different events in MotionEvent
        // We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {



            // Has the player lifted their finger up?

       /*         case MotionEvent.ACTION_DOWN:

                        player.setX((int)motionEvent.getX()-70);
                        player.setY((int)motionEvent.getY()+7);

                    break;
                case MotionEvent.ACTION_UP:

                        player.setX((int)motionEvent.getX()-70);
                        player.setY((int)motionEvent.getY()+7);

                    break;
                 // Has the player touched the screen?*/
            case MotionEvent.ACTION_MOVE:
                //  if(motionEvent.getX()-20>=player.getX()&&motionEvent.getX()-20< (player.getX()+player.getBitmap().getWidth()) && motionEvent.getY() >=player.getY() &&
                //     motionEvent.getY()<(player.getY()+player.getBitmap().getHeight())) {
                player.setX((int) motionEvent.getX() - (player.getBitmap(displayWidth,displayHeight).getWidth()/2));
                player.setY((int) motionEvent.getY() + (player.getBitmap(displayWidth,displayHeight).getHeight()/2));
                //   }

                // Do something here
                break;
        }
        return true;
    }

    public void StartGame() {

        startGame=true;

    }

}