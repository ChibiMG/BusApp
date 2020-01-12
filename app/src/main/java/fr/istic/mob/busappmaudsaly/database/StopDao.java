package fr.istic.mob.busappmaudsaly.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface StopDao {

    @Query("SELECT * FROM Stop")
    List<Stop> getAll();

    @Query("select * from stop\n" +
            "natural join stop_time\n" +
            "where trip_id = (select trip_id from stop_time\n" +
            "natural join trip\n" +
            "natural join calendar\n" +
            "where (sunday = (:dayOfWeek = 1) or monday = (:dayOfWeek = 2) or tuesday = (:dayOfWeek = 3) or wednesday = (:dayOfWeek = 4) or thursday = (:dayOfWeek = 5) or friday = (:dayOfWeek = 6) or saturday = (:dayOfWeek = 7))\n" +
            "and start_date <= :currentDate\n" +
            "and route_id = :currentRouteId\n" +
            "and direction_id = :currentDirection\n" +
            "and arrival_time > :currentTime\n" +
            "order by arrival_time\n" +
            "limit 1)\n" +
            "order by arrival_time")
    List<Stop> getBusArret(int dayOfWeek, int currentDate, int currentRouteId, int currentDirection, String currentTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Stop> stops);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Stop stop);

    @Query("SELECT COUNT(*) FROM Stop")
    int count();
}
