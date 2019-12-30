package fr.istic.mob.busappmaudsaly.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface BusRouteDao {
    @Query("SELECT * FROM busRoute")
    List<BusRoute> getAll();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insertAll(BusRoute... busRoutes);

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(BusRoute busRoute);
}
