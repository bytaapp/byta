package ml.byta.byta.Objects;

/**
 * Created by ibai on 11/4/17.
 */

public class Objeto {

    String id, id_usuario, descripcion, ubicacion, fecha_subido;
    float distancia;

    public Objeto(String id, String id_usuario, String descripcion, String ubicacion, String fecha_subido) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fecha_subido = fecha_subido;
    }

    public Objeto(String id, String id_usuario, String descripcion, String ubicacion, String fecha_subido, int distancia) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fecha_subido = fecha_subido;
        this.distancia = distancia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha_subido() {
        return fecha_subido;
    }

    public void setFecha_subido(String fecha_subido) {
        this.fecha_subido = fecha_subido;
    }
}
