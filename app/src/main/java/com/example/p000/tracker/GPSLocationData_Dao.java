package com.example.p000.tracker;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface GPSLocationData_Dao {

    @Insert
    public void addLocData(E_GPSLocationData gpsLocationData);


}
