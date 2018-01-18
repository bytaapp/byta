package ml.byta.byta.Server.Responses;

import java.util.List;

import ml.byta.byta.DataBase.Object;

public class ObjectsResponse {

    private boolean ok;
    private List<Object> objects;
    private String error;

    public ObjectsResponse(boolean ok, List<Object> objects, String error) {
        this.ok = ok;
        this.objects = objects;
        this.error = error;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
