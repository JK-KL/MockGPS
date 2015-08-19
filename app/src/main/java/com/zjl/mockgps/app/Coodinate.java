package com.zjl.mockgps.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by C0dEr on 15/8/7.
 */
public class Coodinate implements Parcelable {
    public Double latitude;
    public Double longitude;

    public Coodinate setCood(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }

    public static final Parcelable.Creator<Coodinate> CREATOR= new Creator<Coodinate>() {
        @Override
        public Coodinate createFromParcel(Parcel parcel) {
            Coodinate cood=new Coodinate();
            cood.latitude=parcel.readDouble();
            cood.longitude=parcel.readDouble();
            return cood;
        }

        @Override
        public Coodinate[] newArray(int i) {
            return new Coodinate[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

}
