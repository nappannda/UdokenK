package com.sakasho.udokenk.Event;

import com.sakasho.udokenk.Fragment.SiruSelectToppingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nappannda on 2016/02/21.
 */
public class SiruToppingSelectFinishEvent {
    public SiruToppingSelectFinishEvent(ArrayList<Integer> topping, int size, int kanban) {
        this.topping = topping;
        this.size = size;
        this.kanban = kanban;
    }

    public ArrayList<Integer> getTopping() {
        return topping;
    }

    public void setTopping(ArrayList<Integer> topping) {
        this.topping = topping;
    }

    ArrayList<Integer> topping;

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
