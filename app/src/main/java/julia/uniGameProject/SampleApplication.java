package julia.uniGameProject;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by julia on 11.06.16.
 */
public class SampleApplication extends Application {
    private static final String DEBUG_TAG = SampleApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Activity getActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Error, while getting current activity", e);
        }
        throw new RuntimeException("Cannot obtain current activity!");
    }
}
