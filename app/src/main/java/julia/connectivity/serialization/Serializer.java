package julia.connectivity.serialization;

import com.owlike.genson.Genson;

/**
 * Created by julia on 18.06.16.
 */
public class Serializer {
    private static final Genson SERIALIZER = new Genson();

    public static <T> String serialize(T object) {
        String data = SERIALIZER.serialize(object);
        Meta meta = new Meta(object.getClass().getName(), data);
        return SERIALIZER.serialize(meta);
    }

}
