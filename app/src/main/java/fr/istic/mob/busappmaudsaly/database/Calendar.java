package fr.istic.mob.busappmaudsaly.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Calendar {

    @PrimaryKey
    @ColumnInfo(name = "service_id")
    public int serviceId;

    @ColumnInfo (name = "monday")
    public int monday;

    @ColumnInfo (name = "tuesday")
    public int tuesday;

    @ColumnInfo (name = "wednesday")
    public int wednesday;

    @ColumnInfo (name = "thursday")
    public int thursday;

    @ColumnInfo (name = "friday")
    public int friday;

    @ColumnInfo (name = "saturday")
    public int saturday;

    @ColumnInfo (name = "sunday")
    public int sunday;

    @ColumnInfo (name = "start_date")
    public int startDate;

    @ColumnInfo (name = "end_date")
    public int endDate;

    public Calendar(int serviceId, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, int startDate, int endDate) {
        this.serviceId = serviceId;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
