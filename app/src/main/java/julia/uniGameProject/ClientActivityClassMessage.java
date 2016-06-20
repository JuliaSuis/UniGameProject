package julia.uniGameProject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import julia.connectivity.Connection;
import julia.connectivity.client.Client;
import julia.connectivity.communication.CoordinateMessage;


public class ClientActivityClassMessage extends AppCompatActivity {

    private Button mButtonSendClass;

    private View.OnClickListener mButtonSendClassListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Client client = Connection.getConnection().getClientInstance();
            if (client == null) {
                throw new RuntimeException("Client must be sat first!");
            }
            client.sendMessage(new CoordinateMessage(9, 13));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_activity_class_message);
        mButtonSendClass = (Button) findViewById(R.id.button_send_class_message);
        mButtonSendClass.setEnabled(false);
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
