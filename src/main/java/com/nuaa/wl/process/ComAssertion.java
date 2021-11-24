package com.nuaa.wl.process;

import com.nuaa.wl.definition.*;

import java.util.*;

public class ComAssertion {
    public void comAssertion(HashSet<GTS> gtsHashSet){
        ArrayList<Vnode> vnodes = new ArrayList<>();
        HashMap<String,ArrayList<ArrayList<String>>> varPath = new HashMap<>();
        ArrayList<ArrayList<ArrayList<Var>>> allVars = new ArrayList<>();
        for(GTS gts : gtsHashSet){
            if(!gts.getGTSName().equals("independentAssertion")){
                ArrayList<Vnode> graph = gts.getGraph();
                ArrayList<ArrayList<Var>> varsArray = new ArrayList<>();
                for(Vnode vnode : graph){
                    for(Var var : vnode.getVars()){
                        if(varPath.get("["+var.getvName()+","+var.getValue()+"]:")==null){
                            varPath.put("["+var.getvName()+","+var.getValue()+"]:",vnode.getPathSet());
                        }else{
                            ArrayList<ArrayList<String>> arrayLists = varPath.get("[" + var.getvName() + "," + var.getValue() + "]:");
                            arrayLists.addAll(vnode.getPathSet());
                            varPath.replace("["+var.getvName()+","+var.getValue()+"]:",arrayLists);
                        }
                    }
                    ArrayList<Var> varlistTemp = new ArrayList<>();
                    varlistTemp.addAll(vnode.getVars());
                    varsArray.add(varlistTemp);
                    vnodes.add(vnode);
                }
                allVars.add(varsArray);
            }
        }
        System.out.println("所有变量取值时的路径：");
        Iterator iter = varPath.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.print(key);
            System.out.println(val);
        }

            //1.状态集合节点作笛卡尔积 256结果
            ArrayList<ArrayList<ArrayList<Var>>> result = new ArrayList<>();
            descartes(allVars,result,0,new ArrayList<ArrayList<Var>>());
            System.out.println("求出笛卡尔积结果共256种：");
            ArrayList<ArrayList<Var>> res = new ArrayList<>();
            for(ArrayList<ArrayList<Var>> vars : result){
                ArrayList<Var> newArray = new ArrayList<>();
                for(ArrayList<Var> var : vars){
                    for(Var varTemp : var){
                        newArray.add(varTemp);
                    }
                }
                res.add(newArray);
            }
            //2.将独立断言中的变量全部复制一份,获取所有独立断言
            HashSet<Var> assertionVarInit = new HashSet<>();
            ArrayList<Assertion> assertions = new ArrayList<>();
            for(GTS gts : gtsHashSet){
                if(gts.getGTSName().equals("independentAssertion")){
                    for(Var var : gts.getVars()){
                        Var varTemp = new Var();
                        varTemp.setvName(var.getvName());
                        varTemp.setValue(var.getValue());
                        assertionVarInit.add(varTemp);
                    }
                    assertions.addAll(gts.getAssertionArrayList());
                }
            }
            //3.做传播断言的预处理
            ArrayList<String> allPath = new ArrayList<>();
            for(ArrayList<Var> vars : res){
                ArrayList<ArrayList<ArrayList<String>>> strings = new ArrayList<>();
                for(Var var : vars){
                    String s = "["+var.getvName()+","+var.getValue()+"]:";
                    if(varPath.get(s)!=null){
                        strings.add(varPath.get(s));
                    }
                }
                vars.addAll(assertionVarInit);//将独立断言中的变量加入各个变量集合

                {String s = "";
                int n = 0;
                for(ArrayList<ArrayList<String>> path : strings){
                    if(n==0){
                        s = s + path;
                    }else {
                        s = s + "∩" + path ;
                    }
                    n++;
                }
                allPath.add(s);
                }//输出Transition路径

            }

