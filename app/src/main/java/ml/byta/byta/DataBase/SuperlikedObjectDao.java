package ml.byta.byta.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SuperlikedObjectDao {

    // Selecciona todos los objetos.
    @Query("SELECT * FROM SuperlikedObject")
    List<SuperlikedObject> getAllObjects();

    // Selecciona un objeto por su ID.
    @Query("SELECT * FROM SuperlikedObject WHERE id = :id LIMIT 1")
    SuperlikedObject getById(int id);

    // Selecciona un objeto por el ID de su dueño.
    @Query("SELECT * FROM SuperlikedObject WHERE owner_id = :id LIMIT 1")
    SuperlikedObject getByOwnerId(int id);

    // Selecciona un objeto por el ID del objeto en el servidor.
    @Query("SELECT * FROM SuperlikedObject WHERE server_id = :id LIMIT 1")
    SuperlikedObject getByServerId(int id);

    // Selecciona el último objeto almacenado por su timestamp.
    @Query("SELECT * FROM SuperlikedObject ORDER BY timestamp DESC LIMIT 1")
    SuperlikedObject getLastObjectInTime();

    // Inserta un nuevo objeto.
    @Insert
    void insert(SuperlikedObject superlikedObject);

    // Actualiza un objeto pasado como parámetro.
    @Update
    void update(SuperlikedObject superlikedObject);

    // Elimina un objeto por su ID.
    @Query("DELETE FROM superlikedobject WHERE id = :id")
    void deleteById(int id);

    // Elimina un objeto por el ID del objeto en el servidor.
    @Query("DELETE FROM superlikedobject WHERE server_id = :id")
    void deleteByServerId(int id);

    // Elimina un objeto pasado como parámetro.
    @Delete
    void delete(SuperlikedObject superlikedObject);

    // Elimina todos los objetos de la tabla (equivalente a TRUNCATE en SQL).
    @Query("DELETE FROM superlikedobject")
    void deleteAllObjects();

}
