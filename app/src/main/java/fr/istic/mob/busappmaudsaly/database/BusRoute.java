package fr.istic.mob.busappmaudsaly.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bus_route")
public class BusRoute {

    @PrimaryKey
    @ColumnInfo(name = "route_id")
    public int routeId;

    @ColumnInfo(name = "route_short_name")
    public String routeShortName;

    @ColumnInfo(name = "route_long_name")
    public String routeLongName;

    @ColumnInfo(name = "route_color")
    public String routeColor;

    @ColumnInfo(name = "route_text_color")
    public String routeTextColor;

    @ColumnInfo(name = "route_sort_order")
    public String routeSortOrder;

    public BusRoute(int routeId, String routeShortName, String routeLongName, String routeColor, String routeTextColor, String routeSortOrder) {
        this.routeId = routeId;
        this.routeShortName = routeShortName;
        this.routeLongName = routeLongName;
        this.routeColor = routeColor;
        this.routeTextColor = routeTextColor;
        this.routeSortOrder = routeSortOrder;
    }
}
