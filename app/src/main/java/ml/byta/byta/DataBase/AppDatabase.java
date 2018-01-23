package ml.byta.byta.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Object.class, Chat.class, Message.class, SuperlikedObject.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ObjectDao objectDao();
    public abstract ChatDao chatDao();
    public abstract MessageDao messageDao();
    public abstract SuperlikedObjectDao superlikedObjectDao();

}
