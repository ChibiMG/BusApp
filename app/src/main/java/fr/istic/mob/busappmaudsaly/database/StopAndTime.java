package fr.istic.mob.busappmaudsaly.database;

import androidx.room.Embedded;
import androidx.room.Relation;

public class StopAndTime {

    @Embedded
    public StopTime time;

    @Relation(parentColumn = "stop_id", entityColumn = "stop_id")
    public Stop stop;
}
