package julia.connectivity.serialization;

import com.owlike.genson.Genson;

/**
 * Created by julia on 18.06.16.
 */
public class Parser {
    private static final Genson PARSER = new Genson();

    public static <T> T parse(String serializedData, Class<T> targetClass) {
        Meta meta = PARSER.deserialize(serializedData, Meta.class);
        try {
            Class<?> resultClass = Class.forName(meta.getClassInfo());
            if (targetClass.isAssignableFrom(resultClass)) {
                return targetClass.cast(PARSER.deserialize(meta.getData(), resultClass));
            } else {
                throw new RuntimeException(String.format("Cannot cast object of type %s to %s",
                        resultClass.getName(), targetClass.getName()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
