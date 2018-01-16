package ml.byta.byta.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Object {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "viewed")
    private boolean viewed;

    @ColumnInfo(name = "owner_id")
    private int ownerId;

    public Object(String description, boolean viewed, int ownerId) {
        this.description = description;
        this.viewed = viewed;
        this.ownerId = ownerId;
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

}

