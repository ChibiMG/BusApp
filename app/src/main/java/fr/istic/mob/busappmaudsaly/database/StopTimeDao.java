package fr.istic.mob.busappmaudsaly.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface StopTimeDao {

    @Query("SELECT * FROM stop_time")
    List<StopTime> getAll();

    @Query("SELECT * FROM stop WHERE stop_id = :stopId")
    Stop getStopById(int stopId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StopTime> stopTimes);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(StopTime stopTime);

    @Query("select * from stop_time\n" +
            "natural join trip\n" +
            "natural join calendar\n" +
            "where (sunday = :sunday or monday = :monday or tuesday = :tuesday or wednesday = :wednesday or thursday = :thursday or friday = :friday or saturday = :saturday)\n" +
            "and start_date <= :currentDate\n" +
            "and route_id = :currentRouteId\n" +
            "and direction_id = :currentDirection\n" +
            "and arrival_time > :currentTime\n" +
            "and stop_id = :stopId\n" +
            "order by arrival_time\n")
    List<StopTime> getHoraireForOneStop(int currentDate, int currentRouteId, int currentDirection, String currentTime, int sunday, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int stopId);

    @Query("select * from stop_time\n natural join stop\n" +
            "where trip_id = :tripId\n" +
            "and arrival_time > :arrivalTime\n" +
            "order by arrival_time")
    List<StopAndTime> getStopAndTimeFromTime(int tripId, String arrivalTime);

    @Query("SELECT COUNT(*) FROM stop_time")
    int count();
}
