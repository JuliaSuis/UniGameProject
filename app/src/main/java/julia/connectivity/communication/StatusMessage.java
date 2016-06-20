package julia.connectivity.communication;

/**
 * Created by julia on 18.06.16.
 */
public interface StatusMessage extends Message {
    String CONNECTION_OK = "CONNECTION_OK";
    String CONNECTION_ATTEMPT = "CONNECTION_ATTEMPT";
    String CONNECTION_DEAD = "CONNECTION_DEAD";

    String getStatus();
}
