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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Stop> stops);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Stop stop);

    @Query("SELECT COUNT(*) FROM Stop")
    int count();
}
