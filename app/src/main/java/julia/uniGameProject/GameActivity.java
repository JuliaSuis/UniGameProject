package julia.uniGameProject;

        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.FrameLayout;

        import julia.connectivity.client.Client;
        import julia.connectivity.communication.BallMessage;
        import julia.uniGameProject.game.GameView;
        import julia.uniGameProject.io.CustomGameActionListener;

        import julia.connectivity.Connection;

/**
 * Created by julia on 10.07.16.
 */
public class GameActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String DEBUG_TAG = GameActivity.class.getName();
    Client client = Connection.getConnection().getClientInstance();

    //private GameView gameView;
    private GameView gameView;
    private FrameLayout game;
    private boolean amIServer = Connection.getConnection().isServerUp();

    public boolean onTouch(View v, MotionEvent event) {
        double x = event.getX();
        double y = event.getY();
        return true;
    }

    public void resetGameView(BallMessage ballMessage){
        gameView = new GameView(this, amIServer);
        Log.i(DEBUG_TAG, "Yehoooo " + Connection.getConnection().isServerUp());
        game = new FrameLayout(this);
        game.addView(gameView);
        setContentView(game);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Connection.getConnection().resetClientIOCallback(new CustomGameActionListener());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


            gameView = new GameView(this, amIServer);
            Log.i(DEBUG_TAG, "Yehoooo " + Connection.getConnection().isServerUp());
            game = new FrameLayout(this);
            game.addView(gameView);
            setContentView(game);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        // mButtonSendClass.setOnClickListener(mButtonSendClassListener);
    }

    @Override
    protected void onPause() {
        //mButtonSendClass.setOnClickListener(null);
        super.onPause();
        gameView.pause();
    }


}
