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
            "where (sunday = :sunday or monday = :monday or tuesday = :tuesday or wednesday = :wednesday or thursday = :thursday or friday = :friday or saturday = :saturday)\n" +
            "and start_date <= :currentDate\n" +
            "and route_id = :currentRouteId\n" +
            "and direction_id = :currentDirection\n" +
            "and arrival_time > :currentTime\n" +
            "order by arrival_time\n" +
            "limit 1)\n" +
            "order by arrival_time")
    List<Stop> getBusArret(int currentDate, int currentRouteId, int currentDirection, String currentTime, int sunday, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Stop> stops);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Stop stop);

    @Query("SELECT COUNT(*) FROM Stop")
    int count();
}
