package fr.istic.mob.busappmaudsaly.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface BusRouteDao {
    @Query("SELECT * FROM bus_route")
    List<BusRoute> getAll();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BusRoute> busRoutes);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(BusRoute busRoute);

    @Query("SELECT COUNT(*) FROM bus_route")
    int count();
}
