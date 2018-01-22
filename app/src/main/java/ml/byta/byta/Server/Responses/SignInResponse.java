package ml.byta.byta.Server.Responses;

public class SignInResponse {

    private boolean ok;
    private int id_usuario;
    private String sessionID;
    private String error;

    public SignInResponse(boolean ok, int id_usuario, String sessionID, String error) {
        this.ok = ok;
        this.id_usuario = id_usuario;
        this.sessionID = sessionID;
        this.error = error;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
