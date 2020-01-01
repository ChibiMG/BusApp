package fr.istic.mob.busappmaudsaly.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CalendarDao {

    @Query("SELECT * FROM Calendar")
    List<Calendar> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Calendar... calendars);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Calendar calendar);

    @Query("SELECT COUNT(*) FROM calendar")
    int count();
}
