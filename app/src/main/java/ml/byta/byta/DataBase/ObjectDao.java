package ml.byta.byta.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ObjectDao {

    // Selecciona todos los objetos.
    @Query("SELECT * FROM object")
    List<Object> getAllObjects();

    // Selecciona un objeto por su ID.
    @Query("SELECT * FROM object WHERE id = :id LIMIT 1")
    Object getById(int id);

    // Selecciona todos los objetos que se han visto.
    @Query("SELECT * FROM object WHERE viewed = 1")
    List<Object> getAllViewed();

    // Selecciona todos los objetos que no se han visto.
    @Query("SELECT * FROM object WHERE viewed = 0")
    List<Object> getAllNotViewed();

    // Selecciona un objeto por el ID de su dueño.
    @Query("SELECT * FROM object WHERE owner_id = :id LIMIT 1")
    Object getByOwnerId(int id);

    // Selecciona un objeto por el ID del objeto en el servidor.
    @Query("SELECT * FROM object WHERE server_id = :id LIMIT 1")
    Object getByServerId(int id);

    // Inserta un nuevo objeto.
    @Insert
    void insert(Object object);

    // Inserta más de un objeto.
    @Insert
    void insertObjects(List<Object> objects);

    // Actualiza un objeto, seleccionado por su ID, marcándolo como visto (liked según la lógica planteada).
    @Query("UPDATE object SET viewed = 1 WHERE id = :id")
    void setObjectAsViewedById(int id);

    // Actualiza un objeto pasado como parámetro.
    @Update
    void update(Object object);

    // Elimina un objeto por su ID.
    @Query("DELETE FROM object WHERE id = :id")
    void deleteById(int id);

    // Elimina un objeto pasado como parámetro.
    @Delete
    void delete(Object object);

    // Elimina todos los objetos de la tabla (equivalente a TRUNCATE en SQL).
    @Query("DELETE FROM object")
    void deleteAllObjects();

}

