package fr.istic.mob.busappmaudsaly.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface TripDao {
    @Query("SELECT * FROM trip")
    List<Trip> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    void insertAll(List<Trip> trips);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Trip trip);

    @Query("SELECT COUNT(*) FROM trip")
    int count();
}
