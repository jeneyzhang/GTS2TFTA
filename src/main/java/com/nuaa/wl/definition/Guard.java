package com.nuaa.wl.definition;

import java.io.Serializable;
import java.util.ArrayList;

public class Guard implements Serializable {
    private ArrayList<BoolExp> boolGuard;

    public ArrayList<BoolExp> getBoolGuard() {
        return boolGuard;
    }

    public void setBoolGuard(ArrayList<BoolExp> boolGuard) {
        this.boolGuard = boolGuard;
    }

    @Override
    public String toString() {
        return "Guard{" +
                "boolGuard=" + boolGuard +
                '}';
    }
}
