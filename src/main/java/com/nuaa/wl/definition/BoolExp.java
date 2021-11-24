package com.nuaa.wl.definition;

import java.io.Serializable;

public class BoolExp implements Serializable {
    private boolean notPre;
    private String left;
    private String operator;
    private String right;
    private String suf;

    public boolean isNotPre() {
        return notPre;
    }

    public void setNotPre(boolean notPre) {
        this.notPre = notPre;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getSuf() {
        return suf;
    }

    public void setSuf(String suf) {
        this.suf = suf;
    }

    @Override
    public String toString() {
        return "BoolExp{" +
                "notPre=" + notPre +
                ", left='" + left + '\'' +
                ", operator='" + operator + '\'' +
                ", right='" + right + '\'' +
                ", suf='" + suf + '\'' +
                '}';
    }
}