            //4.传播断言
            int num = 0;
            for(ArrayList<Var> vars : res){
                System.out.println("原始变量集合"+vars);
                System.out.println("经过Transition:"+allPath.get(num));
                System.out.println("传播断言：");
                HashSet<Var> varsTemp = new HashSet<>();
                varsTemp.addAll(vars);
                HashSet<Var> vars1 = processInstruct(varsTemp, assertions);
                System.out.println("结果：");
                System.out.println(vars1);
                num++;
            }
    }

    private static void descartes(ArrayList<ArrayList<ArrayList<Var>>> dimvalue, ArrayList<ArrayList<ArrayList<Var>>> result, int layer, ArrayList<ArrayList<Var>> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                descartes(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    ArrayList<ArrayList<Var>> list = new ArrayList<>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    descartes(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    ArrayList<ArrayList<Var>> list = new ArrayList<>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }

    public boolean processBoolExp(HashSet<Var> vars,ArrayList<BoolExp> guard){
        boolean flag = false;
        ArrayList<BoolExp> boolGuard = guard;
        if(boolGuard.size()==1){
            BoolExp boolExpTemp = boolGuard.get(0);
            if(boolExpTemp.isNotPre()){
                String leftVname = boolExpTemp.getLeft();
                System.out.println(leftVname);
                Var var = getVar(leftVname, vars);

                if(var.getValue().equalsIgnoreCase("TRUE")){
                    flag = false;
                }//为假
                else{
                    flag = true;
                }//为真
            }//如果前缀为not
            else {
                if(!boolExpTemp.getOperator().equals("==")){
                    String leftVname = boolExpTemp.getLeft(); //注释头
                     Var var = getVar(leftVname, vars);
                  if(var.getValue().equalsIgnoreCase("TRUE")){
                                       flag = true;
                                    }//为假
                                    else{
                                        flag = false;
                                    }//为真  //注释尾
                }//如果没有操作符==
                else {
                    String leftVname = boolExpTemp.getLeft();
                    String rightValue = boolExpTemp.getRight();
                    Var var = getVar(leftVname, vars);
                    if(leftVname.equals(var.getvName())){
                        if(rightValue.equals(var.getValue())){
                            flag = true;
                        }
                    }
                }//如果有操作符==
            }//如果没有not前缀
        }//单条件没有后缀
        else{
            BoolExp boolExpOne = boolGuard.get(0);
            BoolExp boolExpTwo = boolGuard.get(1);
            if(boolExpOne.getSuf().equals("and")){
                if(boolExpOne.isNotPre()){
                    String leftVname = boolExpOne.getLeft();
                    Var var = getVar(leftVname, vars);
                    if (var.getValue().equalsIgnoreCase("True")){
                        flag = false;
                    }//当结果为True时 由于not前缀 取反
                    else {
                        flag = true;//正确则考虑第二个
                        String operatorTwo = boolExpTwo.getOperator(); //zs
                        String twoLeftVname = boolExpTwo.getLeft();
                        String twoRightValue = boolExpTwo.getRight();
                        Var varTemp = getVar(twoLeftVname, vars);
                        if(operatorTwo.equals("==")) {
                            if (twoLeftVname.equals(varTemp.getvName())) {
                                if (twoRightValue.equals(varTemp.getValue())) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            }
                        }else{
                            if (varTemp.getValue().equalsIgnoreCase("True")){
                                flag = true;
                            }else{
                                flag = false;
                            }
                    }
                    }//当结果为False时 由于not前缀 取反
                }//not前缀 则无操作符和right
                else{
                    String operatorOne = boolExpOne.getOperator();
                    String oneLeftVname = boolExpOne.getLeft();
                    String oneRightValue = boolExpOne.getRight();
                    Var var = getVar(oneLeftVname, vars);
                    if(operatorOne.equals("")){
                        if (var.getValue().equalsIgnoreCase("True")){
                            String operatorTwo = boolExpTwo.getOperator();
                            String twoLeftVname = boolExpTwo.getLeft();
                            String twoRightValue = boolExpTwo.getRight();
                            Var varTemp = getVar(twoLeftVname,vars);
                            if(operatorTwo.equals("==")){
                                if(twoLeftVname.equals(varTemp.getvName())){
                                    if(twoRightValue.equals(varTemp.getValue())){
                                        flag = true;
                                    }else {
                                        flag = false;
                                    }
                                }
                            }
                            else{
                                if (varTemp.getValue().equalsIgnoreCase("True")){
                                    flag = true;
                                }else{
                                    flag = false;
                                }
                            }
                        }
                        else {
                            flag = false;
                        }
                    }//无操作符
                    else{
                        if(operatorOne.equals("==")){
                            if(oneLeftVname.equals(var.getvName())){
                                if(oneRightValue.equals(var.getValue())){
                                    String operatorTwo = boolExpTwo.getOperator();
                                    String twoLeftVname = boolExpTwo.getLeft();
                                    System.out.println(twoLeftVname);
                                    String twoRightValue = boolExpTwo.getRight();
                                    Var varTemp = getVar(twoLeftVname,vars);
                                    for (Var var1 : vars) {
                                        if(var1.getvName().equals(twoLeftVname)){
                                            System.out.println(var1);
                                        }
                                    }
                                    if(operatorTwo.equals("==")){
                                        if(twoLeftVname.equals(varTemp.getvName())){
                                            if(twoRightValue.equals(varTemp.getValue())){
                                                flag = true;
                                            }else {
                                                flag = false;
                                            }
                                        }
                                    }
                                    else{
                                        System.out.println(boolGuard);

//                                        if(varTemp == null){
//                                            System.out.println("ss");; //debug
//                                        }
//                                        if (varTemp.getValue() == null){
//
//                                            System.out.println("ss");; //debug
//                                        }
                                        if (varTemp.getValue().equalsIgnoreCase("True")){
                                            flag = true;
                                        }else{
                                            flag = false;
                                        }
                                    }
                                }else {
                                    flag = false;
                                }
                            }
                        }
                    }//有操作符 ==
                }//无not前缀 则有操作符和right
            }//如果带后缀为and
            else if(boolExpOne.getSuf().equals("or")){
                if(boolExpOne.isNotPre()){
                 //String leftVname = boolExpOne.getLeft();
                    //                    Var var = getVar(leftVname, vars);
                    //                    if (!var.getValue().equalsIgnoreCase("True")){
                    //                        flag = true;
                    //                    }//当结果为True时 由于not前缀 取反
                    //                    else {
                    //                        String operatorTwo = boolExpTwo.getOperator();
                    //                        String twoLeftVname = boolExpTwo.getLeft();
                    //                        String twoRightValue = boolExpTwo.getRight();
                    //                        Var varTemp = getVar(twoLeftVname, vars);
                    //                        if(operatorTwo.equals("==")) {
                    //                            if (twoLeftVname.equals(varTemp.getvName())) {
                    //                                if (twoRightValue.equals(varTemp.getValue())) {
                    //                                    flag = true;
                    //                                } else {
                    //                                    flag = false;
                    //                                }
                    //                            }
                    //                        }else{
                    //                            if (varTemp.getValue().equalsIgnoreCase("True")){
                    //                                flag = true;
                    //                            }else{
                    //                                flag = false;
                    //                            }
                    //                    }
                }//有not前缀
                else{
                    String oneLeftVname = boolExpOne.getLeft();
                    String oneRightValue = boolExpOne.getRight();
                    Var var = getVar(oneLeftVname,vars);
                    if(var.getvName().equals(oneLeftVname)){
                        if(var.getValue().equals(oneRightValue)){
                            flag = true;
                        }
                        else{
                            String twoLeftVname = boolExpTwo.getLeft();
                            String twoRightValue = boolExpTwo.getRight();
                            Var varTemp = getVar(twoLeftVname,vars);
                            if(varTemp.getvName().equals(twoLeftVname)){
                                if(varTemp.getValue().equals(twoRightValue)){
                                    flag = true;
                                }
                            }
                        }//第一个为false 则继续看第二个
                    }
                }//无not前缀
            }//如果带后缀为or
            else if(boolExpOne.getSuf().equals("pand")){ //zs
                            if(boolExpOne.isNotPre()){
                                String leftVname = boolExpOne.getLeft();
                                Var var = getVar(leftVname, vars);
                                if (var.getValue().equalsIgnoreCase("True")){
                                    flag = false;
                                }//当结果为True时 由于not前缀 取反
                                else {
                                    flag = true;//正确则考虑第二个
                                    String operatorTwo = boolExpTwo.getOperator();
                                    String twoLeftVname = boolExpTwo.getLeft();
                                    String twoRightValue = boolExpTwo.getRight();
                                    Var varTemp = getVar(twoLeftVname, vars);
                                    if(operatorTwo.equals("==")) {
                                        if (twoLeftVname.equals(varTemp.getvName())) {
                                            if (twoRightValue.equals(varTemp.getValue())){ // && var.state < varTemp.state) {
                                                flag = true;
                                            } else {
                                                flag = false;
                                            }
                                        }
                                    }else{
                                        if (varTemp.getValue().equalsIgnoreCase("True")){ // && var.state < varTemp.state){
                                            flag = true;
                                        }else{
                                            flag = false;
                                        }
                                }
                                }//当结果为False时 由于not前缀 取反
                            }//not前缀 则无操作符和right
                            else{
                                String operatorOne = boolExpOne.getOperator();
                                String oneLeftVname = boolExpOne.getLeft();
                                String oneRightValue = boolExpOne.getRight();
                                Var var = getVar(oneLeftVname, vars);
                                if(operatorOne.equals("")){
                                    if (var.getValue().equalsIgnoreCase("True")){
                                        String operatorTwo = boolExpTwo.getOperator();
                                        String twoLeftVname = boolExpTwo.getLeft();
                                        String twoRightValue = boolExpTwo.getRight();
                                        Var varTemp = getVar(twoLeftVname,vars);
                                        if(operatorTwo.equals("==")){
                                            if(twoLeftVname.equals(varTemp.getvName())){
                                                if(twoRightValue.equals(varTemp.getValue()) ){ //&& var.state < varTemp.state){
                                                    flag = true;
                                                }else {
                                                    flag = false;
                                                }
                                            }
                                        }
                                        else{
                                            if (varTemp.getValue().equalsIgnoreCase("True")){ // && var.state < varTemp.state){
                                                flag = true;
                                            }else{
                                                flag = false;
                                            }
                                        }
                                    }
                                    else {
                                        flag = false;
                                    }
                                }//无操作符
                                else{
                                    if(operatorOne.equals("==")){
                                        if(oneLeftVname.equals(var.getvName())){
                                            if(oneRightValue.equals(var.getValue())){
                                                String operatorTwo = boolExpTwo.getOperator();
                                                String twoLeftVname = boolExpTwo.getLeft();
                                                String twoRightValue = boolExpTwo.getRight();
                                                Var varTemp = getVar(twoLeftVname,vars);
                                                if(operatorTwo.equals("==")){
                                                    if(twoLeftVname.equals(varTemp.getvName())){
                                                        if(twoRightValue.equals(varTemp.getValue()) ){ //&& var.state < varTemp.state){
                                                            flag = true;
                                                        }else {
                                                            flag = false;
                                                        }
                                                    }
                                                }
                                                else{
                                                    if (varTemp.getValue().equalsIgnoreCase("True") ){ //&& var.state < varTemp.state){
                                                        flag = true;
                                                    }else{
                                                        flag = false;
                                                    }
                                                }
                                            }else {
                                                flag = false;
                                            }
                                        }
                                    }
                                }//有操作符 ==
                            }//无not前缀 则有操作符和right
                        }//如果带后缀为and
        }//双条件有后缀
        return flag;
    }

    public HashSet<Var> processInstruct(HashSet<Var> vars,ArrayList<Assertion> pInstruct){
        HashSet<Var> varArrayListTemp = new HashSet<>();
        varArrayListTemp.addAll(vars);
        ArrayList<Assertion> assertionHashSet = pInstruct;
        for(Assertion assertion : assertionHashSet){
            if(!assertion.isIfExist()){
                ArrayList<Instruct> instructArrayList = assertion.getInstructArrayList();
                Instruct instruct = instructArrayList.get(0);
                String variableReference = instruct.getVariableReference();
                String expression = instruct.getExpression();
                Var var = getVar(variableReference, varArrayListTemp);
                var.getDomain().add(expression);
                Var varTemp = new Var();
                varTemp.setType(var.getType());
                varTemp.setDomain(var.getDomain());
                if(!expression.equalsIgnoreCase("TRUE")){
                    varTemp.setValue(getVar(expression,varArrayListTemp).getValue());
                    varTemp.getDomain().add(getVar(expression,varArrayListTemp).getValue());
                }else{
                    varTemp.setValue(expression);
                }
                varTemp.setDependedVar(var.getDependedVar());
                varTemp.setDependOn(var.isDependOn());
                varTemp.setFlag(var.isFlag());
                varTemp.setInit(var.getInit());
                varTemp.setvName(var.getvName());
                varArrayListTemp.remove(var);
                varArrayListTemp.add(varTemp);
            }//没有if直接赋值
            else{
                ArrayList<BoolExp> boolExpArrayList = assertion.getBoolExpArrayList();
                if(processBoolExp(varArrayListTemp,boolExpArrayList)){
                    Instruct instruct = assertion.getInstructArrayList().get(0);
                    String variableReference = instruct.getVariableReference();
                    String expression = instruct.getExpression();
                    Var var = getVar(variableReference, varArrayListTemp);
                    var.getDomain().add(expression);
                    Var varTemp = new Var();
                    varTemp.setType(var.getType());
                    varTemp.setDomain(var.getDomain());
                    if(!expression.equalsIgnoreCase("TRUE")){
                        varTemp.setValue(getVar(expression,varArrayListTemp).getValue());
                        varTemp.getDomain().add(getVar(expression,varArrayListTemp).getValue());
                    }else{
                        varTemp.setValue(expression);
                    }
                    varTemp.setDependedVar(var.getDependedVar());
                    varTemp.setDependOn(var.isDependOn());
                    varTemp.setFlag(var.isFlag());
                    varTemp.setInit(var.getInit());
                    varTemp.setvName(var.getvName());
                    varArrayListTemp.remove(var);
                    varArrayListTemp.add(varTemp);
                }
            }//有if要判断
        }
        return varArrayListTemp;
    }

    public Var getVar(String varname,HashSet<Var> vars){
        Var var = new Var();
        for(Var vartemp : vars){
            if(vartemp.getvName().equals(varname)){
                var = vartemp;
            }
        }
        return var;
    }

}
