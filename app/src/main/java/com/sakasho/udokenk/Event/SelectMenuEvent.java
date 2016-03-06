package com.sakasho.udokenk.Event;

/**
 * Created by nappannda on 2016/02/04.
 */
public class SelectMenuEvent {
    private int menu = 0;

    public SelectMenuEvent(int menu) {
        this.menu = menu;
    }

    public int getMenu() {
        return menu;
    }

}
