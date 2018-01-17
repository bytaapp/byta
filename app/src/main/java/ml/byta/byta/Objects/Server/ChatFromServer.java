package ml.byta.byta.Objects.Server;

import java.util.List;

import ml.byta.byta.DataBase.Object;
import ml.byta.byta.Objects.User;

public class ChatFromServer {

    private int serverId;
    private List<User> users;
    private List<Object> objects;

    public ChatFromServer(int serverId, List<User> users, List<Object> objects) {
        this.serverId = serverId;
        this.users = users;
        this.objects = objects;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }
}
