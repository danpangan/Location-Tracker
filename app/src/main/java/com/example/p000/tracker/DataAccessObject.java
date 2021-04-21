package com.example.p000.tracker;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DataAccessObject {

    @Insert
    public void addLocation(C_rLocation location);

    @Query("DELETE FROM location")
    public void deleteLocation();

    /*@Query("SELECT * FROM location ORDER BY datetime")
    public List<C_rLocation> sendLocation();*/

    @Query("SELECT * FROM location ORDER BY datetime")
    public List<C_rLocation> sendLocation();

    @Query("UPDATE location SET status = 0 WHERE id = (SELECT id FROM location ORDER BY id DESC LIMIT 1)")
    public void updateLocation();

}
