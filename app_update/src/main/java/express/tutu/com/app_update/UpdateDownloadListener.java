package express.tutu.com.app_update;

/**
 * Created by cjlkbxt on 2018/7/10/010.
 */

public interface UpdateDownloadListener {
    public void onStarted();
    public void onProgressChanged(int progress, String downloadUrl);
    public void onFinished(float completeSize, String downloadUrl);
    public void onFailure();

}
