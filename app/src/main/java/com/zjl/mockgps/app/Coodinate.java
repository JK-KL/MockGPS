package com.zjl.mockgps.app;

import android.os.Parcel;
import android.os.Parcelable;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

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

    public static Coodinate La2Cood(LatLng l) {
        Coodinate coodinate = new Coodinate();
        coodinate.longitude = l.longitude;
        coodinate.latitude = l.latitude;
        return coodinate;
    }

    public static List<Coodinate> La2Cood(List<LatLng> l) {
        List<Coodinate> coodinate = new ArrayList<Coodinate>();
        for (LatLng ll : l) {
            Coodinate coodinate1 = new Coodinate();
            coodinate1.longitude = ll.longitude;
            coodinate1.latitude = ll.latitude;
            coodinate.add(coodinate1);
        }
        return coodinate;
    }

    public static final Parcelable.Creator<Coodinate> CREATOR = new Creator<Coodinate>() {
        @Override
        public Coodinate createFromParcel(Parcel parcel) {
            Coodinate cood = new Coodinate();
            cood.latitude = parcel.readDouble();
            cood.longitude = parcel.readDouble();
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
