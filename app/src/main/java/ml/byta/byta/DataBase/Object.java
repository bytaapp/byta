package ml.byta.byta.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Object {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "viewed")
    private boolean viewed;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "distance")
    private double distance;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "owner_id")
    private int ownerId;

    @ColumnInfo(name = "server_id")
    private int serverId;

    public Object(String description, boolean viewed, String location, double distance, long timestamp, int ownerId, int serverId) {
        this.description = description;
        this.viewed = viewed;
        this.location = location;
        this.distance = distance;
        this.timestamp = timestamp;
        this.ownerId = ownerId;
        this.serverId = serverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}

