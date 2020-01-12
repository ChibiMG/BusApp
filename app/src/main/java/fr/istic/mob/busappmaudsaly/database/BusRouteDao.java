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

    @Query("select DISTINCT route_id, route_long_name, route_short_name, route_color, route_text_color from stop_time\n" +
            "natural join trip\n" +
            "natural join stop\n" +
            "NATURAL join bus_route\n" +
            "where stop_name = :arret\n" +
            "order by route_id")
    List<BusRoute> getRouteInOneStop(String arret);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BusRoute> busRoutes);


    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(BusRoute busRoute);

    @Query("SELECT COUNT(*) FROM bus_route")
    int count();
}
