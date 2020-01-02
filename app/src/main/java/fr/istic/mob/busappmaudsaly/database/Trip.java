package fr.istic.mob.busappmaudsaly.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "trip", foreignKeys = {
        @ForeignKey(
                entity = BusRoute.class,
                parentColumns = "route_id",
                childColumns = "route_id"
        ),
        @ForeignKey(
                entity = Calendar.class,
                parentColumns = "service_id",
                childColumns = "service_id"
        )
})
public class Trip {

    @PrimaryKey
    @ColumnInfo(name = "trip_id")
    public int tripId;

    @ColumnInfo(name = "route_id")
    public int routeId;

    @ColumnInfo(name = "service_id")
    public int serviceId;

    @ColumnInfo(name = "trip_headsign")
    public String tripHeadsign;

    @ColumnInfo(name = "direction_id")
    public int directionId;

    public Trip(int tripId, int routeId, int serviceId, String tripHeadsign, int directionId) {
        this.tripId = tripId;
        this.routeId = routeId;
        this.serviceId = serviceId;
        this.tripHeadsign = tripHeadsign;
        this.directionId = directionId;
    }
}
