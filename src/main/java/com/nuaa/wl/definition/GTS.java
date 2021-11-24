package com.nuaa.wl.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class GTS implements Serializable {
    private String GTSName;//卫士转换系统的名字
    private HashSet<Var> vars;//变量集合
    private ArrayList<Event> eventArrayList;//事件集合
    private ArrayList<Transition> transitionArrayList;//转换集合
    private ArrayList<Assertion> assertionArrayList;//断言集合
    private ArrayList<Vnode> graph;//可达性图

    public String getGTSName() {
        return GTSName;
    }

    public void setGTSName(String GTSName) {
        this.GTSName = GTSName;
    }

    public HashSet<Var> getVars() {
        return vars;
    }

    public void setVars(HashSet<Var> vars) {
        this.vars = vars;
    }

    public ArrayList<Event> getEventArrayList() {
        return eventArrayList;
    }

    public void setEventArrayList(ArrayList<Event> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }

    public ArrayList<Transition> getTransitionArrayList() {
        return transitionArrayList;
    }

    public void setTransitionArrayList(ArrayList<Transition> transitionArrayList) {
        this.transitionArrayList = transitionArrayList;
    }

    public ArrayList<Assertion> getAssertionArrayList() {
        return assertionArrayList;
    }

    public void setAssertionArrayList(ArrayList<Assertion> assertionArrayList) {
        this.assertionArrayList = assertionArrayList;
    }

    public ArrayList<Vnode> getGraph() {
        return graph;
    }

    public void setGraph(ArrayList<Vnode> graph) {
        this.graph = graph;
    }

    @Override
    public String toString() {
        return "GTS{" +
                "GTSName='" + GTSName + '\'' +
                ", vars=" + vars +
                ", eventArrayList=" + eventArrayList +
                ", transitionArrayList=" + transitionArrayList +
                ", assertionArrayList=" + assertionArrayList +
                ", graph=" + graph +
                '}';
    }
}
