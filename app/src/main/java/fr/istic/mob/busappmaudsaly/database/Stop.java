package fr.istic.mob.busappmaudsaly.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stop")
public class Stop {

    @PrimaryKey
    @ColumnInfo(name = "stop_id")
    public int stopId;

    @ColumnInfo (name = "stop_name")
    public String stopName;

    public Stop(int stopId, String stopName) {
        this.stopId = stopId;
        this.stopName = stopName;
    }
}
