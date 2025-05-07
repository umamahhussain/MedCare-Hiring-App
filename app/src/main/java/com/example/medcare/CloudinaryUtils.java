package com.example.medcare;

        import android.content.Context;
        import com.cloudinary.android.MediaManager;
        import java.util.HashMap;
        import java.util.Map;

public class CloudinaryUtils {

    private static boolean initialized = false;

    public static void init(Context context) {
        if (!initialized) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "umamahhussain");
            config.put("api_key", "648326857344586");
            config.put("api_secret", "NTub_3GySpQLzXWLnT4YNrMF0UA");

            MediaManager.init(context.getApplicationContext(), config);
            initialized = true;
        }
    }
}
