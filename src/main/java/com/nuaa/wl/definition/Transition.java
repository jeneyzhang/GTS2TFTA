package com.nuaa.wl.definition;

import java.io.Serializable;
import java.util.HashSet;

public class Transition implements Serializable {
    private String eventName;//事件名
    private Guard guard;//卫士
    private HashSet<String> varHashSet;//涉及到的变量
    private HashSet<Assertion> pInstruc;//指令集

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Guard getGuard() {
        return guard;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
    }

    public HashSet<String> getVarHashSet() {
        return varHashSet;
    }

    public void setVarHashSet(HashSet<String> varHashSet) {
        this.varHashSet = varHashSet;
    }

    public HashSet<Assertion> getpInstruc() {
        return pInstruc;
    }

    public void setpInstruc(HashSet<Assertion> pInstruc) {
        this.pInstruc = pInstruc;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "eventName='" + eventName + '\'' +
                ", guard=" + guard +
                ", varHashSet=" + varHashSet +
                ", pInstruc=" + pInstruc +
                '}';
    }

}
