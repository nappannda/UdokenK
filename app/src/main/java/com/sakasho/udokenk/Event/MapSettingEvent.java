package com.sakasho.udokenk.Event;

import com.sakasho.udokenk.UdonShopEntity;

/**
 * Created by nappannda on 2016/02/05.
 */
public class MapSettingEvent {
    private UdonShopEntity udonShopEntity;

    public MapSettingEvent(UdonShopEntity udonShopEntity) {
        this.udonShopEntity = udonShopEntity;
    }

    public UdonShopEntity getUdonShopEntity() {
        return udonShopEntity;
    }
}
