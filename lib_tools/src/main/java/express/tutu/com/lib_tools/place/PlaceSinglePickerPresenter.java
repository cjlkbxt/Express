package express.tutu.com.lib_tools.place;

import android.content.Context;

import java.util.List;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlaceSinglePickerPresenter implements PlacePickerContract.IPlaceSinglePickerPresenter{
    private Context mContext;
    private PlaceModel mPlaceModel;
    private PlaceSinglePickerParams mPickerParams;
    private PlaceSinglePickerViewImpl mViewImpl;

    private Place mOriginalPlace;

    public PlaceSinglePickerPresenter(android.content.Context context, PlaceSinglePickerParams params) {
        this.mContext = context;
        this.mPickerParams = params;
        mPlaceModel = new PlaceModel(context, mPickerParams.filterValidPlace, mPickerParams.isShowHistory);
    }

    @Override
    public void attach(PlaceSinglePickerViewImpl iView) {
        mViewImpl = iView;
    }

    @Override
    public void detach() {
        mViewImpl = null;
        mContext = null;
    }

    @Override
    public void pickPlace(Place place) {
        mOriginalPlace = place;
        if(mViewImpl != null) {
            mViewImpl.pickPlace(place);
            mViewImpl.refreshHistoryViews();
        }
    }

    @Override
    public void pickPlace(int placeCode) {
        mOriginalPlace = getPlace(placeCode);
        if(mViewImpl != null) {
            mViewImpl.pickPlace(placeCode);
            mViewImpl.refreshHistoryViews();
        }
    }

    @Override
    public void storeToHistory(Place place) {
        if(!mPickerParams.isShowHistory) return;
        mPlaceModel.storeToHistory(place, mPickerParams.historyPosition);
    }

    @Override
    public List<Place> getRecentHistory(int recentCount) {
        return mPlaceModel.getRecentHistory(recentCount, mPickerParams.historyPosition);
    }

    @Override
    public Place getPlace(int placeCode) {
        return mPlaceModel.getPlace(placeCode);
    }

    @Override
    public String getFullPlaceName(Place place) {
        return mPlaceModel.getFullPlaceName(place);
    }

    @Override
    public Place getNationRootPlace() {
        return mPlaceModel.getNationRootPlace();
    }

    @Override
    public List<Place> getPlaceList(List<String> placeCodes) {
        return mPlaceModel.getPlaceList(placeCodes);
    }

    @Override
    public void setOriginalPlace(Place place) {
        mOriginalPlace = place;
    }

    @Override
    public Place getOriginalPlace() {
        return mOriginalPlace;
    }
}
