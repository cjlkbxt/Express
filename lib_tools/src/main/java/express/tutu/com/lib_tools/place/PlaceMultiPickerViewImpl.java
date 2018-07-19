package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import express.tutu.com.lib_tools.R;
import express.tutu.com.lib_tools.utils.CollectionUtil;
import express.tutu.com.lib_tools.utils.DensityUtil;

/**
 * Created by cjlkbxt on 2018/7/18/018.
 */

public class PlaceMultiPickerViewImpl extends BasePlacePickerViewImpl implements PlacePickerContract.IPlaceMultiPickerView{
    private LayoutInflater mInflater;
    private ListenedDrawRelativeLayout mContentView;
    private FlowLayout mSelectedPlacesContainer;
    private View clearSelectedBtn;
    private View confirmBtn;

    private PlaceMultiPickerPresenter mPresenter;
    private PlaceMultiPickerParams mPickerParams;
    private OnPickListener mOnPickListener;

    public void setOnPickListener(OnPickListener onPickListener) {
        this.mOnPickListener = onPickListener;
    }

    public PlaceMultiPickerViewImpl(Context context, PlaceMultiPickerPresenter presenter, PlaceMultiPickerParams params) {
        super(context, presenter, params);
        this.mPickerParams = params;
        this.mPresenter = presenter;
        mInflater = LayoutInflater.from(context);
        mContentView = (ListenedDrawRelativeLayout) mInflater.inflate(R.layout.grid_multiple_place_picker, null);

        initView();
        initEvent();
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

        if(!CollectionUtil.isEmpty(mPresenter.getOriginalPlaces())) {
            pickPlaces(mPresenter.getOriginalPlaces());
            refreshHistoryViews();
        }

        mSelectedPlacesContainer = (FlowLayout) mContentView.findViewById(R.id.selected_place_container);
        clearSelectedBtn = mContentView.findViewById(R.id.selected_place_clear_btn);
        confirmBtn = mContentView.findViewById(R.id.confirm_btn);
        refreshSelectedViews();
    }

    private void initEvent() {
        clearSelectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectedPlaces();
                refreshHistoryViews();
                refreshSelectedViews();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CollectionUtil.isEmpty(getSelectedPlaces())) {
                    if(mPickerParams.mustSelect) {
                        if (mOnPickListener != null) {
                            mOnPickListener.onPick(getSelectedPlaces());
                        }
                        return;
                    }
                    Place currentPlace = getCurrentPlace();
                    mPresenter.storeToHistory(currentPlace);

                    mPresenter.setOriginalPlaces(getSelectedPlaces());

                    if (mOnPickListener != null) {
                        List<Place> places = new ArrayList<>();
                        places.add(currentPlace);
                        boolean block = mOnPickListener.onPick(places);
                        if(block) return;
                    }

                    mPresenter.pickPlace(currentPlace);
                } else {
                    for (int i = 0; i < getSelectedPlaces().size(); i++) {
                        mPresenter.storeToHistory(getSelectedPlaces().get(i));
                    }
                    mPresenter.setOriginalPlaces(getSelectedPlaces());

                    if (mOnPickListener != null) {
                        boolean block = mOnPickListener.onPick(getSelectedPlaces());
                        if(block) return;
                    }

                    refreshHistoryViews();
                }
            }
        });
    }

    @Override
    public void pickPlaces(List<Place> places) {
        if(mGridPlacePicker == null) return;
        mGridPlacePicker.pickPlaces(places);
    }

    @Override
    public void clickPlace(Place place) {
        mPresenter.addPlaceToPicker(place);
    }

    @Override
    public void addPlaceToPicker(Place place) {
        if(mGridPlacePicker == null) return;
        mGridPlacePicker.addSelectedPlace(place);
    }

    @Override
    public void addPlacesToPicker(List<Place> places) {
        if(mGridPlacePicker == null) return;
        mGridPlacePicker.addSelectedPlaces(places);
    }

    @Override
    public void removeSelectedPlace(Place place) {
        if(mGridPlacePicker == null) return;
        mGridPlacePicker.removeSelectedPlace(place);
    }

    @Override
    public void removeSelectedPlaces(List<Place> places) {
        if(mGridPlacePicker == null) return;
        mGridPlacePicker.removeSelectedPlaces(places);
    }

    @Override
    public List<Place> getSelectedPlaces() {
        if(mGridPlacePicker == null) return new ArrayList<>();
        return mGridPlacePicker.getSelectedPlaces();
    }

    @Override
    public void refreshSelectedViews() {
        if(mSelectedPlacesContainer == null) return;
        mSelectedPlacesContainer.removeAllViews();
        for (int i = 0; i < getSelectedPlaces().size(); i++) {
            final Place place = getSelectedPlaces().get(i);
            addPlaceItemView(place);
        }
        observeSelectedView();
    }

    private void addPlaceItemView(final Place place) {
        final View selectedPlaceItem = mInflater.inflate(R.layout.layout_place_selected_item, null);
        TextView placeTv = (TextView) selectedPlaceItem.findViewById(R.id.place_name_tv);
        placeTv.setText(place.getShortName());
        selectedPlaceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedPlacesContainer.removeView(selectedPlaceItem);
                observeSelectedView();
                removeSelectedPlace(place);
                refreshHistoryViews();
            }
        });
        FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        lp.rightMargin = DensityUtil.dip2px(context, 6);
        lp.bottomMargin = DensityUtil.dip2px(context, 10);
        mSelectedPlacesContainer.addView(selectedPlaceItem, lp);
    }

    private void observeSelectedView() {
        if(mSelectedPlacesContainer.getChildCount() == 0) {
            mSelectedPlacesContainer.setVisibility(View.GONE);
            clearSelectedBtn.setVisibility(View.GONE);
        } else {
            mSelectedPlacesContainer.setVisibility(View.VISIBLE);
            clearSelectedBtn.setVisibility(View.VISIBLE);
        }
    }

    public interface OnPickListener {
        boolean onPick(List<Place> places);
    }
}
