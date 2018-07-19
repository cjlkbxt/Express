package express.tutu.com.lib_tools.place;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import express.tutu.com.lib_tools.utils.CollectionUtil;

/**
 * Created by cjlkbxt on 2018/7/18/018.
 */

public class PlaceMultiPickerPresenter implements PlacePickerContract.IPlaceMultiPickerPresenter{
    private static final String ALERT_MAX_SELECTED_PLACE = "%s最多选%d个";
    private static final String ALERT_CONTAIN_SELF = "【%s】已被选择";
    private static final String ALERT_CONTAIN_CHILDREN = "【%s】已经替换了所有下级地区";
    private static final String ALERT_CONTAIN_PARENT = "【%s】已替换";

    private Context mContext;
    private PlaceModel mPlaceModel;
    private PlaceMultiPickerParams mPickerParams;
    private PlaceMultiPickerViewImpl mViewImpl;

    private List<Place> mOriginalPlaces = new ArrayList<>();

    public PlaceMultiPickerPresenter(Context context, PlaceMultiPickerParams params) {
        this.mContext = context;
        this.mPickerParams = params;
        mPlaceModel = new PlaceModel(context, mPickerParams.filterValidPlace, mPickerParams.isShowHistory);
    }

    @Override
    public void attach(PlaceMultiPickerViewImpl view) {
        mViewImpl = view;
    }

    @Override
    public void detach() {
        mViewImpl = null;
        mContext = null;
    }

    @Override
    public void setOriginalPlaces(List<Place> places) {
        this.mOriginalPlaces.clear();
        if(CollectionUtil.isEmpty(places)) return;
        this.mOriginalPlaces.addAll(places);
    }

