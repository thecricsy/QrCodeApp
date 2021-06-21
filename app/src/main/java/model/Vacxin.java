package model;

public class Vacxin {
    String name;
    String id;

    public Vacxin(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public Vacxin() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

