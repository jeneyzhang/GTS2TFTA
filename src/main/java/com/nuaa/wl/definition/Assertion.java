package com.nuaa.wl.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Assertion implements Serializable {
    private boolean ifExist;
    private ArrayList<BoolExp> boolExpArrayList;
    private ArrayList<Instruct> instructArrayList;
    private HashSet<String> dependVar = new HashSet<>();
    private HashSet<String> dependedVar = new HashSet<>();

    public boolean isIfExist() {
        return ifExist;
    }
    
    public void setIfExist(boolean ifExist) {
        this.ifExist = ifExist;
    }

    public ArrayList<BoolExp> getBoolExpArrayList() {
        return boolExpArrayList;
    }

    public void setBoolExpArrayList(ArrayList<BoolExp> boolExpArrayList) {
        this.boolExpArrayList = boolExpArrayList;
    }

    public ArrayList<Instruct> getInstructArrayList() {
        return instructArrayList;
    }

    public void setInstructArrayList(ArrayList<Instruct> instructArrayList) {
        this.instructArrayList = instructArrayList;
    }

    public HashSet<String> getDependVar() {
        return dependVar;
    }

    public void setDependVar(HashSet<String> dependVar) {
        this.dependVar = dependVar;
    }

    public HashSet<String> getDependedVar() {
        return dependedVar;
    }

    public void setDependedVar(HashSet<String> dependedVar) {
        this.dependedVar = dependedVar;
    }

    @Override
    public String toString() {
        return "Assertion{" +
                "ifExist=" + ifExist +
                ", boolExpArrayList=" + boolExpArrayList +
                ", instructArrayList=" + instructArrayList +
                ", dependVar=" + dependVar +
                ", dependedVar=" + dependedVar +
                '}';
    }
}
