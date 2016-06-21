package julia.connectivity.communication;

/**
 * Created by julia on 18.06.16.
 */
public class SimpleMessage extends BaseMessage implements TalkMessage {
    private String message;

    public SimpleMessage() {

    }

    public SimpleMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
