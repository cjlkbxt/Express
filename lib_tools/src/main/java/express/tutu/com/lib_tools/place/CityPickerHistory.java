package express.tutu.com.lib_tools.place;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

@Entity(nameInDb = "CITY_PICKER_HISTORY")
public class CityPickerHistory {

    @Property(nameInDb = "TIME")
    private java.util.Date time;
    @NonNull
    @Property(nameInDb = "CITY")
    private int city;
    @Property(nameInDb = "POSITION")
    private String position;
    @Id
    @Property(nameInDb = "POSITION_CITY")
    private String position_city;
    @Generated(hash = 331861450)
    public CityPickerHistory(java.util.Date time, int city, String position,
                             String position_city) {
        this.time = time;
        this.city = city;
        this.position = position;
        this.position_city = position_city;
    }
    @Generated(hash = 1862652046)
    public CityPickerHistory() {
    }
    public java.util.Date getTime() {
        return this.time;
    }
    public void setTime(java.util.Date time) {
        this.time = time;
    }
    public int getCity() {
        return this.city;
    }
    public void setCity(int city) {
        this.city = city;
    }
    public String getPosition() {
        return this.position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getPosition_city() {
        return this.position_city;
    }
    public void setPosition_city(String position_city) {
        this.position_city = position_city;
    }
}
