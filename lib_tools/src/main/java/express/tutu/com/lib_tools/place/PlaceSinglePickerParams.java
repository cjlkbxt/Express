package express.tutu.com.lib_tools.place;

import android.os.Parcel;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlaceSinglePickerParams extends PlacePickerParams{
    public int placeCode;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.placeCode);
    }

    public PlaceSinglePickerParams() {
    }

    protected PlaceSinglePickerParams(Parcel in) {
        super(in);
        this.placeCode = in.readInt();
    }

    public static final Creator<PlaceSinglePickerParams> CREATOR = new Creator<PlaceSinglePickerParams>() {
        @Override
        public PlaceSinglePickerParams createFromParcel(Parcel source) {
            return new PlaceSinglePickerParams(source);
        }

        @Override
        public PlaceSinglePickerParams[] newArray(int size) {
            return new PlaceSinglePickerParams[size];
        }
    };
}
