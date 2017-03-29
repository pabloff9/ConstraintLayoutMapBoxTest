package com.example.joao.constrainttest.maps.model;

public class Region {
    private String name;
    private String idRegion;

    public Region() {
    }

    public Region(String name, String idRegion) {
        this.name = name;
        this.idRegion = idRegion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(String idRegion) {
        this.idRegion = idRegion;
    }
}
