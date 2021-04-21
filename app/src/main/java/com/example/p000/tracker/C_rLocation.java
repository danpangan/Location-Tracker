package com.example.p000.tracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "location")
public class C_rLocation {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "latitude")
    private Double latitude;
    @ColumnInfo(name = "longitude")
    private Double longitude;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "datetime")
    private String datetime;
    @ColumnInfo(name = "status")
    private int status;
    @ColumnInfo(name = "isStart")
    private int isStart;
    @ColumnInfo(name = "isEnd")
    private int isEnd;

    public int getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
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

    public int getIsStart() {
        return isStart;
    }

    public int getIsEnd() {
        return isEnd;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }
}
