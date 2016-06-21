package julia.connectivity.communication;

import java.util.Date;

/**
 * Created by julia on 18.06.16.
 */
public interface Message {
    public String getClientId();
    public Date getSendTime();
}

