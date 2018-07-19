package express.tutu.com.lib_tools.place;

import android.content.Context;

import java.util.List;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlacePickerContract {
    public interface IView {
        void clickPlace(Place place);
        void pickPlace(Place place);
        void pickPlace(int placeCode);
        void clearSelectedPlaces();
        void refreshHistoryViews();
        void switchPickerList(Place place);
        void resetPlaceList();
        void scrollToTop();
        void showToast(Context context, String content);
        Place getCurrentPlace();
    }

    public interface IPlaceMultiPickerView {
        void pickPlaces(List<Place> places);
        void addPlaceToPicker(Place place);
        void addPlacesToPicker(List<Place> places);
        void removeSelectedPlace(Place place);
        void removeSelectedPlaces(List<Place> places);
        void refreshSelectedViews();
        List<Place> getSelectedPlaces();
    }

    public interface IPlaceSinglePickerView {
        Place getSelectedPlace();
    }

    public interface IPresenter {
        void detach();
        void pickPlace(Place place);
        void pickPlace(int placeCode);
        void storeToHistory(Place place);
        List<Place> getRecentHistory(int recentCount);
        Place getPlace(int placeCode);
        String getFullPlaceName(Place place);
        Place getNationRootPlace();
        List<Place> getPlaceList(List<String> placeCodes);
    }

    public interface IPlaceMultiPickerPresenter extends IPresenter {
        void attach(PlaceMultiPickerViewImpl iView);
        void addPlaceToPicker(int placeCode);
        void addPlaceToPicker(Place place);
        void addPlaceToPicker(List<Place> places);
        void pickPlace(List<Place> places);
        void setOriginalPlaces(List<Place> places);
        List<Place> getOriginalPlaces();
    }

    public interface IPlaceSinglePickerPresenter extends IPresenter {
        void attach(PlaceSinglePickerViewImpl iView);
        void setOriginalPlace(Place place);
        Place getOriginalPlace();
    }
}
