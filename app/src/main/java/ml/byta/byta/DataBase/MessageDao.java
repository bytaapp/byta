package ml.byta.byta.DataBase;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    // Selecciona todos los mensajes.
    @Query("SELECT * FROM message")
    List<Message> getAllMessages();

    // Selecciona todos los mensajes ordenados de más recientes a más antiguos.
    @Query("SELECT * FROM message ORDER BY timestamp DESC")
    List<Message> getAllMessagesNewestFirst();

    // Selecciona un mensaje por su ID.
    @Query("SELECT * FROM message WHERE id = :id LIMIT 1")
    Message getById(int id);

    // Selecciona todos los mensajes de un chat por el ID del chat.
    @Query("SELECT * FROM message WHERE chat_id = :id")
    List<Message> getByChatId(int id);

    // Selecciona todos los mensajes de un chat por el ID del autor.
    @Query("SELECT * FROM message WHERE author_id = :id")
    List<Message> getByAuthorId(int id);

    // Selecciona todos los mensajes de un chat en concreto y escritos por un usuario en concreto.
    @Query("SELECT * FROM message WHERE chat_id = :chatId AND author_id = :authorId")
    List<Message> getByAuthorIdInChat(int chatId, int authorId);

    // Selecciona el último mensaje de un chat por el ID del chat.
    @Query("SELECT * FROM message WHERE chat_id = :id ORDER BY timestamp DESC LIMIT 1")
    Message getLastMessageFromChat(int id);

    // Selecciona un mensaje por su ID en el servidor.
    @Query("SELECT * FROM message WHERE server_id = :id LIMIT 1")
    Message getByServerId(int id);

    // Inserta un nuevo mensaje.
    @Insert
    void insert(Message message);

    // Inserta más de un mensaje.
    @Insert
    void insertMessages(List<Message> messages);

    // Elimina un mensaje por su ID.
    @Query("DELETE FROM message WHERE id = :id")
    void deleteById(int id);

    // Elimina un mensaje pasado como parámetro.
    @Delete
    void delete(Message message);

    // Elimina todos los mensajes de la tabla (equivalente a TRUNCATE en SQL).
    @Query("DELETE FROM message")
    void deleteAllMessages();

}