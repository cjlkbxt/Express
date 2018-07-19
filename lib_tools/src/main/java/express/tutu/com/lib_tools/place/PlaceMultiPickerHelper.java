package express.tutu.com.lib_tools.place;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import express.tutu.com.lib_tools.utils.CollectionUtil;

/**
 * Created by cjlkbxt on 2018/7/18/018.
 */

public class PlaceMultiPickerHelper extends BasePickerHelper{

    private Handler mHandler = new Handler();
    private OnPickListener mOnPickListener;

    //true 手动调用dismiss，即选择完的dismiss
    private boolean isManualDismiss;

    private PlaceMultiPickerPresenter mPresenter;
    private PlaceMultiPickerViewImpl mViewImpl;

    public PlaceMultiPickerHelper(Context context, PlaceMultiPickerParams params, Activity windowCallback) {
        super(context, windowCallback);
        mPresenter = new PlaceMultiPickerPresenter(context, params);
        mViewImpl = new PlaceMultiPickerViewImpl(context, mPresenter, params);
        mPresenter.attach(mViewImpl);
        mViewImpl.setOnPickListener(new PlaceMultiPickerViewImpl.OnPickListener() {
            @Override
            public boolean onPick(List<Place> places) {
                if(mOnPickListener != null) {
                    mOnPickListener.onPick(places);
                }
                hideView();
                return false;
            }
        });
    }

    public void setOnPickListener(OnPickListener onPickListener) {
        this.mOnPickListener = onPickListener;
    }

    @Override
    protected ListenedDrawRelativeLayout createContentView() {
        return mViewImpl.getContentView();
    }

    @Override
    protected String getPopupWdName() {
        return "multiPickerPopupWd";
    }

    public List<Place> getSelectedPlaces() {
        return mViewImpl.getSelectedPlaces();
    }

    /**
     * 重置城市列表，切换至全国
     */
    public void resetPlaceList() {
        mViewImpl.resetPlaceList();
    }

    /**
     * 重置
     */
    public void clear() {
        mPresenter.setOriginalPlaces(null);
        mViewImpl.clearSelectedPlaces();
        mViewImpl.resetPlaceList();
        mViewImpl.refreshHistoryViews();
        mViewImpl.refreshSelectedViews();
    }

    /**
     * 会替换之前所有选择
     * @param placeCode
     */
    public void pickPlace(int placeCode) {
        mPresenter.pickPlace(placeCode);
    }

    /**
     * 会替换之前所有选择
     * @param place
     */
    public void pickPlace(Place place) {
        mPresenter.pickPlace(place);
    }

    /**
     * 会替换之前所有选择
     * @param places
     */
    public void pickPlace(List<Place> places) {
        mPresenter.pickPlace(places);
    }

    /**
     * 在之前选择基础上添加
     * @param placeCode
     */
    public void addSelectedPlace(int placeCode) {
        mPresenter.addPlaceToPicker(placeCode);
    }

    /**
     * 在之前选择基础上添加
     * @param place
     */
    public void addSelectedPlace(Place place) {
        mPresenter.addPlaceToPicker(place);
    }

    /**
     * 在之前选择基础上添加
     * @param places
     */
    public void addSelectedPlaces(List<Place> places) {
        mPresenter.addPlaceToPicker(places);
    }

    @Override
    public void showView(View anchorView) {
        isManualDismiss = false;
        super.showView(anchorView);
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
    protected void onShow() {
        if(mOnPickListener != null) {
            mOnPickListener.onShow();
        }
    }

    @Override
    protected void onDismiss() {
        if(mOnPickListener != null) {
            mOnPickListener.onDismiss();
        }

        if(isManualDismiss) return;

        //没有选择的情况，开始有选过，则恢复之前的选择，否则默认选择全国
        if(CollectionUtil.isEmpty(mPresenter.getOriginalPlaces())) {
            List<Place> temp = new ArrayList<>();
            temp.add(mPresenter.getNationRootPlace());
            pickPlace(temp);
        } else {
            List<Place> temp = new ArrayList<>();
            temp.addAll(mPresenter.getOriginalPlaces());
            pickPlace(temp);
            return;
        }

        if(mOnPickListener != null) {
            mOnPickListener.onPick(mViewImpl.getSelectedPlaces());
        }
    }

    public interface OnPickListener {
        void onPick(List<Place> places);
        void onShow();
        void onDismiss();
    }
}
