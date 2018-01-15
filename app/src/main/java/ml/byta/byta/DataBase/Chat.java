package ml.byta.byta.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Chat {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "first_user_id")
    private int firstUserId;

    @ColumnInfo(name = "second_user_id")
    private int secondUserId;

    public Chat(int id, int firstUserId, int secondUserId) {
        this.id = id;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(int firstUserId) {
        this.firstUserId = firstUserId;
    }

    public int getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(int secondUserId) {
        this.secondUserId = secondUserId;
    }

}
