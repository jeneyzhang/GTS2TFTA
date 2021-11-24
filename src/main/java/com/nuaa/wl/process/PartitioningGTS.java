package com.nuaa.wl.process;

import com.nuaa.wl.definition.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PartitioningGTS {
    public HashSet<GTS> partitioningGTS(GTS gts){
        HashSet<GTS> gtsHashSet = new HashSet<>();
        ArrayList<Transition> transitionArrayList = gts.getTransitionArrayList();
        ArrayList<Assertion> assertionArrayList = gts.getAssertionArrayList();
        ArrayList<Event> eventArrayList = gts.getEventArrayList();
        HashSet<Var> varHashSet = gts.getVars();
        for(Assertion assertion : assertionArrayList){
            ArrayList<BoolExp> boolExpArrayList = assertion.getBoolExpArrayList();
            ArrayList<Instruct> instructArrayList = assertion.getInstructArrayList();
            for(Instruct instruct : instructArrayList){
                String variableReference = instruct.getVariableReference();
                String expression = instruct.getExpression();
                Var var = getVar(variableReference,varHashSet);
                var.setDependOn(true);
                //说明该变量是依赖变量
                HashSet<String> dependedVar = new HashSet<>();
                if(!(expression.equalsIgnoreCase("TRUE")||expression.equalsIgnoreCase("FALSE")||expression.equalsIgnoreCase("FAILED")||expression.equalsIgnoreCase("WORKING"))){
                    dependedVar.add(expression);
                }
                if(boolExpArrayList != null){
                    for(BoolExp boolExp : boolExpArrayList){
                        dependedVar.add(boolExp.getLeft());
                    }
                }
                var.getDependedVar().addAll(dependedVar);
            }
        }//找出依赖被依赖关系，并处理
        HashMap<String,HashSet<String>> transitionAndVars = new HashMap<>();
        for(Transition transition : transitionArrayList){
            HashSet<String> varT = new HashSet<>();
            HashSet<String> varNames = transition.getVarHashSet();
            HashSet<String> dependedVars = new HashSet<String>();
            for(String varname : varNames){
                Var var = getVar(varname, varHashSet);
                dependedVars.addAll(var.getDependedVar());
            }
            varT.addAll(varNames);
            varT.addAll(dependedVars);
            transitionAndVars.put(transition.getEventName(),varT);
        }

        HashSet<HashSet<String>> trans = new HashSet<>();
        for(Transition transition : transitionArrayList){
            HashSet<String> strings = transitionAndVars.get(transition.getEventName());
            HashSet<String> son = new HashSet<>();
            for(Transition transition1 : transitionArrayList){
                if(!transition1.getEventName().equals(transition.getEventName())){
                    if(strings.containsAll(transitionAndVars.get(transition1.getEventName()))){
                        son.add(transition.getEventName());
                        son.add(transition1.getEventName());
                    }
                }
            }
            trans.add(son);
        }

        int count = 1;

        HashSet<Var> independentAssertionVar = new HashSet<>();
        independentAssertionVar.addAll(gts.getVars());
        ArrayList<Transition> independentAssertionTran = new ArrayList<>();
        independentAssertionTran.addAll(gts.getTransitionArrayList());
        ArrayList<Event> independentAssertionEvent = new ArrayList<>();
        independentAssertionEvent.addAll(gts.getEventArrayList());

        for(HashSet<String> hashSet : trans){
            if(!hashSet.isEmpty()){
                GTS gtsTemp = new GTS();
                gtsTemp.setGTSName("Part"+""+(count));
                HashSet<Var> varHashSetTemp = new HashSet<>();
                ArrayList<Transition> transitionTemp = new ArrayList<>();
                ArrayList<Assertion> assertionTemp = new ArrayList<>();
                ArrayList<Event> eventTemp = new ArrayList<>();
                for(String tranName : hashSet){
                    for(Transition transition : transitionArrayList){
                        if(transition.getEventName().equalsIgnoreCase(tranName)){
                            transitionTemp.add(transition);
                            independentAssertionTran.remove(transition);
                            eventTemp.add(getEvent(tranName,eventArrayList));
                            independentAssertionEvent.remove(getEvent(tranName,eventArrayList));
                        }
                    }
                    HashSet<String> varset = transitionAndVars.get(tranName);
                    for(String vname : varset){
                        varHashSetTemp.add(getVar(vname,varHashSet));
                        independentAssertionVar.remove(getVar(vname,varHashSet));
                    }

                }
                gtsTemp.setVars(varHashSetTemp);
                gtsTemp.setTransitionArrayList(transitionTemp);
                gtsTemp.setAssertionArrayList(assertionTemp);
                gtsTemp.setEventArrayList(eventTemp);
                count++;
                gtsHashSet.add(gtsTemp);
            }
        }//独立GTS划分

        GTS independentAssertion = new GTS();


        independentAssertion.setGTSName("independentAssertion");
        independentAssertion.setEventArrayList(independentAssertionEvent);
        independentAssertion.setTransitionArrayList(independentAssertionTran);
        independentAssertion.setAssertionArrayList(gts.getAssertionArrayList());
        independentAssertion.setVars(independentAssertionVar);

        gtsHashSet.add(independentAssertion);
        return gtsHashSet;
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

    public Event getEvent(String eventName,ArrayList<Event> events){
        Event event = new Event();
        for(Event eventTemp : events){
            if(eventTemp.getEventName().equalsIgnoreCase(eventName)){
                event = eventTemp;
            }
        }
        return event;
    }
}
