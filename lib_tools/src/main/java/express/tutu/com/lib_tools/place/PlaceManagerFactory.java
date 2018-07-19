package express.tutu.com.lib_tools.place;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlaceManagerFactory {
    private static PlaceManagerCallback sCallback;

    public static PlaceManager getNormal() {
        if (sCallback != null) {
            PlaceManager manager = sCallback.getNormal();
            if (manager != null) return manager;
        }
        return PlaceManager.get();
    }

    public static ValidPlaceManager getValid() {
        if (sCallback != null) {
            ValidPlaceManager manager = sCallback.getValid();
            if (manager != null) return manager;
        }
        return ValidPlaceManager.get();
    }

    public static void setDefault(PlaceManagerCallback callback) {
        sCallback = callback;
    }

    public interface PlaceManagerCallback {

        PlaceManager getNormal();

        ValidPlaceManager getValid();
    }
}
