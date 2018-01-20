package ml.byta.byta.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Chat {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "first_user_id")
    private int firstUserId;

    @ColumnInfo(name = "second_user_id")
    private int secondUserId;

    @ColumnInfo(name = "interlocutor_name")
    private String interlocutorName;

    @ColumnInfo(name = "server_id")
    private int serverId;

    public Chat(int firstUserId, int secondUserId, String interlocutorName, int serverId) {
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.interlocutorName = interlocutorName;
        this.serverId = serverId;
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

    public String getInterlocutorName() {
        return interlocutorName;
    }

    public void setInterlocutorName(String interlocutorName) {
        this.interlocutorName = interlocutorName;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
