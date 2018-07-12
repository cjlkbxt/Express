package express.tutu.com.lib_tools.tools.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class TouchHack extends FrameLayout{
    public interface OnHackTouchListener {
        void onHackTouch(TouchHack touchHack, TouchRecord down, TouchRecord up);
    }

    public static class TouchRecord {
        private View target;
        private MotionEvent event;

        public TouchRecord(View target, MotionEvent event) {
            this.target = target;
            this.event = MotionEvent.obtainNoHistory(event);
        }

        public View getTarget() {
            return target;
        }

        public MotionEvent getEvent() {
            return event;
        }

        @Override
        public String toString() {
            return target.toString() + " @ " + "[" + event.getX() + "," + event.getY() + "]" + event.getEventTime();
        }
    }

    private OnHackTouchListener onHackTouchL;
    private TouchRecord down, up;

    private String windowName;

    public TouchHack(@NonNull Context context) {
        super(context);
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setOnHackTouchL(OnHackTouchListener onHackTouchL) {
        this.onHackTouchL = onHackTouchL;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        long nano = Debug.threadCpuTimeNanos();
        boolean handled;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                down = null;
                up = null;
//                long s1 = Debug.threadCpuTimeNanos();
                handled = super.dispatchTouchEvent(ev);
//                funcTime.add(Debug.threadCpuTimeNanos() - s1);
                if (handled) {
                    down = new TouchRecord(findTouchTarget(this), ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                up = new TouchRecord(findTouchTarget(this), ev);
            case MotionEvent.ACTION_CANCEL:
                if (onHackTouchL != null) {
                    onHackTouchL.onHackTouch(this, down, up);
                }
            default:
//                long s2 = Debug.threadCpuTimeNanos();
                handled = super.dispatchTouchEvent(ev);
//                funcTime.add(Debug.threadCpuTimeNanos() - s2);
                break;
        }
//        hackTime.add(Debug.threadCpuTimeNanos() - nano);
        return handled;
    }

    private View findTouchTarget(View view) {
//        long nano = Debug.threadCpuTimeNanos();
//        try {
        return findTouchTargetInternal(view);
//        } finally {
//            findTime.add(Debug.threadCpuTimeNanos() - nano);
//        }
    }

    private View findTouchTargetInternal(View view) {
        if (!(view instanceof ViewGroup)) {
            return view;
        }
        View childTarget = view;
        try {
            Object touchTarget = HookUtil.get(view, ViewGroup.class, "mFirstTouchTarget");
            if (touchTarget != null) {
                Object child = HookUtil.get(touchTarget, "child");
                if (child != null && child instanceof View) {
                    childTarget = (View) child;
                }
            }
        } catch (Exception ignored) {}
        if (childTarget != view) {
            childTarget = findTouchTargetInternal(childTarget);
        }
        return childTarget;
    }
}
