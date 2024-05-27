package com.example.gia;

public class Material {
    private String id;
    private String name;
    private String body;

    private String id_te;


    public Material() {
        this.id = id;
        this.name = name;
        this.body = body;
        this.id_te = id_te;

    }

    public String getId_te() {
        return id_te;
    }

    public void setId_te(String id_te) {
        this.id_te = id_te;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
