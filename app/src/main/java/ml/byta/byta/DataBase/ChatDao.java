package ml.byta.byta.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ChatDao {

    // Selecciona todos los chats.
    @Query("SELECT * FROM chat")
    List<Chat> getAllChats();

    // Selecciona un chat por su ID.
    @Query("SELECT * FROM chat WHERE id = :id")
    Chat getById(int id);

    // Selecciona un chat por el ID del usuario.
    @Query("SELECT * FROM chat WHERE first_user_id = :id OR second_user_id = :id")
    Chat getByUserId(int id);

    // Inserta un nuevo chat.
    @Insert
    void insert(Chat chat);

    // Inserta más de un chat.
    @Insert
    void insertChats(List<Chat> chats);

    // Elimina un chat por su ID.
    @Query("DELETE FROM chat WHERE id = :id")
    void deleteById(int id);

    // Elimina un chat pasado como parámetro.
    @Delete
    void delete(Chat chat);

}
