package express.tutu.com.lib_tools.place;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlaceSinglePickerHelper extends BasePickerHelper{
    private Handler mHandler = new Handler();

    //true 手动调用dismiss，即选择完的dismiss
    private boolean isManualDismiss;

    private PlaceSinglePickerPresenter mPresenter;
    private PlaceSinglePickerViewImpl mViewImpl;
    private PlacePickerParams mPickerParams;

    private OnPickListener mOnPickListener;

    public void setOnPickListener(OnPickListener onPickListener) {
        this.mOnPickListener = onPickListener;
    }

    public PlaceSinglePickerHelper(Context context, Activity windowCallback) {
        this(context, new PlaceSinglePickerParams(), windowCallback);
    }

    public PlaceSinglePickerHelper(Context context, PlaceSinglePickerParams params, Activity windowCallback) {
        super(context, windowCallback);
        this.mPickerParams = params;
        mPresenter = new PlaceSinglePickerPresenter(context, params);
        mViewImpl = new PlaceSinglePickerViewImpl(context, mPresenter, params);
        mPresenter.attach(mViewImpl);
        mViewImpl.setOnPickListener(new PlaceSinglePickerViewImpl.OnPickListener() {
            @Override
            public boolean onPick(Place place) {
                if(mOnPickListener != null) {
                    mOnPickListener.onPick(place);
                }
                hideView();
                return false;
            }
        });
    }

    public Place getSelectedPlace() {
        return mViewImpl.getSelectedPlace();
    }

    public void clear() {
        mPresenter.setOriginalPlace(null);
        mViewImpl.clearSelectedPlaces();
        mViewImpl.resetPlaceList();
        mViewImpl.refreshHistoryViews();
    }

    public void pickPlace(Place place) {
        mPresenter.pickPlace(place);
    }

    public void pickPlace(int placeCode) {
        mPresenter.pickPlace(placeCode);
    }

    public String getFullPlaceName(Place place) {
        return mPresenter.getFullPlaceName(place);
    }

    @Override
    public void showView(View anchorView) {
        isManualDismiss = false;
        super.showView(anchorView);
        //重新打开，切回当前选择的place页
        mViewImpl.switchPickerList(getSelectedPlace());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mViewImpl.scrollToTop();
            }
        });
    }

    @Override
    public void hideView() {
        isManualDismiss = true;
        super.hideView();
    }

    @Override
    protected ListenedDrawRelativeLayout createContentView() {
        ListenedDrawRelativeLayout contentView = mViewImpl.getContentView();
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        return contentView;
    }

    @Override
    protected String getPopupWdName() {
        return "singlePickerPopupWd";
    }

    @Override
    protected void onShow() {
        if(mOnPickListener != null) {
            mOnPickListener.onShow();
        }
    }

    @Override
    protected void onDismiss() {
        if(isManualDismiss) {
            if(mOnPickListener != null) {
                mOnPickListener.onDismiss();
            }
            return;
        }

        Place currentPlace = mViewImpl.getCurrentPlace();
        if(getSelectedPlace() == null && (currentPlace.getCode() != 0 || mPickerParams.requiredStyle == GridPlacePicker.STYLE_NOT_REQUIRED)) {
            mPresenter.setOriginalPlace(currentPlace);
            mViewImpl.pickPlace(currentPlace);
            mViewImpl.refreshHistoryViews();
            if(mOnPickListener != null) {
                mOnPickListener.onPick(currentPlace);
            }
        }
        if(mOnPickListener != null) {
            mOnPickListener.onDismiss();
        }
    }

    public interface OnPickListener {
        void onPick(Place place);
        void onShow();
        void onDismiss();
    }
}
