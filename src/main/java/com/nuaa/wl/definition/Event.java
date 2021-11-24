package com.nuaa.wl.definition;

import java.io.Serializable;

public class Event implements Serializable {
    private String eventName;
    private String others;
    //private int state;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", others='" + others + '\'' +
                '}';
    }
}
