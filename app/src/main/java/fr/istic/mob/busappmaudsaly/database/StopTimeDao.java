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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StopTime> stopTimes);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(StopTime stopTime);

    @Query("SELECT COUNT(*) FROM stop_time")
    int count();
}
