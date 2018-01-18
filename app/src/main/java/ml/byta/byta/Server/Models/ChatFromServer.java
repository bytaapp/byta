package ml.byta.byta.Server.Models;

import java.util.List;

import ml.byta.byta.DataBase.Object;

public class ChatFromServer {

    private int serverId;
    private List<UserFromServer> users;
    private List<Object> objects;

    public ChatFromServer(int serverId, List<UserFromServer> users, List<Object> objects) {
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

    public List<UserFromServer> getUsers() {
        return users;
    }

    public void setUsers(List<UserFromServer> users) {
        this.users = users;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }
}
