package com.example.joao.constrainttest.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Campus implements Parcelable {
    private String name;
    private String idRegional;
    private String idCampus;
    private float lat;
    private float lon;
    private String idRegion;

    public Campus() {
    }

    public Campus(String name, String idRegional, String idCampus, float lat, float lon, String idRegion) {
        this.name = name;
        this.idRegional = idRegional;
        this.idCampus = idCampus;
        this.lat = lat;
        this.lon = lon;
        this.idRegion = idRegion;
    }

    protected Campus(Parcel in) {
        name = in.readString();
        idRegional = in.readString();
        idCampus = in.readString();
        lat = in.readFloat();
        lon = in.readFloat();
        idRegion = in.readString();
    }

    public static final Creator<Campus> CREATOR = new Creator<Campus>() {
        @Override
        public Campus createFromParcel(Parcel in) {
            return new Campus(in);
        }

        @Override
        public Campus[] newArray(int size) {
            return new Campus[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdRegional() {
        return idRegional;
    }

    public void setIdRegional(String idRegional) {
        this.idRegional = idRegional;
    }

    public String getIdCampus() {
        return idCampus;
    }

    public void setIdCampus(String idCampus) {
        this.idCampus = idCampus;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(String idRegion) {
        this.idRegion = idRegion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(idRegional);
        parcel.writeString(idCampus);
        parcel.writeFloat(lat);
        parcel.writeFloat(lon);
        parcel.writeString(idRegion);
    }
}
