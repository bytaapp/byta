package ml.byta.byta.Objects;

public class Object {

    private int id;
    private int author_id;
    private String description;
    private String location;
    private String created_at;

    public Object(int id, int author_id, String description, String location, String created_at) {
        this.id = id;
        this.author_id = author_id;
        this.description = description;
        this.location = location;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
