package com.brok.patapata;

public class POJO_requests {
    private String userid;
    private  String driverid;
    private String litres;
    private Double latitude;
    private Double longitude;

    public POJO_requests() {
    }
    public POJO_requests(String userid, String driverid, String litres, Double latitude, Double longitude) {
        this.userid = userid;
        this.driverid = driverid;
        this.litres = litres;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserid() {
        return userid;
    }



    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }



    public String getLitres() {
        return litres;
    }

    public void setLitres(String litres) {
        this.litres = litres;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
