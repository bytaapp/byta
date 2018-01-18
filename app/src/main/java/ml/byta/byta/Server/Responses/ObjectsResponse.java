package ml.byta.byta.Server.Responses;

import java.util.List;

import ml.byta.byta.DataBase.Object;

public class ObjectsResponse {

    private boolean ok;
    private List<Object> objects;

    public ObjectsResponse(boolean ok, List<Object> objects) {
        this.ok = ok;
        this.objects = objects;
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
}
