package com.example.p000.tracker;

public class C_Details {

    private Double longitude, latitude;
    private String address, datetime;
    private int status, is_start, is_end;


    public C_Details(Double longitude, Double latitude, String address, String datetime, int isStart, int isEnd, int status) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.datetime = datetime;
        this.status = status;
        this.is_start = isStart;
        this.is_end = isEnd;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getStatus() {
        return status;
    }

    public int getIs_start() {
        return is_start;
    }

    public int getIs_end() {
        return is_end;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setIs_start(int is_start) {
        this.is_start = is_start;
    }

    public void setIs_end(int is_end) {
        this.is_end = is_end;
    }
}
