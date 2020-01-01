package fr.istic.mob.busappmaudsaly.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BusRoute.class, Trip.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BusRouteDao busRouteDao();
    public abstract TripDao tripDao();
}
