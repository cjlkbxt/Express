package express.tutu.com.lib_tools.place;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlacePickerParams implements Parcelable{
    public String title;
    //最大历史数
    public int maxHistoryCount = 3;
    //选择器列数
    public int pickerColumn = 4;
    //可选择到的级别
    public int pickDeep = Place.DEPTH_TOWN;
    //是否只取有效place
    public boolean filterValidPlace;
    public boolean isShowHistory;
    //显示历史时，设置的标识
    public String historyPosition;
    //是否出现不限xx 比如：GridPlacePicker.STYLE_NOT_REQUIRED
    public int requiredStyle = GridPlacePicker.STYLE_NOT_REQUIRED;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.maxHistoryCount);
        dest.writeInt(this.pickerColumn);
        dest.writeInt(this.pickDeep);
        dest.writeByte(this.filterValidPlace ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowHistory ? (byte) 1 : (byte) 0);
        dest.writeString(this.historyPosition);
        dest.writeInt(this.requiredStyle);
    }

    public PlacePickerParams() {
    }

    protected PlacePickerParams(Parcel in) {
        this.title = in.readString();
        this.maxHistoryCount = in.readInt();
        this.pickerColumn = in.readInt();
        this.pickDeep = in.readInt();
        this.filterValidPlace = in.readByte() != 0;
        this.isShowHistory = in.readByte() != 0;
        this.historyPosition = in.readString();
        this.requiredStyle = in.readInt();
    }

    public static final Parcelable.Creator<PlacePickerParams> CREATOR = new Parcelable.Creator<PlacePickerParams>() {
        @Override
        public PlacePickerParams createFromParcel(Parcel in) {
            return new PlacePickerParams(in);
        }

        @Override
        public PlacePickerParams[] newArray(int size) {
            return new PlacePickerParams[size];
        }
    };
}
