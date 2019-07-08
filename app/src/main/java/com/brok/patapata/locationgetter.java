package com.brok.patapata;

import com.google.android.gms.maps.model.LatLng;

public class locationgetter {
    private Double latitude;
    private Double longitude;
    LatLng location;
    private String id;

    public LatLng getLocation() {
        return location;
    }

    public String getCurrentID(){
        return id;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public locationgetter(LatLng location) {
        this.location = location;

    }

    public locationgetter() {
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
