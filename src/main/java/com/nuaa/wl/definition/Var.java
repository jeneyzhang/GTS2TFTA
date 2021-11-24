package com.nuaa.wl.definition;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;

public class Var implements Serializable {
    private String type;//变量的数据类型
    private String vName;//变量名
    private String init;//初始赋值
    private String value;//Var的取值
    //private String state;
    private HashSet<String> domain = new HashSet<>();//变量域
    private boolean flag;//标识变量类型（false是状态变量，true是流式变量）
    private boolean dependOn ;//是否是依赖变量
    private HashSet<String> dependedVar = new HashSet<>();//存放被依赖的变量

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HashSet<String> getDomain() {
        return domain;
    }

    public void setDomain(HashSet<String> domain) {
        this.domain = domain;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isDependOn() {
        return dependOn;
    }

    public void setDependOn(boolean dependOn) {
        this.dependOn = dependOn;
    }

    public HashSet<String> getDependedVar() {
        return dependedVar;
    }

    public void setDependedVar(HashSet<String> dependedVar) {
        this.dependedVar = dependedVar;
    }

    @Override
    public String toString() {
        return "Var{" +
                "vName='" + vName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Var var = (Var) o;
        return Objects.equals(vName, var.vName) &&
                Objects.equals(value, var.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vName, value);
    }
}
