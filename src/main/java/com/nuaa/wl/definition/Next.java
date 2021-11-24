package com.nuaa.wl.definition;

import java.io.Serializable;

public class Next implements Serializable {
    private String transitionName;
    private String outNode;
    private String inNode;

    public String getTransitionName() {
        return transitionName;
    }

    public void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
    }

    public String getOutNode() {
        return outNode;
    }

    public void setOutNode(String outNode) {
        this.outNode = outNode;
    }

    public String getInNode() {
        return inNode;
    }

    public void setInNode(String inNode) {
        this.inNode = inNode;
    }

    @Override
    public String toString() {
        return "Next{" +
                "transitionName='" + transitionName + '\'' +
                ", outNode='" + outNode + '\'' +
                ", inNode='" + inNode + '\'' +
                '}';
    }
}
