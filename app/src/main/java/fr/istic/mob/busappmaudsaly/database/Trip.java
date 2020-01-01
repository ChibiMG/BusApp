package fr.istic.mob.busappmaudsaly.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Trip {

    @PrimaryKey
    public int tripId;

    @ForeignKey(entity = BusRoute.class,parentColumns = "route_id", childColumns = "route_id")
    public int routeId;

    @ColumnInfo(name = "trip_headsign")
    public String tripHeadsign;

    @ColumnInfo(name = "direction_id")
    public int directionId;

    public Trip(int tripId, int routeId, String tripHeadsign, int directionId) {
        this.tripId = tripId;
        this.routeId = routeId;
        this.tripHeadsign = tripHeadsign;
        this.directionId = directionId;
    }
}
