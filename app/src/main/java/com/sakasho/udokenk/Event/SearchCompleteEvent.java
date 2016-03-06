package com.sakasho.udokenk.Event;

import com.sakasho.udokenk.UdonShopEntity;

/**
 * Created by nappannda on 2016/02/04.
 */
public class SearchCompleteEvent {
    private UdonShopEntity udonShopEntity;

    public SearchCompleteEvent(UdonShopEntity udonShopEntity) {
        this.udonShopEntity = udonShopEntity;
    }

    public UdonShopEntity getUdonShopEntity() {
        return udonShopEntity;
    }
}
