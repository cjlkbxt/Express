package express.tutu.com.lib_tools.tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class BaseDialog extends Dialog {
    private Context context;
    public static final String TAG = "BaseDialog";
    private TouchHack touchHack;

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    @Override
    public void setContentView(int layoutResID) {
        touchHack = new TouchHack(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResID, touchHack, true);
        touchHack.setOnHackTouchL(ViewTouchSource.getInstance().hackTouchL);
        super.setContentView(touchHack);
    }

    @Override
    public void setContentView(@NonNull View view) {
        touchHack = new TouchHack(context);
        touchHack.removeAllViews();
        touchHack.addView(view);
        touchHack.setOnHackTouchL(ViewTouchSource.getInstance().hackTouchL);
        super.setContentView(touchHack);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        touchHack = new TouchHack(context);
        touchHack.removeAllViews();
        touchHack.addView(view);
        touchHack.setOnHackTouchL(ViewTouchSource.getInstance().hackTouchL);
        super.setContentView(touchHack, params);
    }

    @Override
    public void addContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    public BaseDialog setDialogName(String dialogName) {
        if (touchHack != null && !TextUtils.isEmpty(dialogName)) {
            touchHack.setWindowName(dialogName);
            Log.i(TAG, dialogName);
        }
        return this;
    }
}
