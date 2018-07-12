package express.tutu.com.lib_tools.tools.dialog;

import android.content.DialogInterface;
import android.view.View;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public interface IDlgBuilder<Dialog, Builder extends IDlgBuilder<Dialog, Builder>> {

    Builder setCancelable(boolean cancelable);

    Builder setOnDismissListener(DialogInterface.OnDismissListener dismissL);

    Builder setOnCancelListener(DialogInterface.OnCancelListener cancelL);

    Builder setOnKeyListener(DialogInterface.OnKeyListener keyL);

    Builder setOnShowListener(DialogInterface.OnShowListener showL);

    Builder setView(View view);

    Builder setView(int layoutId);

    Dialog create();

    Dialog show();
}

