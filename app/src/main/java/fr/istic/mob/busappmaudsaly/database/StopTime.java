package fr.istic.mob.busappmaudsaly.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "stop_time",
        foreignKeys = {
                @ForeignKey(entity = Trip.class, parentColumns = "trip_id", childColumns = "trip_id"),
                @ForeignKey(entity = Stop.class, parentColumns = "stop_id", childColumns = "stop_id")
        }
)
public class StopTime {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "time_id")
    public int timeId;

    @ColumnInfo (name = "trip_id")
    public int tripId;

    @ColumnInfo (name = "arrival_time")
    public String arrivalTime;

    @ColumnInfo (name = "departure_time")
    public String departureTime;

    @ColumnInfo (name = "stop_id")
    public int stopId;

    public StopTime(int tripId, String arrivalTime, String departureTime, int stopId) {
        this.tripId = tripId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopId = stopId;
    }
}
