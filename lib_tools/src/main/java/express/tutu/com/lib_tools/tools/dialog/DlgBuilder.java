package express.tutu.com.lib_tools.tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public abstract class DlgBuilder<Dlg extends Dialog, Builder extends DlgBuilder<Dlg, Builder>>
        implements IDlgBuilder<Dlg, Builder> {

    @NonNull
    Context context;
    boolean cancelable = true;
    View view = null;
    int viewLayoutId = 0;
    DialogInterface.OnDismissListener dismissL;
    DialogInterface.OnCancelListener cancelL;
    DialogInterface.OnKeyListener keyL;
    DialogInterface.OnShowListener showL;

    public DlgBuilder(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    public Context getContext() {
        return context;
    }

    @Override
    public Builder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return builder();
    }

    @Override
    public Builder setOnDismissListener(DialogInterface.OnDismissListener dismissL) {
        this.dismissL = dismissL;
        return builder();
    }

    @Override
    public Builder setOnCancelListener(DialogInterface.OnCancelListener cancelL) {
        this.cancelL = cancelL;
        return builder();
    }

    @Override
    public Builder setOnKeyListener(DialogInterface.OnKeyListener keyL) {
        this.keyL = keyL;
        return builder();
    }

    @Override
    public Builder setOnShowListener(DialogInterface.OnShowListener showL) {
        this.showL = showL;
        return builder();
    }

    @Override
    public Builder setView(View view) {
        this.view = view;
        return builder();
    }

    @Override
    public Builder setView(int layoutId) {
        this.viewLayoutId = layoutId;
        return builder();
    }

    @Override
    public Dlg show() {
        Dlg dlg = create();
        dlg.show();
        return dlg;
    }

    protected Builder builder() {
        return (Builder) this;
    }
}