    @Override
    public List<Place> getOriginalPlaces() {
        return mOriginalPlaces;
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
    public void pickPlace(Place place) {
        if(place == null) return;
        ArrayList<Place> places = new ArrayList<>();
        places.add(place);
        setOriginalPlaces(places);
        mViewImpl.pickPlace(place);
        mViewImpl.refreshSelectedViews();
        mViewImpl.refreshHistoryViews();
    }

    @Override
    public void pickPlace(int placeCode) {
        if(placeCode < 0) return;
        Place place = mPlaceModel.getPlace(placeCode);
        if(place == null) return;
        pickPlace(place);
    }

    @Override
    public void pickPlace(List<Place> places) {
        if(CollectionUtil.isEmpty(places)) return;
        setOriginalPlaces(places);
        mViewImpl.pickPlaces(places);
        mViewImpl.refreshHistoryViews();
        mViewImpl.refreshSelectedViews();
    }

    @Override
    public void addPlaceToPicker(Place place) {
        if (place == null) return;
        if(hasSelected(place)) {
            mViewImpl.removeSelectedPlace(place);
            mViewImpl.refreshSelectedViews();
            mViewImpl.refreshHistoryViews();
            return;
        }
        List<Place> placeList = mPlaceModel.getLinealPlaceList(place);
        if(!checkPlaceValid(placeList)) {
            return;
        }
        mViewImpl.addPlaceToPicker(place);
        mViewImpl.refreshSelectedViews();
        mViewImpl.refreshHistoryViews();
    }

    @Override
    public void addPlaceToPicker(int placeCode) {
        if(placeCode < 0) return;
        Place place = mPlaceModel.getPlace(placeCode);
        if(place == null) return;
        addPlaceToPicker(place);
    }

    @Override
    public void addPlaceToPicker(List<Place> places) {
        if(CollectionUtil.isEmpty(places)) return;

        List<Place> temp = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            List<Place> placeList = mPlaceModel.getLinealPlaceList(places.get(i));
            if(checkPlaceValid(placeList)) {
                temp.add(places.get(i));
            }
        }

        mViewImpl.addPlacesToPicker(temp);
        mViewImpl.refreshHistoryViews();
        mViewImpl.refreshSelectedViews();
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
    public List<Place> getPlaceList(List<String> codes) {
        return mPlaceModel.getPlaceList(codes);
    }

    @Override
    public Place getNationRootPlace() {
        return mPlaceModel.getNationRootPlace();
    }

    /**
     * @param placeList city层级关系
     * @return
     */
    private boolean checkPlaceValid(List<Place> placeList) {
        if(CollectionUtil.isEmpty(placeList)) return false;
        Place place = placeList.get(placeList.size() - 1);
        //选择过 return false
        if(hasSelected(place)) return false;
        //如果添加过“全国” 删除“全国”
        deleteNationIfExist();
        //已选择parent 删除parent
        deleteParentIfExist(place);
        //选择过children 删除children return true
        deleteChildrenIfExist(place);
        //达到选择上限 return false
        if(checkExceedMaxSelectedPlaceCount()) return false;
        return true;
    }

    private boolean hasSelected(Place place) {
        if(place == null) return false;
        if(mViewImpl.getSelectedPlaces().contains(place)) {
//            mViewImpl.showToast(mContext, String.format(ALERT_CONTAIN_SELF, place.getName()));
            return true;
        }
        return false;
    }

    private void deleteParentIfExist(Place place) {
        if(place == null) return;
        List<Place> needRemovePlaces = new ArrayList<>();
        List<Place> linealPlaceList = mPlaceModel.getLinealPlaceList(place);
        for (int i = 0; i < linealPlaceList.size(); i++) {
            for (int j = 0; j < mViewImpl.getSelectedPlaces().size(); j++) {
                if (linealPlaceList.get(i).getCode() == mViewImpl.getSelectedPlaces().get(j).getCode()) {
                    needRemovePlaces.add(mViewImpl.getSelectedPlaces().get(j));
                    break;
                }
            }
        }

        if(CollectionUtil.isNotEmpty(needRemovePlaces)) {
            mViewImpl.removeSelectedPlaces(needRemovePlaces);
            String alertStr = String.format(ALERT_CONTAIN_PARENT, place.getName());
            for (int i = 0; i < needRemovePlaces.size(); i++) {
                if(i != 0) alertStr += " ";
                alertStr += needRemovePlaces.get(i).getName();
            }
            mViewImpl.showToast(mContext, alertStr);
        }
    }

    private void deleteChildrenIfExist(Place place) {
        if(place == null) return;
        if(mPlaceModel.isNation(place)) {
            if(!CollectionUtil.isEmpty(mViewImpl.getSelectedPlaces())) {
                mViewImpl.showToast(mContext, String.format(ALERT_CONTAIN_CHILDREN, place.getName()));
            }
            mViewImpl.clearSelectedPlaces();
            return;
        }
        List<Place> needRemovePlaces = new ArrayList<>();
        Iterator<Place> iterator = mViewImpl.getSelectedPlaces().iterator();
        while (iterator.hasNext()) {
            Place selectedPlace = iterator.next();
            List<Place> orderedCityList = mPlaceModel.getLinealPlaceList(selectedPlace);
            for (int i = 0; i < orderedCityList.size(); i++) {
                if (orderedCityList.get(i).getCode() == place.getCode()) {
                    needRemovePlaces.add(selectedPlace);
                    break;
                }
            }
        }
        if(CollectionUtil.isNotEmpty(needRemovePlaces)) {
            mViewImpl.removeSelectedPlaces(needRemovePlaces);
            mViewImpl.showToast(mContext, String.format(ALERT_CONTAIN_CHILDREN, place.getName()));
        }
    }

    private boolean checkExceedMaxSelectedPlaceCount() {
        if(mViewImpl.getSelectedPlaces().size() == mPickerParams.maxPicked) {
            String alertPos = "";
            if("XFindGoodsContainerFragment_START".equals(mPickerParams.historyPosition)) {
                alertPos = "起始地";
            } else if ("XFindGoodsContainerFragment_END".equals(mPickerParams.historyPosition)) {
                alertPos = "目的地";
            }
            mViewImpl.showToast(mContext, String.format(ALERT_MAX_SELECTED_PLACE, alertPos, mPickerParams.maxPicked));
            return true;
        }
        return false;
    }

    /**
     * 选择其他地区时 删除全国
     */
    private void deleteNationIfExist() {
        Iterator<Place> iterator = mViewImpl.getSelectedPlaces().iterator();
        while (iterator.hasNext()) {
            if(mPlaceModel.isNation(iterator.next())) {
                iterator.remove();
                return;
            }
        }
    }

}
