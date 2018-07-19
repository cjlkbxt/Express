package express.tutu.com.lib_tools.place;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class ConfigOptionBean implements Parcelable{

//    @SerializedName("optionValue")
    private String optionValue;//选项值；数据类型String
//    @SerializedName("optionName")
    private String optionName;//选项名称;数据类型string
    public ConfigOptionBean(String value,String optionName){
        this.optionName = optionName;
        this.optionValue = value;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public ConfigOptionBean(Parcel in){
        this.optionValue = in.readString();
        this.optionName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.optionValue);
        dest.writeString(this.optionName);
    }


    public static final Creator<ConfigOptionBean> CREATOR = new Creator<ConfigOptionBean>() {
        @Override
        public ConfigOptionBean createFromParcel(Parcel source) {
            return new ConfigOptionBean(source);
        }

        @Override
        public ConfigOptionBean[] newArray(int size) {
            return new ConfigOptionBean[size];
        }
    };
}
