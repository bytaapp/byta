package ml.byta.byta.Server.ModelsFromServer;

public class ObjectFromServer {

    private int id;
    private int id_usuario;
    private String descripcion;
    private String ubicacion;
    private double fecha_subido;
    private double distancia;

    public ObjectFromServer(int id, int id_usuario, String descripcion, String ubicacion, double fecha_subido, double distancia) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fecha_subido = fecha_subido;
        this.distancia = distancia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
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

    public double getFecha_subido() {
        return fecha_subido;
    }

    public void setFecha_subido(double fecha_subido) {
        this.fecha_subido = fecha_subido;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
