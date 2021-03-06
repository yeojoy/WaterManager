package me.yeojoy.watermanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeojoy on 15. 6. 9..
 */
public class MyWater implements Parcelable {

    private int id = -1;

    /** date format > yyyy-MM-dd */
    private String drinkingDate;
    /** time format(24Hours) > HH:mm:ss */
    private String drinkingTime;
    private int drinkingQuantity;

    public MyWater() {}

    public MyWater(int id, String drinkingDate, String drinkingTime, int drinkingQuantity) {
        this.id = id;
        this.drinkingDate = drinkingDate;
        this.drinkingTime = drinkingTime;
        this.drinkingQuantity = drinkingQuantity;
    }

    public MyWater(Parcel in) {
        readToParcel(in);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrinkingDate() {
        return drinkingDate;
    }

    public void setDrinkingDate(String drinkingDate) {
        this.drinkingDate = drinkingDate;
    }

    public String getDrinkingTime() {
        return drinkingTime;
    }

    public void setDrinkingTime(String drinkingTime) {
        this.drinkingTime = drinkingTime;
    }

    public int getDrinkingQuantity() {
        return drinkingQuantity;
    }

    public void setDrinkingQuantity(int drinkingQuantity) {
        this.drinkingQuantity = drinkingQuantity;
    }

    @Override
    public String toString() {
        return "MyWater{" +
                "id=" + id +
                ", drinkingDate='" + drinkingDate + '\'' +
                ", drinkingTime='" + drinkingTime + '\'' +
                ", drinkingQuantity='" + drinkingQuantity + '\'' +
                '}';
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MyWater createFromParcel(Parcel in) {
            return new MyWater(in);
        }

        public MyWater[] newArray(int size) {
            return new MyWater[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(drinkingDate);
        parcel.writeString(drinkingTime);
        parcel.writeInt(drinkingQuantity);
    }

    public void readToParcel(Parcel parcel) {
        setId(parcel.readInt());
        setDrinkingDate(parcel.readString());
        setDrinkingTime(parcel.readString());
        setDrinkingQuantity(parcel.readInt());
    }
}
