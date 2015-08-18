package com.zjl.mockgps.app;

/**
 * Created by C0dEr on 15/8/7.
 */
public class Coodinate {
    public Double latitude;
    public Double longitude;

    public Coodinate setCood(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        return  this;
    }
}
