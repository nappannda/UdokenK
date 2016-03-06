package com.sakasho.udokenk.Event;

import com.sakasho.udokenk.Fragment.AwaseSelectOkazuFragment;

/**
 * Created by nappannda on 2016/02/22.
 */
public class AwaseSelectOkazuFinishEvent {
    private int udon;
    private String okazus[];
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public AwaseSelectOkazuFinishEvent(int udon, String okazus[], int count) {
        setUdon(udon);
        setOkazus(okazus);
        setCount(count);
    }

    public int getUdon() {
        return udon;
    }

    public void setUdon(int udon) {
        this.udon = udon;
    }

    public String[] getOkazus() {
        return okazus;
    }

    public void setOkazus(String[] okazus) {
        this.okazus = okazus;
    }
}
