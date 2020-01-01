package fr.istic.mob.busappmaudsaly.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TripDao {
    @Query("SELECT * FROM Trip")
    List<Trip> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Trip... trips);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Trip trip);

    @Query("SELECT COUNT(*) FROM Trip")
    int count();
}
