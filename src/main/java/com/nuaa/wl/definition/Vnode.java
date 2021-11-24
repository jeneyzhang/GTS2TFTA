package com.nuaa.wl.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Vnode implements Serializable {
    private String nodeName;//结点名
    private HashSet<Var> vars;//变量集合
    private ArrayList<Next> nexts = new ArrayList<>();//后继集合
    private boolean flag;//曾经是否出现在队列中
    private ArrayList<ArrayList<String>> pathSet;//路径

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public HashSet<Var> getVars() {
        return vars;
    }

    public void setVars(HashSet<Var> vars) {
        this.vars = vars;
    }

    public ArrayList<Next> getNexts() {
        return nexts;
    }

    public void setNexts(ArrayList<Next> nexts) {
        this.nexts = nexts;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ArrayList<ArrayList<String>> getPathSet() {
        return pathSet;
    }

    public void setPathSet(ArrayList<ArrayList<String>> pathSet) {
        this.pathSet = pathSet;
    }

    @Override
    public String toString() {
        return "Vnode{" +
                "nodeName='" + nodeName + '\'' +
                ", vars=" + vars +
                ", nexts=" + nexts +
                ", flag=" + flag +
                ", pathSet=" + pathSet +
                '}';
    }
}
