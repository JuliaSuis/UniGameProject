package julia.uniGameProject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import julia.connectivity.Connection;
import julia.connectivity.client.Client;
import julia.connectivity.communication.SimpleMessage;

public class ClientActivity extends AppCompatActivity {

    private Button mButtonSendMessage;

    private View.OnClickListener mButtonSendMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Client client = Connection.getConnection().getClientInstance();
            if (client == null) {
                throw new RuntimeException("Client must be sat first!");
            }
            EditText input = (EditText)ClientActivity.this.findViewById(R.id.message_text);
            if (input.getText().length() != 0) {
                Editable editable = input.getText();
                char[] text = new char[editable.length()];
                editable.getChars(0, text.length, text, 0);
                SimpleMessage simpleMessage = new SimpleMessage(String.valueOf(text));
                simpleMessage.setSendTime(new Date());
                client.sendMessage(simpleMessage);
                editable.clear();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_client);
        mButtonSendMessage = (Button) findViewById(R.id.button_send_message);
        mButtonSendMessage.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mButtonSendMessage.setOnClickListener(mButtonSendMessageListener);
    }

    @Override
    protected void onPause() {
        mButtonSendMessage.setOnClickListener(null);
        super.onPause();
    }
}
