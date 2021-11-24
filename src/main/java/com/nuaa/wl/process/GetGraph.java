package com.nuaa.wl.process;

import com.nuaa.wl.definition.*;

import java.util.ArrayList;
import java.util.HashSet;

public class GetGraph {
    public void getGraph(HashSet<GTS> gtsHashSet){
        for(GTS gts : gtsHashSet){
            if(!gts.getGTSName().equals("independentAssertion")){
                System.out.println(gts.getGTSName()+":");
                ArrayList<Vnode> vnodes = new ArrayList<>();
                Vnode vnodeStart = new Vnode();
                vnodeStart.setNodeName("S0");
                HashSet<Var> vars = gts.getVars();
                vnodeStart.setVars(vars);
                vnodeStart.setFlag(false);
                vnodes.add(vnodeStart);
                //当节点集合中有节点没有处理过就要进行处理

                for(int i=0;i<vnodes.size() ;i++ ){
                    Vnode vnode = vnodes.get(i);
                    System.out.println(vnode);
                    if(!vnode.isFlag()){
                        for(Transition transition : gts.getTransitionArrayList()){
                                if(processBoolExp(vnode.getVars(),transition.getGuard().getBoolGuard())){
                                    HashSet<Var> newVarArraylist = processInstruct(vnode.getVars(),transition.getpInstruc());
                                    System.out.println("经过"+transition.getEventName());
                                    Next nextTemp = new Next();
                                    nextTemp.setTransitionName(transition.getEventName());
                                    nextTemp.setOutNode(vnode.getNodeName());
                                    if(contain(vnodes,newVarArraylist).equals("")){
                                        Vnode vnodeTemp = new Vnode();
                                        vnodeTemp.setFlag(false);
                                        vnodeTemp.setVars(newVarArraylist);
                                        int num = vnodes.size();
                                        vnodeTemp.setNodeName("S"+num);
                                        nextTemp.setInNode(vnodeTemp.getNodeName());
                                        vnode.getNexts().add(nextTemp);
                                        System.out.println("得到："+vnodeTemp);
                                        vnodes.add(vnodeTemp);
                                    }else {
                                        System.out.println("得到："+contain(vnodes,newVarArraylist));
                                        nextTemp.setInNode(contain(vnodes,newVarArraylist));
                                        vnode.getNexts().add(nextTemp);
                                    }
                                }//如果 当前的变量满足transition的条件则变量的值修改
                                else {
//                                System.out.println("不满足"+transition.getEventName());
                                }
                        }
                        vnode.setFlag(true);
                    }
                }
                System.out.println();
                gts.setGraph(vnodes);
            }
        }
    }

    public boolean processBoolExp(HashSet<Var> vars,ArrayList<BoolExp> guard){
        boolean flag = false;
        ArrayList<BoolExp> boolGuard = guard;
        if(boolGuard.size()==1){
            BoolExp boolExpTemp = boolGuard.get(0);
            if(boolExpTemp.isNotPre()){
                
            }//如果前缀为not
            else {
                if(!boolExpTemp.getOperator().equals("==")){
                    
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
                        String twoLeftVname = boolExpTwo.getLeft();
                        String twoRightValue = boolExpTwo.getRight();
                        Var varTemp = getVar(twoLeftVname,vars);
                        if(twoLeftVname.equals(varTemp.getvName())){
                            if(twoRightValue.equals(varTemp.getValue())){
                                flag = true;
                            }else {
                                flag = false;
                            }
                        }
                    }//当结果为False时 由于not前缀 取反
                }//not前缀 则无操作符和right
                else{
                    String leftVname = boolExpOne.getLeft();
                    Var var = getVar(leftVname, vars);
                    if (var.getValue().equalsIgnoreCase("True")){
                        flag = true;
                        String twoLeftVname = boolExpTwo.getLeft();
                        String twoRightValue = boolExpTwo.getRight();
                        Var varTemp = getVar(twoLeftVname,vars);
                        if(twoLeftVname.equals(varTemp.getvName())){
                            if(twoRightValue.equals(varTemp.getValue())){
                                flag = true;
                            }else {
                                flag = false;
                            }
                        }
                    }
                    else {
                        flag = false;
                    }
                }//无not前缀 则有操作符和right
            }//如果带后缀为and
            else if(boolExpOne.getSuf().equals("or")){
                if(boolExpOne.isNotPre()){

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
        }//双条件有后缀
        return flag;
    }

    public HashSet<Var> processInstruct(HashSet<Var> vars,HashSet<Assertion> pInstruct){
        HashSet<Var> varArrayListTemp = new HashSet<>();
        HashSet<Assertion> assertionHashSet = pInstruct;
        if(assertionHashSet.size()==1){
            for(Assertion assertion : assertionHashSet){
                Instruct instruct = assertion.getInstructArrayList().get(0);
                String variableReference = instruct.getVariableReference();
                String expression = instruct.getExpression();
                Var var = getVar(variableReference, vars);
                var.getDomain().add(expression);
                Var varTemp = new Var();
                varTemp.setType(var.getType());
                varTemp.setValue(expression);
                varTemp.setDependedVar(var.getDependedVar());
                varTemp.setDependOn(var.isDependOn());
                varTemp.setFlag(var.isFlag());
                varTemp.setDomain(var.getDomain());
                varTemp.setInit(var.getInit());
                varTemp.setvName(var.getvName());
                varArrayListTemp.addAll(vars);
                varArrayListTemp.remove(var);
                varArrayListTemp.add(varTemp);
            }
        }//如果直接赋值
        else{
            for(Assertion assertion : assertionHashSet){
                if(assertion.isIfExist()){
                    ArrayList<BoolExp> boolExpArrayList = assertion.getBoolExpArrayList();
                    if(processBoolExp(vars,boolExpArrayList)){
                        Instruct instruct = assertion.getInstructArrayList().get(0);
                        String variableReference = instruct.getVariableReference();
                        String expression = instruct.getExpression();
                        Var var = getVar(variableReference, vars);
                        var.getDomain().add(expression);
                        Var varTemp = new Var();
                        varTemp.setType(var.getType());
                        varTemp.setValue(expression);
                        varTemp.setDependedVar(var.getDependedVar());
                        varTemp.setDependOn(var.isDependOn());
                        varTemp.setFlag(var.isFlag());
                        varTemp.setDomain(var.getDomain());
                        varTemp.setInit(var.getInit());
                        varTemp.setvName(var.getvName());
                        if(varArrayListTemp.isEmpty()){
                            varArrayListTemp.addAll(vars);
                        }
                        varArrayListTemp.remove(var);
                        varArrayListTemp.add(varTemp);
                    }
                }//如果有if
            }
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

//    public boolean containAB(ArrayList<Vnode> vnodes,Vnode vnode){
//        boolean flag = false;
//        for(Vnode vnodeTemp : vnodes){
//            if(vnodeTemp.getVars().equals(vnode.getVars())){
//                flag = true;
//            }
//        }
//        return flag;
//    }

    public String contain(ArrayList<Vnode> vnodes,HashSet<Var> vars){
        String vnodeName = "";
        for(Vnode vnodeTemp : vnodes){
            if(vnodeTemp.getVars().equals(vars)){
                vnodeName = vnodeTemp.getNodeName();
            }
        }
        return vnodeName;
    }

}
