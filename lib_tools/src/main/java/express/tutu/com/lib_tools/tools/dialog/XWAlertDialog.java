package express.tutu.com.lib_tools.tools.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import express.tutu.com.lib_tools.R;
import express.tutu.com.lib_tools.utils.ContextUtil;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class XWAlertDialog extends XWBaseDialog{
    private static int positiveBtnColor = ContextCompat.getColor(ContextUtil.get(), R.color.colorPrimary);

    public static void setPositiveBtnColor(int positiveBtnColor) {
        XWAlertDialog.positiveBtnColor = positiveBtnColor;
    }

    protected XWAlertDialog(Builder builder) {
        super(builder);
    }

    public static XWAlertDialog simpleAlert(Context context, CharSequence message) {
        return new Builder(context).setMessage(message).setPositiveButton("好的", null).create();
    }
    public static XWAlertDialog simpleAlert(Context context, CharSequence message, String dialogName) {
        return new Builder(context).setMessage(message).setPositiveButton("好的", null).setDialogName(dialogName).create();
    }
    public static class Builder extends XWBaseDialog.Builder<XWAlertDialog, Builder> {
        public Builder(Context context) {
            super(context);
            setPositiveTextColor(positiveBtnColor).setShowCloseBtn(false).setCancelable(true);
        }
        @Override
        public XWAlertDialog create() {
            return new XWAlertDialog(this);
        }
    }
}
