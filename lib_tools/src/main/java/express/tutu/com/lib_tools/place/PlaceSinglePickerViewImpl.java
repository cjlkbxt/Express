package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;

import express.tutu.com.lib_tools.R;
import express.tutu.com.lib_tools.utils.CollectionUtil;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlaceSinglePickerViewImpl extends BasePlacePickerViewImpl implements PlacePickerContract.IPlaceSinglePickerView{
    private LayoutInflater mInflater;
    private ListenedDrawRelativeLayout mContentView;

    private PlaceSinglePickerPresenter mPresenter;

    private OnPickListener mOnPickListener;

    public void setOnPickListener(OnPickListener onPickListener) {
        this.mOnPickListener = onPickListener;
    }

    public PlaceSinglePickerViewImpl(Context context, PlaceSinglePickerPresenter presenter, PlaceSinglePickerParams params) {
        super(context, presenter, params);
        this.mPresenter = presenter;
        mInflater = LayoutInflater.from(context);
        mContentView = (ListenedDrawRelativeLayout) mInflater.inflate(R.layout.common_bs_pop_grid_place_picker, null);

        initView();
    }

    public ListenedDrawRelativeLayout getContentView() {
        return mContentView;
    }

    private void initView() {
        ScrollView mScrollView = (ScrollView) mContentView.findViewById(R.id.scrollView);
        GridPlacePicker gridPlacePicker = (GridPlacePicker) mContentView.findViewById(R.id.gridPlacePicker);
        gridPlacePicker.initData();
        View selectPlaceHistoryWrapper = mContentView.findViewById(R.id.select_place_history_wrapper);
        LinearLayout selectPlaceHistoryContainer = (LinearLayout) mContentView.findViewById(R.id.select_place_history_container);

        initBaseView(mScrollView, selectPlaceHistoryWrapper, selectPlaceHistoryContainer, gridPlacePicker);

        if(mPresenter.getOriginalPlace() != null) {
            pickPlace(mPresenter.getOriginalPlace());
            refreshHistoryViews();
        }
    }

    @Override
    public void clickPlace(Place place) {
        super.clickPlace(place);
        mPresenter.pickPlace(place);
        mPresenter.storeToHistory(place);
        mPresenter.setOriginalPlace(place);
        if(mOnPickListener != null) {
            boolean block = mOnPickListener.onPick(place);
            if(block) return;
        }
        refreshHistoryViews();
    }

    @Override
    public Place getSelectedPlace() {
        if(mGridPlacePicker == null) return null;
        List<Place> selectedPlaces = mGridPlacePicker.getSelectedPlaces();
        if(CollectionUtil.isEmpty(selectedPlaces)) return null;
        return selectedPlaces.get(0);
    }

    public interface OnPickListener {
        boolean onPick(Place place);
    }
}
