package ml.byta.byta.DataBase;

import android.app.Activity;
import android.arch.persistence.room.Room;

public class Database {

    private Activity activity;
    public static AppDatabase db;

    public Database(Activity activity) {
        this.activity = activity;
        this.db = Room.databaseBuilder(activity.getApplicationContext(), AppDatabase.class, "local-database").build();
    }

    public AppDatabase getDb() {
        return db;
    }

    public void setDb(AppDatabase db) {
        this.db = db;
    }
}
