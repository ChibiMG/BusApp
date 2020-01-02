package fr.istic.mob.busappmaudsaly.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BusRoute.class, Calendar.class, Trip.class, StopTime.class, Stop.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BusRouteDao busRouteDao();
    public abstract TripDao tripDao();
    public abstract CalendarDao calendarDao();
    public abstract StopTimeDao stopTimeDao();
    public abstract StopDao stopDao();
}
