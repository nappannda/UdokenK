package com.sakasho.udokenk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nappannda on 2016/02/04.
 */
public class UdonShop {
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("name_kana")
    private String name_kana;
    @Expose
    @SerializedName("latitude")
    private String latitude;
    @Expose
    @SerializedName("longitude")
    private String longitude;
    @Expose
    @SerializedName("address")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_kana() {
        return name_kana;
    }

    public void setName_kana(String name_kana) {
        this.name_kana = name_kana;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
