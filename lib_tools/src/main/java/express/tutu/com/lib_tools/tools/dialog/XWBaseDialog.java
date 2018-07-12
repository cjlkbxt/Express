package express.tutu.com.lib_tools.tools.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import express.tutu.com.lib_tools.R;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class XWBaseDialog extends BaseDialog{
    private View contentView;
    private Root root;

    public interface Checker<T> {
        boolean check(T t);
    }

    public FrameLayout.LayoutParams getContentParams() {
        return (FrameLayout.LayoutParams) contentView.getLayoutParams();
    }

    public View getContentView() {
        return contentView;
    }

    protected XWBaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected XWBaseDialog(Builder b) {
        super(b.context, R.style.NobackDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window == null) {
            throw new IllegalStateException("Window is null!");
        }
        window.setGravity(b.gravity);
        window.getDecorView().setBackgroundResource(android.R.color.transparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        if (b.animationResId != 0) {
            window.setWindowAnimations(b.animationResId);
        }
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = b.dimAmount;
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        windowParams.width = dm.widthPixels;
//        windowParams.height = dm.heightPixels;
        windowParams.alpha = b.dialogAlpha;
        window.setAttributes(windowParams);

        setCancelable(b.cancelable);
        setCanceledOnTouchOutside(b.cancelable);
        setOnDismissListener(b.dismissL);
        setOnCancelListener(b.cancelL);
        setOnShowListener(b.showL);
        setOnKeyListener(b.keyL);

        root = new Root(getContext());
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.widget_dialog_base_style, root, false);
        root.addView(contentView);
        if (b.cancelable) {
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                }
            });
        }
        setContentView(root, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setDialogName(b.dialogName);
        View btnExit = findViewById(R.id.btn_exit);
        View layoutMain = findViewById(R.id.layout_main);
        layoutMain.getLayoutParams().width = b.dialogWidth;
        layoutMain.getLayoutParams().height = b.dialogHeight;
        layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
        ImageView icon = (ImageView) findViewById(R.id.info_icon);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvMessage = (TextView) findViewById(R.id.tv_msg);
        LinearLayout titleContainer = (LinearLayout) findViewById(R.id.title_container);
        FrameLayout viewContainer = (FrameLayout) findViewById(R.id.view_container);
        View btnContainer = findViewById(R.id.btn_container);
        View btnDivider = findViewById(R.id.btn_divider);
        View btnPos = findViewById(R.id.btn_positive);
        View btnNeg = findViewById(R.id.btn_negative);
        TextView textPos = (TextView) findViewById(R.id.text_positive);
        TextView textNeg = (TextView) findViewById(R.id.text_negative);
        ImageView iconPos = (ImageView) findViewById(R.id.icon_positive);
        ImageView iconNeg = (ImageView) findViewById(R.id.icon_negative);
        if (b.iconResId > 0) {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(b.iconResId);
        } else {
            icon.setVisibility(View.GONE);
        }
        if (b.title != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(b.title);
            tvTitle.setGravity(b.titleGravity);
            if (b.titleSize > 0) {
                tvTitle.setTextSize(b.titleSize);
            }
            tvTitle.setTextColor(b.titleColor);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        titleContainer.setGravity(b.titleGravity & Gravity.HORIZONTAL_GRAVITY_MASK);
        if (b.view != null || b.viewLayoutId != 0) {
            viewContainer.setVisibility(View.VISIBLE);
            if (b.view == null) {
                LayoutInflater.from(getContext()).inflate(b.viewLayoutId, viewContainer);
            } else {
                b.view.setLayoutParams(b.view.getLayoutParams() == null
                        ? new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
                        : new FrameLayout.LayoutParams(b.view.getLayoutParams().width, b.view.getLayoutParams().height, Gravity.CENTER));
                viewContainer.addView(b.view);
            }
            if (b.viewPadding != -1) {
                viewContainer.setPadding(b.viewPadding, b.viewPadding, b.viewPadding, b.viewPadding);
            }
            if (b.viewMargin != -1) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewContainer.getLayoutParams();
                params.topMargin = b.viewMargin;
                params.bottomMargin = b.viewMargin;
                params.leftMargin = b.viewMargin;
                params.rightMargin = b.viewMargin;
                viewContainer.setLayoutParams(params);
            }
            tvMessage.setVisibility(View.GONE);
        } else if (b.message != null) {
            viewContainer.setVisibility(View.GONE);
            tvMessage.setText(b.message);
            tvMessage.setGravity(b.messageGravity);
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            viewContainer.setVisibility(View.GONE);
            tvMessage.setVisibility(View.GONE);
        }
        ViewGroup.MarginLayoutParams messageLayoutParams = (ViewGroup.MarginLayoutParams) tvMessage.getLayoutParams();
        if (b.title != null && b.message != null) {
            messageLayoutParams.topMargin = 0;
        } else {
            messageLayoutParams.topMargin = DensityTools.dip2px(b.context, 20);
        }
        tvMessage.setLayoutParams(messageLayoutParams);

        if (b.dialogBg != 0) {
            layoutMain.setBackgroundResource(b.dialogBg);
        }
        if (b.isShowClose) {
            btnExit.setVisibility(View.VISIBLE);
            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                }
            });
        } else {
            btnExit.setVisibility(View.GONE);
//            ((RelativeLayout.LayoutParams) layoutMain.getLayoutParams()).setMargins(0, 0, 0, 0);
        }
        if (TextUtils.isEmpty(b.posText) && b.posIcon == 0) {
            btnPos.setVisibility(View.GONE);
        } else {
            btnPos.setVisibility(View.VISIBLE);
            final DialogInterface.OnClickListener clickL = b.posClickL;
            final Checker<? super XWBaseDialog> checker = b.posChecker;
            final boolean isClickDismiss = b.isBtnClickDismiss;
            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checker != null && !checker.check(XWBaseDialog.this)) {
                        return;
                    }
                    if (isClickDismiss) {
                        dismiss();
                    }
                    if (clickL != null) {
                        clickL.onClick(XWBaseDialog.this, XWBaseDialog.BUTTON_POSITIVE);
                    }
                }
            });
            iconPos.setClickable(false);
            textPos.setClickable(false);
            if (!TextUtils.isEmpty(b.posText)) {
                textPos.setText(b.posText);
                textPos.setTextColor(b.posColor);
            } else {
                textPos.setVisibility(View.GONE);
            }
            if (b.posIcon != 0) {
                iconPos.setImageResource(b.posIcon);
                iconPos.setVisibility(View.VISIBLE);
            } else {
                iconPos.setVisibility(View.GONE);
            }
        }
        if (TextUtils.isEmpty(b.negText) && b.negIcon == 0) {
            btnNeg.setVisibility(View.GONE);
        } else {
            btnNeg.setVisibility(View.VISIBLE);
            final DialogInterface.OnClickListener clickL = b.negClickL;
            final Checker<? super XWBaseDialog> checker = b.negChecker;
            final boolean isClickDismiss = b.isBtnClickDismiss;
            btnNeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checker != null && !checker.check(XWBaseDialog.this)) {
                        return;
                    }
                    if (isClickDismiss) {
                        dismiss();
                    }
                    if (clickL != null) {
                        clickL.onClick(XWBaseDialog.this, XWBaseDialog.BUTTON_NEGATIVE);
                    }
                }
            });
            iconNeg.setClickable(false);
            textNeg.setClickable(false);
            if (!TextUtils.isEmpty(b.negText)) {
                textNeg.setText(b.negText);
                textNeg.setTextColor(b.negColor);
            } else {
                textNeg.setVisibility(View.GONE);
            }
            if (b.negIcon != 0) {
                iconNeg.setImageResource(b.negIcon);
            } else {
                iconNeg.setVisibility(View.GONE);
            }
        }
        titleContainer.setVisibility(icon.getVisibility() == View.VISIBLE || tvTitle.getVisibility() == View.VISIBLE
                ? View.VISIBLE : View.GONE);
        btnContainer.setVisibility(btnPos.getVisibility() == View.VISIBLE || btnNeg.getVisibility() == View.VISIBLE
                ? View.VISIBLE : View.GONE);
        btnDivider.setVisibility(btnPos.getVisibility() == View.VISIBLE && btnNeg.getVisibility() == View.VISIBLE
                ? View.VISIBLE : View.GONE);
        if (b.btnHeight != -1 && btnContainer.getVisibility() == View.VISIBLE) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) btnContainer.getLayoutParams();
            params.height = b.btnHeight;
            btnContainer.setLayoutParams(params);
        }


    }


    public static class Builder<Dialog extends XWBaseDialog, SubBuilder extends Builder<Dialog, SubBuilder>> extends DlgBuilder<Dialog, SubBuilder> {
        private CharSequence title = null;
        private CharSequence message = null;
        private CharSequence posText = null;
        private CharSequence negText = null;
        private int iconResId = 0;
        private OnClickListener posClickL = null;
        private OnClickListener negClickL = null;
        private boolean isShowClose;
        private int titleSize;
        private int titleColor = 0xff333333;
        private int posColor = 0xff666666;
        private int negColor = 0xff666666;
        private int posIcon = 0;
        private int negIcon = 0;
        private Checker<? super Dialog> posChecker;
        private Checker<? super Dialog> negChecker;
        private int dialogBg;
        private int gravity = Gravity.CENTER;
        private int titleGravity = Gravity.CENTER;
        private int messageGravity = Gravity.CENTER;
        private int animationResId;
        private float dimAmount = 0.5f;
        private float dialogAlpha = 1.0f;
        private int dialogWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        private int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int viewPadding = -1;
        private int viewMargin = -1;
        private int btnHeight = -1;
        private boolean isBtnClickDismiss = true;
        private String dialogName;

        public Builder(Context ctx) {
            super(ctx);
        }

        public SubBuilder setBtnClickDismiss(boolean btnClickDismiss) {
            this.isBtnClickDismiss = btnClickDismiss;
            return builder();
        }

        public SubBuilder setTitle(CharSequence title) {
            this.title = title;
            return builder();
        }

        public SubBuilder setTitleSize(int titleSize) {
            this.titleSize = titleSize;
            return builder();
        }

        public SubBuilder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return builder();
        }

        public SubBuilder setIcon(int resId) {
            this.iconResId = resId;
            return builder();
        }

        public SubBuilder setMessage(CharSequence msg) {
            this.message = msg;
            return builder();
        }

        public SubBuilder setShowCloseBtn(boolean show) {
            this.isShowClose = show;
            return builder();
        }

        public SubBuilder setPositiveButton(CharSequence text, OnClickListener l) {
            this.posText = text;
            this.posClickL = l;
            return builder();
        }

        public SubBuilder setPositiveTextColor(int color) {
            posColor = color;
            return builder();
        }

        public SubBuilder setNegativeButton(CharSequence text, OnClickListener l) {
            this.negText = text;
            this.negClickL = l;
            return builder();
        }

        public SubBuilder setNegativeTextColor(int color) {
            negColor = color;
            return builder();
        }

        public SubBuilder setPositiveIcon(int posIcon) {
            this.posIcon = posIcon;
            return builder();
        }

        public SubBuilder setNegativeIcon(int negIcon) {
            this.negIcon = negIcon;
            return builder();
        }

        public SubBuilder setPositiveChecker(Checker<? super Dialog> posChecker) {
            this.posChecker = posChecker;
            return builder();
        }

        public SubBuilder setNegativeChecker(Checker<? super Dialog> negChecker) {
            this.negChecker = negChecker;
            return builder();
        }

        public SubBuilder setView(View view) {
            this.view = view;
            return builder();
        }

        public SubBuilder setView(int layoutId) {
            this.viewLayoutId = layoutId;
            return builder();
        }

        public SubBuilder setViewPadding(int padding) {
            this.viewPadding = padding;
            return builder();
        }

        public SubBuilder setViewMargin(int margin) {
            this.viewMargin = margin;
            return builder();
        }

        @Override
        public Dialog create() {
            return null;
        }

        public SubBuilder setGravity(int gravity) {
            this.gravity = gravity;
            return builder();
        }

        public SubBuilder setTitleGravity(int titleGravity) {
            this.titleGravity = titleGravity;
            return builder();
        }

        public SubBuilder setMessageGravity(int messageGravity) {
            this.messageGravity = messageGravity;
            return builder();
        }

        public SubBuilder setAnimationResId(int animationResId) {
            this.animationResId = animationResId;
            return builder();
        }

        /**
         * 设置黑暗度
         *
         * @param dimAmount 0.0f和1.0f之间，0.0f完全不暗，1.0f全暗
         */
        public SubBuilder setDimAmount(float dimAmount) {
            this.dimAmount = dimAmount;
            return builder();
        }

        /**
         * 这是窗体本身的透明度，非背景
         *
         * @param dialogAlpha 0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
         */
        public SubBuilder setDialogAlpha(float dialogAlpha) {
            this.dialogAlpha = dialogAlpha;
            return builder();
        }

        public SubBuilder setDialogWidth(int dialogWidth) {
            this.dialogWidth = dialogWidth;
            return builder();
        }

        public SubBuilder setDialogHeight(int dialogHeight) {
            this.dialogHeight = dialogHeight;
            return builder();
        }

        public SubBuilder setDialogBg(int dialogBg) {
            this.dialogBg = dialogBg;
            return builder();
        }

        public SubBuilder setBtnHeight(int btnHeight) {
            this.btnHeight = btnHeight;
            return builder();
        }

        public SubBuilder setDialogName(String dialogName){
            this.dialogName = dialogName;
            return builder();
        }
    }

    private static class Root extends FrameLayout {

        public Root(@NonNull Context context) {
            super(context);
        }

//        @Override
//        protected void onAttachedToWindow() {
//            super.onAttachedToWindow();
//            System.out.println("## on attached to window ## ");
//        }
//
//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            System.out.println("## on measure ## " + getMeasuredWidth() + "x" + getMeasuredHeight());
//        }
//
//        @Override
//        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//            super.onLayout(changed, left, top, right, bottom);
//            System.out.println("## on layout ## " + getX() + "x" + getY());
//        }
//
//        @Override
//        public void setScaleX(float scaleX) {
//            super.setScaleX(scaleX);
//            System.out.println("## set scale ## " + scaleX);
//        }
    }
}
