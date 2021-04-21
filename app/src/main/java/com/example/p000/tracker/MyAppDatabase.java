package com.example.p000.tracker;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {C_rLocation.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {

    public abstract DataAccessObject dataAccessObject();
}
