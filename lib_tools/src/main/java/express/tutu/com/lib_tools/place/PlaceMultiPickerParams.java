package express.tutu.com.lib_tools.place;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cjlkbxt on 2018/7/18/018.
 */

public class PlaceMultiPickerParams extends PlacePickerParams{

    //最大可选数
    public int maxPicked = 5;
    //必须选择，如果空列表，选择当前城市（即当前列表所属城市）
    public boolean mustSelect;
    public List<String> mPlaceCodes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.maxPicked);
        dest.writeByte(this.mustSelect ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.mPlaceCodes);
    }

    public PlaceMultiPickerParams() {
    }

    protected PlaceMultiPickerParams(Parcel in) {
        super(in);
        this.maxPicked = in.readInt();
        this.mustSelect = in.readByte() != 0;
        this.mPlaceCodes = in.createStringArrayList();
    }

    public static final Parcelable.Creator<PlaceMultiPickerParams> CREATOR = new Parcelable.Creator<PlaceMultiPickerParams>() {
        @Override
        public PlaceMultiPickerParams createFromParcel(Parcel source) {
            return new PlaceMultiPickerParams(source);
        }

        @Override
        public PlaceMultiPickerParams[] newArray(int size) {
            return new PlaceMultiPickerParams[size];
        }
    };
}
