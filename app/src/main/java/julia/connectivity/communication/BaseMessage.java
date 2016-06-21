package julia.connectivity.communication;

import java.util.Date;

/**
 * Created by julia on 21.06.16.
 */
public class BaseMessage implements Message {

    private String clientId;
    private Date sendTime;

    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
