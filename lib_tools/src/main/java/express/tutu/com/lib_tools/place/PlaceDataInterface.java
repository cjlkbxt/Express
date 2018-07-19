package express.tutu.com.lib_tools.place;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public interface PlaceDataInterface {
    Place getPlace(int code);

    Place getParent(Place child);

    Place getParentCity(Place child);

    List<Place> getSiblingsPlaces(Place p);

    boolean hasChildren(Place place);

    boolean hasChildren(int parentCode);

    List<Place> getChildren(int parentCode);

    List<Place> getCityList(int deep, int parentCodeId);

    Place getCityByString(String province, String city, String district);

    Place getNationRootPlace();

    /**
     * lat+","+lon
     */
    @Nullable
    String getLonLatStr(int city);

    /**
     * [0]-parent short name,[1]-self short name
     */
    String[] getParent_ChildName(int id);

    String getFullCityNameByCode(int code, String separator);

    /**
     * parent shortname+"-"+self shortname
     * self shortname
     */
    String getParentChildStr(int cityCode);

    /**
     * parent shortname+" "+self shortname
     * self shortname
     */
    String getParentSpaceChildStr(int cityCode);

    /**
     * parent name + self name
     * self shortname
     */
    String getFullName(int cityCode);

    boolean isInvalidateCity(int city);
}
