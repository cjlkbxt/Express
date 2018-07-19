package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import express.tutu.com.lib_tools.tools.toast.ToastCompat;
import express.tutu.com.lib_tools.utils.CollectionUtil;
import express.tutu.com.lib_tools.utils.DensityUtil;
import express.tutu.com.lib_tools.R;
import express.tutu.com.lib_tools.utils.ToastUtil;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class BasePlacePickerViewImpl implements PlacePickerContract.IView{
    protected Context context;
    protected PlacePickerContract.IPresenter mPresenter;
    protected GridPlacePicker mGridPlacePicker;

    private PlacePickerParams mParams;

    private ScrollView mScrollView;
    private View mSelectPlaceHistoryWrapper;
    private LinearLayout mSelectPlaceHistoryContainer;

    private ToastCompat mToast;

    public BasePlacePickerViewImpl(Context context, PlacePickerContract.IPresenter presenter, PlacePickerParams params) {
        this.context = context;
        this.mPresenter = presenter;
        this.mParams = params;
    }

    protected void initBaseView(ScrollView scrollView, View selectPlaceHistoryWrapper, LinearLayout selectPlaceHistoryContainer, GridPlacePicker gridPlacePicker) {
        this.mScrollView = scrollView;
        this.mSelectPlaceHistoryWrapper = selectPlaceHistoryWrapper;
        this.mSelectPlaceHistoryContainer = selectPlaceHistoryContainer;
        this.mGridPlacePicker = gridPlacePicker;
        mGridPlacePicker.setOnPickListener(new GridPlacePicker.OnPickListener() {
            @Override
            public void onDataChange(Place parent, List<Place> children) {
            }

            @Override
            public void onPick(Place place) {
                clickPlace(place);
            }
        });

        this.mGridPlacePicker.setColumn(mParams.pickerColumn);
        this.mGridPlacePicker.setIgnoreInvalidPlace(mParams.filterValidPlace);
        this.mGridPlacePicker.setRequiredStyle(mParams.requiredStyle);
        this.mGridPlacePicker.setPickDeep(mParams.pickDeep);

        if(mParams.isShowHistory) {
            selectPlaceHistoryWrapper.setVisibility(View.VISIBLE);
            refreshHistoryViews();
        } else {
            selectPlaceHistoryWrapper.setVisibility(View.GONE);
        }
    }

    @Override
    public void clickPlace(Place place) {
    }

    @Override
    public void pickPlace(int placeCode) {
        Place place = mPresenter.getPlace(placeCode);
        if (place != null) {
            pickPlace(place);
        }
    }

    @Override
    public void pickPlace(Place place) {
        if (mGridPlacePicker == null || place == null) return;
        mGridPlacePicker.pickPlace(place);
    }

    @Override
    public void clearSelectedPlaces() {
        if (mGridPlacePicker == null) return;
        mGridPlacePicker.clearSelectedPlaces();
    }

    @Override
    public void refreshHistoryViews() {
        if (!mParams.isShowHistory || mSelectPlaceHistoryWrapper == null) return;
        List<Place> recentPlaces = mPresenter.getRecentHistory(mParams.maxHistoryCount);

        if (CollectionUtil.isEmpty(recentPlaces)) {
            mSelectPlaceHistoryWrapper.setVisibility(View.GONE);
        } else {
            mSelectPlaceHistoryContainer.removeAllViews();
            for (int i = 0; i < mParams.maxHistoryCount; i++) {
                if (recentPlaces.size() > i) {
                    addPlaceHistoryItem(recentPlaces.get(i), i);
                } else {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(DensityUtil.dip2px(context, 6), 0, 0, 0);
                    lp.weight = 1;
                    mSelectPlaceHistoryContainer.addView(new View(context), lp);
                }
            }
            mSelectPlaceHistoryWrapper.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void switchPickerList(Place place) {
        if (mGridPlacePicker == null || place == null) return;
        mGridPlacePicker.prepareSwitchPlaceList(place, false);
    }

    @Override
    public void resetPlaceList() {
        if (mGridPlacePicker == null) return;
        mGridPlacePicker.resetPlaceList();
    }

    @Override
    public Place getCurrentPlace() {
        if(mGridPlacePicker == null) return null;
        return mGridPlacePicker.getCurrentPlace();
    }

    @Override
    public void scrollToTop() {
        if(mScrollView == null) return;
        mScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    public void showToast(Context context, String content) {
        ToastUtil.showToast(context, content);
    }

    private void addPlaceHistoryItem(final Place place, int position) {
        if (place == null || place.getCode() < 0) return;
        TextView placeTv = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_pick_place_history, null);
        placeTv.setTextColor(ContextCompat.getColorStateList(context, R.color.selector_place_text_color));
        placeTv.setText(place.getShortName());
        placeTv.setTag(place);
        placeTv.setSelected(mGridPlacePicker.getSelectedPlaces().contains(place));
        placeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlace(place);
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (position != 0) {
            lp.setMargins(DensityUtil.dip2px(context, 6), 0, 0, 0);
        }
        lp.weight = 1;
        mSelectPlaceHistoryContainer.addView(placeTv, lp);
    }
}
