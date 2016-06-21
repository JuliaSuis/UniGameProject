package julia.connectivity.communication;

/**
 * Created by julia on 18.06.16.
 */
public class Status extends BaseMessage implements StatusMessage {
    private String status;

    public Status() {

    }

    public Status(String status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return status;
    }
}
