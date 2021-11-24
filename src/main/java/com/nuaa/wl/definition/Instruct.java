package com.nuaa.wl.definition;

import java.io.Serializable;
import java.util.ArrayList;

public class Instruct implements Serializable {
    private String variableReference;
    private String expression;

    public String getVariableReference() {
        return variableReference;
    }

    public void setVariableReference(String variableReference) {
        this.variableReference = variableReference;
    }


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Instruct{" +
                "variableReference='" + variableReference + '\'' +
                ", expression=" + expression +
                '}';
    }
}
