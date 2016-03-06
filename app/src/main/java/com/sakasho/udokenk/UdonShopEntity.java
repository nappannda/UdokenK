package com.sakasho.udokenk;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nappannda on 2016/02/04.
 */
public class UdonShopEntity implements Serializable{
    @Expose
    @SerializedName("rest")
    private List<UdonShop> udonShopList;

    public List<UdonShop> getUdonShosList() {
        return udonShopList;
    }

    public void setUdonShopList(List<UdonShop> udonShopList) {
        this.udonShopList = udonShopList;
    }

    public Location getNowLocation() {
        return nowLocation;
    }

    public void setNowLocation(Location nowLocation) {
        this.nowLocation = nowLocation;
    }

    private Location nowLocation;//今の現在地を保存
}
