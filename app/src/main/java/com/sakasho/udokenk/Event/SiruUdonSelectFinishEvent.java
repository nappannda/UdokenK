package com.sakasho.udokenk.Event;

/**
 * Created by nappannda on 2016/02/06.
 */
public class SiruUdonSelectFinishEvent {
    public SiruUdonSelectFinishEvent(int size, int kanban) {
        this.size = size;
        this.kanban = kanban;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getKanban() {
        return kanban;
    }

    public void setKanban(int kanban) {
        this.kanban = kanban;
    }

    private int size, kanban;
}
