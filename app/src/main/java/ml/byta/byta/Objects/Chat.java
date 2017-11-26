package ml.byta.byta.Objects;

import java.util.List;

public class Chat {

    private int id;
    private String created_at;
    private List<User> users;
    private List<Object> objects;

    public Chat(int id, String created_at, List<User> users, List<Object> objects) {
        this.id = id;
        this.created_at = created_at;
        this.users = users;
        this.objects = objects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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
