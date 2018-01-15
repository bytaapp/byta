package ml.byta.byta.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ObjectDao {

    // Selecciona todos los objetos.
    @Query("SELECT * FROM object")
    List<Object> getAllObjects();

    // Selecciona un objeto por su ID.
    @Query("SELECT * FROM object WHERE id = :id")
    Object getById(int id);

    // Selecciona todos los objetos que se han visto.
    @Query("SELECT * FROM object WHERE viewed = true")
    List<Object> getAllViewed();

    // Selecciona todos los objetos que no se han visto.
    @Query("SELECT * FROM object WHERE viewed = false")
    List<Object> getAllNotViewed();

    // Selecciona un objeto por el ID de su dueño.
    @Query("SELECT * FROM object WHERE owner_id = :id")
    Object getByOwnerId(int id);

    // Inserta un nuevo objeto.
    @Insert
    void insert(Object object);

    // Elimina un objeto por su ID.
    @Query("DELETE FROM object WHERE id = :id")
    void deleteById(int id);

    // Elimina un objeto pasado como parámetro.
    @Delete
    void delete(Object object);
}

