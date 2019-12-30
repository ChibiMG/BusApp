package fr.istic.mob.busappmaudsaly.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BusRoute.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BusRouteDao busRouteDao();
}
