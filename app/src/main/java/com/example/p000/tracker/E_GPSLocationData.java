package com.example.p000.tracker;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "gpslocation_data")
public class E_GPSLocationData {

    @PrimaryKey
    @NonNull
    public Integer user_id;

    public Double latitude;
    public Double longitude;
    public String date_recorded;
    public Integer is_sent;
    public Integer is_start;
    public Integer is_end;


}
