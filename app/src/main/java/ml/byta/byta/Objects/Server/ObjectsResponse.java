package ml.byta.byta.Objects.Server;

import java.util.List;

import ml.byta.byta.DataBase.Object;

public class ObjectsResponse {

    private List<Object> objects;

    public ObjectsResponse(List<Object> objects) {
        this.objects = objects;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }
}
