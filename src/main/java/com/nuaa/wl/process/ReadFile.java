package com.nuaa.wl.process;

import com.nuaa.wl.definition.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class ReadFile {
    public GTS readFile(String fileName)throws IOException{
        GTS gts = new GTS();
        HashSet<Var> vars = new HashSet<>();//变量集合
        ArrayList<Event> eventArrayList = new ArrayList<>();//事件集合
        ArrayList<Transition> transitionArrayList = new ArrayList<>();//转换集合
        ArrayList<Assertion> assertionArrayList = new ArrayList<>();//断言集合
        InputStream path = this.getClass().getResourceAsStream("/"+fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(path));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            boolean block = line.startsWith("block");
            if(block){
                int index = line.indexOf(" ");
                gts.setGTSName(line.substring(index+1));
            }//获取GTS的名字
            boolean componentState = line.startsWith("ComponentState");
            boolean Boolean = line.startsWith("Boolean");
            boolean spareComponentState = line.startsWith("SpareComponentState");
            if(componentState||Boolean||spareComponentState){
                int index = line.indexOf(" ");
                int indexLeft = line.indexOf("(");
                int indexRight = line.indexOf(")");
                int indexMid = line.indexOf("=");
                Var var = new Var();
                String varName = line.substring(index + 1, indexLeft);
                String varType;
                String init = line.substring(indexMid+2,indexRight);
                String value = init;
                boolean flag = false;
                if(line.substring(indexLeft+1,indexMid-1).equals("init")){
                    flag = false;
                }else if(line.substring(indexLeft+1,indexMid-1).equals("reset")){
                    flag = true;
                }
                if(componentState||spareComponentState){
                    varType = "String";
                }else{
                    varType = "Boolean";
                }
                var.setType(varType);
                var.setvName(varName);
                var.setInit(init);
                var.setValue(value);
                var.getDomain().add(init);
                var.setFlag(flag);
                vars.add(var);
            }//获取GTS的变量
            boolean event = line.startsWith("event");
            if(event){
                Event eventTemp = new Event();
                int index = line.indexOf(" ");
                int indexEnd = line.indexOf(";");
                eventTemp.setEventName(line.substring(index+1,indexEnd));
                eventArrayList.add(eventTemp);
            }//获取转换事件名字
            boolean transition = line.startsWith("transition");
            if(transition){
                Transition transitionTemp = new Transition();
                HashSet<String> transitionVarHashSet = new HashSet<>();
                int indexspace = line.indexOf(" ");
                int indexcolon = line.indexOf(":");
                int indexarror = line.indexOf("->");
                int indexEnd = line.lastIndexOf(";");
                transitionTemp.setEventName(line.substring(indexspace+1,indexcolon-1));
                Guard guard = new Guard();
                ArrayList<BoolExp> boolExpArrayList = new ArrayList<>();
                String guardTemp = line.substring(indexcolon+2,indexarror-1);
                if(guardTemp.contains("and")){
                    int indexAnd = guardTemp.indexOf("and");
                    String leftBoolExp = guardTemp.substring(0,indexAnd-1);
                    String rightBoolExp = guardTemp.substring(indexAnd+4);
                    BoolExp boolExpOne = new BoolExp();
                    BoolExp boolExpTwo = new BoolExp();
                    boolExpOne.setSuf("and");
                    boolExpTwo.setSuf("");
                    if(leftBoolExp.contains("not")){
                        boolExpOne.setNotPre(true);
                        int indexNot = leftBoolExp.indexOf("not");
                        boolExpOne.setLeft(leftBoolExp.substring(indexNot+4,indexAnd-1));
                        boolExpOne.setOperator("");
                        boolExpOne.setRight("");
                    }else{
                        boolExpOne.setNotPre(false);
                        if(leftBoolExp.contains("==")){
                            int indexEqual = leftBoolExp.indexOf("==");
                            boolExpOne.setOperator("==");
                            boolExpOne.setLeft(leftBoolExp.substring(0,indexEqual-1));
                            boolExpOne.setRight(leftBoolExp.substring(indexEqual+3));
                        }else{
                            boolExpOne.setLeft(leftBoolExp.substring(0,indexAnd-1));
                            boolExpOne.setRight("");
                            boolExpOne.setOperator("");
                        }
                    }
                    if(rightBoolExp.contains("not")){
                        boolExpTwo.setNotPre(true);
                        int indexNot = rightBoolExp.indexOf("not");
                        boolExpTwo.setLeft(rightBoolExp.substring(indexNot+4));
                        boolExpTwo.setOperator("");
                        boolExpTwo.setRight("");
                    }else{
                        boolExpTwo.setNotPre(false);
                        if(rightBoolExp.contains("==")){
                            int indexEqual = rightBoolExp.indexOf("==");
                            boolExpTwo.setOperator("==");
                            boolExpTwo.setLeft(rightBoolExp.substring(0,indexEqual-1));
                            boolExpTwo.setRight(rightBoolExp.substring(indexEqual+3));
                        }else{
                            boolExpTwo.setLeft(rightBoolExp);
                            boolExpTwo.setRight("");
                            boolExpTwo.setOperator("");
                        }
                    }
                    boolExpArrayList.add(boolExpOne);
                    boolExpArrayList.add(boolExpTwo);
                    transitionVarHashSet.add(boolExpOne.getLeft());
                    transitionVarHashSet.add(boolExpTwo.getLeft());
                }
                else if(guardTemp.contains("or")){
                    int indexOr = guardTemp.indexOf("or");
                    String leftBoolExp = guardTemp.substring(0,indexOr-1);
                    String rightBoolExp = guardTemp.substring(indexOr+3);
                    BoolExp boolExpOne = new BoolExp();
                    BoolExp boolExpTwo = new BoolExp();
                    boolExpOne.setSuf("or");
                    boolExpTwo.setSuf("");
                    if(leftBoolExp.contains("not")){
                        boolExpOne.setNotPre(true);
                        int indexNot = leftBoolExp.indexOf("not");
                        boolExpOne.setLeft(leftBoolExp.substring(indexNot+4,indexOr-1));
                        boolExpOne.setOperator("");
                        boolExpOne.setRight("");
                    }else{
                        boolExpOne.setNotPre(false);
                        if(leftBoolExp.contains("==")){
                            int indexEqual = leftBoolExp.indexOf("==");
                            boolExpOne.setOperator("==");
                            boolExpOne.setLeft(leftBoolExp.substring(0,indexEqual-1));
                            boolExpOne.setRight(leftBoolExp.substring(indexEqual+3));
                        }else{
                            boolExpOne.setLeft(leftBoolExp.substring(0,indexOr-1));
                            boolExpOne.setRight("");
                            boolExpOne.setOperator("");
                        }
                    }
                    if(rightBoolExp.contains("not")){
                        boolExpTwo.setNotPre(true);
                        int indexNot = rightBoolExp.indexOf("not");
                        boolExpTwo.setLeft(rightBoolExp.substring(indexNot+4));
                        boolExpTwo.setOperator("");
                        boolExpTwo.setRight("");
                    }else{
                        boolExpTwo.setNotPre(false);
                        if(rightBoolExp.contains("==")){
                            int indexEqual = rightBoolExp.indexOf("==");
                            boolExpTwo.setOperator("==");
                            boolExpTwo.setLeft(rightBoolExp.substring(0,indexEqual-1));
                            boolExpTwo.setRight(rightBoolExp.substring(indexEqual+3));
                        }else{
                            boolExpTwo.setLeft(rightBoolExp);
                            boolExpTwo.setRight("");
                            boolExpTwo.setOperator("");
                        }
                    }
                    boolExpArrayList.add(boolExpOne);
                    boolExpArrayList.add(boolExpTwo);
                    transitionVarHashSet.add(boolExpOne.getLeft());
                    transitionVarHashSet.add(boolExpTwo.getLeft());
                }
         /*        else if(guardTemp.contains("pand")){ //注释头
                                   int indexPand = guardTemp.indexOf("pand");
                                  String leftBoolExp = guardTemp.substring(0,indexPand-1);
                                  String rightBoolExp = guardTemp.substring(indexPand+5);
                                   BoolExp boolExpOne = new BoolExp();
                                   BoolExp boolExpTwo = new BoolExp();
                                   boolExpOne.setSuf("pand");
                                   boolExpTwo.setSuf("");
                                   if(leftBoolExp.contains("not")){
                                       boolExpOne.setNotPre(true);
                                       int indexNot = leftBoolExp.indexOf("not");
                                       boolExpOne.setLeft(leftBoolExp.substring(indexNot+4,indexPand-1));
                                       boolExpOne.setOperator("");
                                       boolExpOne.setRight("");
                                   }else{
                                       boolExpOne.setNotPre(false);
                                       if(leftBoolExp.contains("==")){
                                           int indexEqual = leftBoolExp.indexOf("==");
                                           boolExpOne.setOperator("==");
                                           boolExpOne.setLeft(leftBoolExp.substring(0,indexEqual-1));
                                           boolExpOne.setRight(leftBoolExp.substring(indexEqual+3));
                                      }else{
                                           boolExpOne.setLeft(leftBoolExp.substring(0,indexPand-1));
                                           boolExpOne.setRight("");
                                           boolExpOne.setOperator("");
                                       }
                                   }
                                   if(rightBoolExp.contains("not")){
                                       boolExpTwo.setNotPre(true);
                                       int indexNot = rightBoolExp.indexOf("not");
                                       boolExpTwo.setLeft(rightBoolExp.substring(indexNot+4));
                                       boolExpTwo.setOperator("");
                                       boolExpTwo.setRight("");
                                   }else{
                                       boolExpTwo.setNotPre(false);
                                       if(rightBoolExp.contains("==")){
                                           int indexEqual = rightBoolExp.indexOf("==");
                                           boolExpTwo.setOperator("==");
                                           boolExpTwo.setLeft(rightBoolExp.substring(0,indexEqual-1));
                                           boolExpTwo.setRight(rightBoolExp.substring(indexEqual+3));
                                       }else{
                                           boolExpTwo.setLeft(rightBoolExp);
                                           boolExpTwo.setRight("");
                                           boolExpTwo.setOperator("");
                                        }
                                    }
                                   boolExpArrayList.add(boolExpOne);
                                   boolExpArrayList.add(boolExpTwo);
                                   transitionVarHashSet.add(boolExpOne.getLeft());
                                   transitionVarHashSet.add(boolExpTwo.getLeft());
                                } //注释尾  */

                // else if(guardTemp.contains("por")){
                //                    int indexPor = guardTemp.indexOf("por");
                //                    String leftBoolExp = guardTemp.substring(0,indexPor-1);
                //                    String rightBoolExp = guardTemp.substring(indexPor+4);
                //                    BoolExp boolExpOne = new BoolExp();
                //                    BoolExp boolExpTwo = new BoolExp();
                //                    boolExpOne.setSuf("por");
                //                    boolExpTwo.setSuf("");
                //                    if(leftBoolExp.contains("not")){
                //                        boolExpOne.setNotPre(true);
                //                        int indexNot = leftBoolExp.indexOf("not");
                //                        boolExpOne.setLeft(leftBoolExp.substring(indexNot+4,indexPor-1));
                //                        boolExpOne.setOperator("");
                //                        boolExpOne.setRight("");
                //                    }else{
                //                        boolExpOne.setNotPre(false);
                //                        if(leftBoolExp.contains("==")){
                //                            int indexEqual = leftBoolExp.indexOf("==");
                //                            boolExpOne.setOperator("==");
                //                            boolExpOne.setLeft(leftBoolExp.substring(0,indexEqual-1));
                //                            boolExpOne.setRight(leftBoolExp.substring(indexEqual+3));
                //                        }else{
                //                            boolExpOne.setLeft(leftBoolExp.substring(0,indexPor-1));
                //                            boolExpOne.setRight("");
                //                            boolExpOne.setOperator("");
                //                        }
                //                    }
                //                    if(rightBoolExp.contains("not")){
                //                        boolExpTwo.setNotPre(true);
                //                        int indexNot = rightBoolExp.indexOf("not");
                //                        boolExpTwo.setLeft(rightBoolExp.substring(indexNot+4));
                //                        boolExpTwo.setOperator("");
                //                        boolExpTwo.setRight("");
                //                    }else{
                //                        boolExpTwo.setNotPre(false);
                //                        if(rightBoolExp.contains("==")){
                //                            int indexEqual = rightBoolExp.indexOf("==");
                //                            boolExpTwo.setOperator("==");
                //                            boolExpTwo.setLeft(rightBoolExp.substring(0,indexEqual-1));
                //                            boolExpTwo.setRight(rightBoolExp.substring(indexEqual+3));
                //                        }else{
                //                            boolExpTwo.setLeft(rightBoolExp);
                //                            boolExpTwo.setRight("");
                //                            boolExpTwo.setOperator("");
                //                        }
                //                    }
                //                    boolExpArrayList.add(boolExpOne);
                //                    boolExpArrayList.add(boolExpTwo);
                //                    transitionVarHashSet.add(boolExpOne.getLeft());
                //                    transitionVarHashSet.add(boolExpTwo.getLeft());
                //                }

                // else if(guardTemp.contains("sand")){
                //                    int indexSand = guardTemp.indexOf("sand");
                //                    String leftBoolExp = guardTemp.substring(0,indexSand-1);
                //                    String rightBoolExp = guardTemp.substring(indexSand+5);
                //                    BoolExp boolExpOne = new BoolExp();
                //                    BoolExp boolExpTwo = new BoolExp();
                //                    boolExpOne.setSuf("sand");
                //                    boolExpTwo.setSuf("");
                //                    if(leftBoolExp.contains("not")){
                //                        boolExpOne.setNotPre(true);
                //                        int indexNot = leftBoolExp.indexOf("not");
                //                        boolExpOne.setLeft(leftBoolExp.substring(indexNot+4,indexSand-1));
                //                        boolExpOne.setOperator("");
                //                        boolExpOne.setRight("");
                //                    }else{
                //                        boolExpOne.setNotPre(false);
                //                        if(leftBoolExp.contains("==")){
                //                            int indexEqual = leftBoolExp.indexOf("==");
                //                            boolExpOne.setOperator("==");
                //                            boolExpOne.setLeft(leftBoolExp.substring(0,indexEqual-1));
                //                            boolExpOne.setRight(leftBoolExp.substring(indexEqual+3));
                //                        }else{
                //                            boolExpOne.setLeft(leftBoolExp.substring(0,indexSand-1));
                //                            boolExpOne.setRight("");
                //                            boolExpOne.setOperator("");
                //                        }
                //                    }
                //                    if(rightBoolExp.contains("not")){
                //                        boolExpTwo.setNotPre(true);
                //                        int indexNot = rightBoolExp.indexOf("not");
                //                        boolExpTwo.setLeft(rightBoolExp.substring(indexNot+4));
                //                        boolExpTwo.setOperator("");
                //                        boolExpTwo.setRight("");
                //                    }else{
                //                        boolExpTwo.setNotPre(false);
                //                        if(rightBoolExp.contains("==")){
                //                            int indexEqual = rightBoolExp.indexOf("==");
                //                            boolExpTwo.setOperator("==");
                //                            boolExpTwo.setLeft(rightBoolExp.substring(0,indexEqual-1));
                //                            boolExpTwo.setRight(rightBoolExp.substring(indexEqual+3));
                //                        }else{
                //                            boolExpTwo.setLeft(rightBoolExp);
                //                            boolExpTwo.setRight("");
                //                            boolExpTwo.setOperator("");
                //                        }
                //                    }
                //                    boolExpArrayList.add(boolExpOne);
                //                    boolExpArrayList.add(boolExpTwo);
                //                    transitionVarHashSet.add(boolExpOne.getLeft());
                //                    transitionVarHashSet.add(boolExpTwo.getLeft());
                //                }
                else{
                    BoolExp boolExp = new BoolExp();
                    String boolExpTemp = guardTemp;
                    boolExp.setSuf("");
                    if(boolExpTemp.contains("not")){
                        boolExp.setNotPre(true);
                        int indexNot = boolExpTemp.indexOf("not");
                        boolExp.setLeft(boolExpTemp.substring(indexNot+4));
                        boolExp.setOperator("");
                        boolExp.setRight("");
                    }else{
                        boolExp.setNotPre(false);
                        if(boolExpTemp.contains("==")){
                            int indexEqual = boolExpTemp.indexOf("==");
                            boolExp.setOperator("==");
                            boolExp.setLeft(boolExpTemp.substring(0,indexEqual-1));
                            boolExp.setRight(boolExpTemp.substring(indexEqual+3));
                        }else{
                            boolExp.setLeft(boolExpTemp);
                            boolExp.setRight("");
                            boolExp.setOperator("");
                        }
                    }
                    boolExpArrayList.add(boolExp);
                    transitionVarHashSet.add(boolExp.getLeft());
                }
                guard.setBoolGuard(boolExpArrayList);
                transitionTemp.setGuard(guard);

                String instruct = line.substring(indexarror+3,indexEnd);
                HashSet<Assertion> pInstruc = new HashSet<>();
                if(instruct.contains("{")){
                    instruct = instruct.replace("{","");
                    instruct = instruct.replace("}","");
                    int a = instruct.indexOf(";");
                    int b = 0;
                    while(a!=-1){
                        String temp = instruct.substring(b,a);
                        Assertion assertionTemp = new Assertion();
                        ArrayList<BoolExp> assertionboolExpArrayList = new ArrayList<>();
                        ArrayList<Instruct> instructArrayList = new ArrayList<>();
                        HashSet<String> dependVar;
                        HashSet<String> dependedVar;
                        if(temp.contains("if")){
                            assertionTemp.setIfExist(true);
                            int indexif = temp.indexOf("if");
                            int indexthen = temp.indexOf("then");
                            String assertionboolexp = temp.substring(indexif+3,indexthen-1);
                            if(assertionboolexp.contains("and")){
                                int indexAssertionAnd = assertionboolexp.indexOf("and");
                                String assertionLeftBoolExp = assertionboolexp.substring(0,indexAssertionAnd-1);
                                String assertionRightBoolExp = assertionboolexp.substring(indexAssertionAnd+4);
                                BoolExp assertionBoolExpOne = new BoolExp();
                                BoolExp assertionBoolExpTwo = new BoolExp();
                                assertionBoolExpOne.setSuf("and");
                                assertionBoolExpTwo.setSuf("");
                                if(assertionLeftBoolExp.contains("not")){
                                    assertionBoolExpOne.setNotPre(true);
                                    int assertionIndexNot = assertionLeftBoolExp.indexOf("not");
                                    assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(assertionIndexNot+4,indexAssertionAnd-1));
                                    assertionBoolExpOne.setOperator("");
                                    assertionBoolExpOne.setRight("");
                                }else{
                                    assertionBoolExpOne.setNotPre(false);
                                    if(assertionLeftBoolExp.contains("==")){
                                        int assertionIndexEqual = assertionLeftBoolExp.indexOf("==");
                                        assertionBoolExpOne.setOperator("==");
                                        assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexEqual-1));
                                        assertionBoolExpOne.setRight(assertionLeftBoolExp.substring(assertionIndexEqual+3));
                                    }else{
                                        assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,indexAssertionAnd-1));
                                        assertionBoolExpOne.setRight("");
                                        assertionBoolExpOne.setOperator("");
                                    }
                                }
                                if(assertionRightBoolExp.contains("not")){
                                    assertionBoolExpTwo.setNotPre(true);
                                    int assertionIndexNot = assertionRightBoolExp.indexOf("not");
                                    assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(assertionIndexNot+4));
                                    assertionBoolExpTwo.setOperator("");
                                    assertionBoolExpTwo.setRight("");
                                }else{
                                    assertionBoolExpTwo.setNotPre(false);
                                    if(assertionRightBoolExp.contains("==")){
                                        int indexEqual = assertionRightBoolExp.indexOf("==");
                                        assertionBoolExpTwo.setOperator("==");
                                        assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(0,indexEqual-1));
                                        assertionBoolExpTwo.setRight(assertionRightBoolExp.substring(indexEqual+3));
                                    }else{
                                        assertionBoolExpTwo.setLeft(assertionRightBoolExp);
                                        assertionBoolExpTwo.setRight("");
                                        assertionBoolExpTwo.setOperator("");
                                    }
                                }
                                assertionboolExpArrayList.add(assertionBoolExpOne);
                                assertionboolExpArrayList.add(assertionBoolExpTwo);
                                transitionVarHashSet.add(assertionBoolExpOne.getLeft());
                                transitionVarHashSet.add(assertionBoolExpTwo.getLeft());
                            }
                            else if(assertionboolexp.contains("or")){
                                int assertionIndexOr = assertionboolexp.indexOf("or");
                                String assertionLeftBoolExp = assertionboolexp.substring(0,assertionIndexOr-1);
                                String assertionRightBoolExp = assertionboolexp.substring(assertionIndexOr+3);
                                BoolExp assertionBoolExpOne = new BoolExp();
                                BoolExp assertionBoolExpTwo = new BoolExp();
                                assertionBoolExpOne.setSuf("or");
                                assertionBoolExpTwo.setSuf("");
                                if(assertionLeftBoolExp.contains("not")){
                                    assertionBoolExpOne.setNotPre(true);
                                    int assertionIndexNot = assertionLeftBoolExp.indexOf("not");
                                    assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(assertionIndexNot+4,assertionIndexOr-1));
                                    assertionBoolExpOne.setOperator("");
                                    assertionBoolExpOne.setRight("");
                                }else{
                                    assertionBoolExpOne.setNotPre(false);
                                    if(assertionLeftBoolExp.contains("==")){
                                        int assertionIndexEqual = assertionLeftBoolExp.indexOf("==");
                                        assertionBoolExpOne.setOperator("==");
                                        assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexEqual-1));
                                        assertionBoolExpOne.setRight(assertionLeftBoolExp.substring(assertionIndexEqual+3));
                                    }else{
                                        assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexOr-1));
                                        assertionBoolExpOne.setRight("");
                                        assertionBoolExpOne.setOperator("");
                                    }
                                }
                                if(assertionRightBoolExp.contains("not")){
                                    assertionBoolExpTwo.setNotPre(true);
                                    int assertionIndexNot = assertionRightBoolExp.indexOf("not");
                                    assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(assertionIndexNot+4));
                                    assertionBoolExpTwo.setOperator("");
                                    assertionBoolExpTwo.setRight("");
                                }else{
                                    assertionBoolExpTwo.setNotPre(false);
                                    if(assertionRightBoolExp.contains("==")){
                                        int assertionIndexEqual = assertionRightBoolExp.indexOf("==");
                                        assertionBoolExpTwo.setOperator("==");
                                        assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(0,assertionIndexEqual-1));
                                        assertionBoolExpTwo.setRight(assertionRightBoolExp.substring(assertionIndexEqual+3));
                                    }else{
                                        assertionBoolExpTwo.setLeft(assertionRightBoolExp);
                                        assertionBoolExpTwo.setRight("");
                                        assertionBoolExpTwo.setOperator("");
                                    }
                                }
                                assertionboolExpArrayList.add(assertionBoolExpOne);
                                assertionboolExpArrayList.add(assertionBoolExpTwo);
                                transitionVarHashSet.add(assertionBoolExpOne.getLeft());
                                transitionVarHashSet.add(assertionBoolExpTwo.getLeft());
                            }
                 /*           else if(assertionboolexp.contains("pand")){ //注释头
                                                            int assertionIndexPand = assertionboolexp.indexOf("pand");
                                                            String assertionLeftBoolExp = assertionboolexp.substring(0,assertionIndexPand-1);
                                                            String assertionRightBoolExp = assertionboolexp.substring(assertionIndexPand+5);
                                                            BoolExp assertionBoolExpOne = new BoolExp();
                                                            BoolExp assertionBoolExpTwo = new BoolExp();
                                                            assertionBoolExpOne.setSuf("pand");
                                                            assertionBoolExpTwo.setSuf("");
                                                            if(assertionLeftBoolExp.contains("not")){
                                                                assertionBoolExpOne.setNotPre(true);
                                                                int assertionIndexNot = assertionLeftBoolExp.indexOf("not");
                                                                assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(assertionIndexNot+4,assertionIndexPand-1));
                                                                assertionBoolExpOne.setOperator("");
                                                                assertionBoolExpOne.setRight("");
                                                            }else{
                                                                assertionBoolExpOne.setNotPre(false);
                                                                if(assertionLeftBoolExp.contains("==")){
                                                                    int assertionIndexEqual = assertionLeftBoolExp.indexOf("==");
                                                                    assertionBoolExpOne.setOperator("==");
                                                                    assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexEqual-1));
                                                                    assertionBoolExpOne.setRight(assertionLeftBoolExp.substring(assertionIndexEqual+3));
                                                                }else{
                                                                    assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexPand-1));
                                                                    assertionBoolExpOne.setRight("");
                                                                    assertionBoolExpOne.setOperator("");
                                                                }
                                                            }
                                                            if(assertionRightBoolExp.contains("not")){
                                                                assertionBoolExpTwo.setNotPre(true);
                                                                int assertionIndexNot = assertionRightBoolExp.indexOf("not");
                                                                assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(assertionIndexNot+4));
                                                                assertionBoolExpTwo.setOperator("");
                                                                assertionBoolExpTwo.setRight("");
                                                            }else{
                                                                assertionBoolExpTwo.setNotPre(false);
                                                                if(assertionRightBoolExp.contains("==")){
                                                                    int assertionIndexEqual = assertionRightBoolExp.indexOf("==");
                                                                    assertionBoolExpTwo.setOperator("==");
                                                                    assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(0,assertionIndexEqual-1));
                                                                    assertionBoolExpTwo.setRight(assertionRightBoolExp.substring(assertionIndexEqual+3));
                                                                }else{
                                                                    assertionBoolExpTwo.setLeft(assertionRightBoolExp);
                                                                    assertionBoolExpTwo.setRight("");
                                                                    assertionBoolExpTwo.setOperator("");
                                                                }
                                                            }
                                                            assertionboolExpArrayList.add(assertionBoolExpOne);
                                                            assertionboolExpArrayList.add(assertionBoolExpTwo);
                                                            transitionVarHashSet.add(assertionBoolExpOne.getLeft());
                                                            transitionVarHashSet.add(assertionBoolExpTwo.getLeft());
                                                        } //注释尾  */
                            else{
                                BoolExp boolExp = new BoolExp();
                                String boolExpTemp = assertionboolexp;
                                boolExp.setSuf("");
                                if(boolExpTemp.contains("not")){
                                    boolExp.setNotPre(true);
                                    int indexNot = boolExpTemp.indexOf("not");
                                    boolExp.setLeft(boolExpTemp.substring(indexNot+4));
                                    boolExp.setOperator("");
                                    boolExp.setRight("");
                                }else{
                                    boolExp.setNotPre(false);
                                    if(boolExpTemp.contains("==")){
                                        int indexEqual = boolExpTemp.indexOf("==");
                                        boolExp.setOperator("==");
                                        boolExp.setLeft(boolExpTemp.substring(0,indexEqual-1));
                                        boolExp.setRight(boolExpTemp.substring(indexEqual+3));
                                    }else{
                                        boolExp.setLeft(boolExpTemp);
                                        boolExp.setRight("");
                                        boolExp.setOperator("");
                                    }
                                }
                                assertionboolExpArrayList.add(boolExp);
                                transitionVarHashSet.add(boolExp.getLeft());
                            }
                            String instructTemp = temp.substring(indexthen+5);
                            int indexassignment = instructTemp.indexOf(":=");
                            assertionTemp.setBoolExpArrayList(assertionboolExpArrayList);
                            Instruct instructEnd = new Instruct();
                            instructEnd.setVariableReference(instructTemp.substring(0,indexassignment-1));
                            instructEnd.setExpression(instructTemp.substring(indexassignment+3));
                            instructArrayList.add(instructEnd);
                            assertionTemp.setInstructArrayList(instructArrayList);
                        }
                        else{
                            int indexassignment = temp.indexOf(":=");
                            assertionTemp.setBoolExpArrayList(null);
                            assertionTemp.setIfExist(false);
                            Instruct instructEnd = new Instruct();
                            instructEnd.setVariableReference(temp.substring(0,indexassignment-1));
                            instructEnd.setExpression(temp.substring(indexassignment+3));
                            instructArrayList.add(instructEnd);
                            assertionTemp.setInstructArrayList(instructArrayList);
                        }
                        b = b+a+1;
                        a = instruct.indexOf(";",a+1);
                        pInstruc.add(assertionTemp);
                    }
                }
                else{
                    int indexassignment = instruct.indexOf(":=");
                    Assertion assertionTemp = new Assertion();
                    ArrayList<Instruct> instructArrayList = new ArrayList<>();
                    Instruct instructTemp = new Instruct();
                    instructTemp.setVariableReference(instruct.substring(0,indexassignment-1));
                    instructTemp.setExpression(instruct.substring(indexassignment+3));
                    instructArrayList.add(instructTemp);
                    assertionTemp.setIfExist(false);
                    assertionTemp.setBoolExpArrayList(null);
                    assertionTemp.setDependedVar(null);
                    assertionTemp.setInstructArrayList(instructArrayList);
                    assertionTemp.setDependVar(null);
                    pInstruc.add(assertionTemp);
                }
                transitionTemp.setpInstruc(pInstruc);
                transitionTemp.setVarHashSet(transitionVarHashSet);
                transitionArrayList.add(transitionTemp);
            }
            //获取转换的数据
            boolean assertion = line.startsWith("assertion");
            if(assertion){
                Assertion assertionTemp = new Assertion();
                ArrayList<BoolExp> assertionboolExpArrayList = new ArrayList<>();
                ArrayList<Instruct>instructArrayList = new ArrayList<>();
                int index = line.indexOf(" ");
                int indexEnd = line.lastIndexOf(";");
                String temp = line.substring(index+1,indexEnd);

                if(temp.contains("if")){
                    assertionTemp.setIfExist(true);
                    int indexif = temp.indexOf("if");
                    int indexthen = temp.indexOf("then");
                    String assertionboolexp = temp.substring(indexif+3,indexthen-1);
                    if(assertionboolexp.contains("and")){
                        int indexAssertionAnd = assertionboolexp.indexOf("and");
                        String assertionLeftBoolExp = assertionboolexp.substring(0,indexAssertionAnd-1);
                        String assertionRightBoolExp = assertionboolexp.substring(indexAssertionAnd+4);
                        BoolExp assertionBoolExpOne = new BoolExp();
                        BoolExp assertionBoolExpTwo = new BoolExp();
                        assertionBoolExpOne.setSuf("and");
                        assertionBoolExpTwo.setSuf("");
                        if(assertionLeftBoolExp.contains("not")){
                            assertionBoolExpOne.setNotPre(true);
                            int assertionIndexNot = assertionLeftBoolExp.indexOf("not");
                            assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(assertionIndexNot+4,indexAssertionAnd-1));
                            assertionBoolExpOne.setOperator("");
                            assertionBoolExpOne.setRight("");
                            assertionTemp.getDependedVar().add(assertionBoolExpOne.getLeft());
                        }else{
                            assertionBoolExpOne.setNotPre(false);
                            if(assertionLeftBoolExp.contains("==")){
                                int assertionIndexEqual = assertionLeftBoolExp.indexOf("==");
                                assertionBoolExpOne.setOperator("==");
                                assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexEqual-1));
                                assertionBoolExpOne.setRight(assertionLeftBoolExp.substring(assertionIndexEqual+3));
                            }else{
                                assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,indexAssertionAnd-1));
                                assertionBoolExpOne.setRight("");
                                assertionBoolExpOne.setOperator("");
                            }
                            assertionTemp.getDependedVar().add(assertionBoolExpOne.getLeft());
                        }
                        if(assertionRightBoolExp.contains("not")){
                            assertionBoolExpTwo.setNotPre(true);
                            int assertionIndexNot = assertionRightBoolExp.indexOf("not");
                            assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(assertionIndexNot+4));
                            assertionBoolExpTwo.setOperator("");
                            assertionBoolExpTwo.setRight("");
                            assertionTemp.getDependedVar().add(assertionBoolExpTwo.getLeft());
                        }else{
                            assertionBoolExpTwo.setNotPre(false);
                            if(assertionRightBoolExp.contains("==")){
                                int indexEqual = assertionRightBoolExp.indexOf("==");
                                assertionBoolExpTwo.setOperator("==");
                                assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(0,indexEqual-1));
                                assertionBoolExpTwo.setRight(assertionRightBoolExp.substring(indexEqual+3));
                            }else{
                                assertionBoolExpTwo.setLeft(assertionRightBoolExp);
                                assertionBoolExpTwo.setRight("");
                                assertionBoolExpTwo.setOperator("");
                            }
                            assertionTemp.getDependedVar().add(assertionBoolExpTwo.getLeft());
                        }
                        assertionboolExpArrayList.add(assertionBoolExpOne);
                        assertionboolExpArrayList.add(assertionBoolExpTwo);
                    }
                    else if(assertionboolexp.contains("or")){
                        int assertionIndexOr = assertionboolexp.indexOf("or");
                        String assertionLeftBoolExp = assertionboolexp.substring(0,assertionIndexOr-1);
                        String assertionRightBoolExp = assertionboolexp.substring(assertionIndexOr+3);
                        BoolExp assertionBoolExpOne = new BoolExp();
                        BoolExp assertionBoolExpTwo = new BoolExp();
                        assertionBoolExpOne.setSuf("or");
                        assertionBoolExpTwo.setSuf("");
                        if(assertionLeftBoolExp.contains("not")){
                            assertionBoolExpOne.setNotPre(true);
                            int assertionIndexNot = assertionLeftBoolExp.indexOf("not");
                            assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(assertionIndexNot+4,assertionIndexOr-1));
                            assertionBoolExpOne.setOperator("");
                            assertionBoolExpOne.setRight("");
                            assertionTemp.getDependedVar().add(assertionBoolExpOne.getLeft());
                        }else{
                            assertionBoolExpOne.setNotPre(false);
                            if(assertionLeftBoolExp.contains("==")){
                                int assertionIndexEqual = assertionLeftBoolExp.indexOf("==");
                                assertionBoolExpOne.setOperator("==");
                                assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexEqual-1));
                                assertionBoolExpOne.setRight(assertionLeftBoolExp.substring(assertionIndexEqual+3));
                            }else{
                                assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexOr-1));
                                assertionBoolExpOne.setRight("");
                                assertionBoolExpOne.setOperator("");
                            }
                            assertionTemp.getDependedVar().add(assertionBoolExpOne.getLeft());
                        }
                        if(assertionRightBoolExp.contains("not")){
                            assertionBoolExpTwo.setNotPre(true);
                            int assertionIndexNot = assertionRightBoolExp.indexOf("not");
                            assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(assertionIndexNot+4));
                            assertionBoolExpTwo.setOperator("");
                            assertionBoolExpTwo.setRight("");
                            assertionTemp.getDependedVar().add(assertionBoolExpTwo.getLeft());
                        }else{
                            assertionBoolExpTwo.setNotPre(false);
                            if(assertionRightBoolExp.contains("==")){
                                int assertionIndexEqual = assertionRightBoolExp.indexOf("==");
                                assertionBoolExpTwo.setOperator("==");
                                assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(0,assertionIndexEqual-1));
                                assertionBoolExpTwo.setRight(assertionRightBoolExp.substring(assertionIndexEqual+3));
                            }else{
                                assertionBoolExpTwo.setLeft(assertionRightBoolExp);
                                assertionBoolExpTwo.setRight("");
                                assertionBoolExpTwo.setOperator("");
                            }
                            assertionTemp.getDependedVar().add(assertionBoolExpTwo.getLeft());
                        }
                        assertionboolExpArrayList.add(assertionBoolExpOne);
                        assertionboolExpArrayList.add(assertionBoolExpTwo);
                    }
           /*           else if(assertionboolexp.contains("pand")){ //注释头
                                            int assertionIndexPand = assertionboolexp.indexOf("pand");
                                            String assertionLeftBoolExp = assertionboolexp.substring(0,assertionIndexPand-1);
                                            String assertionRightBoolExp = assertionboolexp.substring(assertionIndexPand+5);
                                            BoolExp assertionBoolExpOne = new BoolExp();
                                            BoolExp assertionBoolExpTwo = new BoolExp();
                                            assertionBoolExpOne.setSuf("pand");
                                            assertionBoolExpTwo.setSuf("");
                                            if(assertionLeftBoolExp.contains("not")){
                                                assertionBoolExpOne.setNotPre(true);
                                                int assertionIndexNot = assertionLeftBoolExp.indexOf("not");
                                                assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(assertionIndexNot+4,assertionIndexPand-1));
                                                assertionBoolExpOne.setOperator("");
                                                assertionBoolExpOne.setRight("");
                                                assertionTemp.getDependedVar().add(assertionBoolExpOne.getLeft());
                                            }else{
                                                assertionBoolExpOne.setNotPre(false);
                                                if(assertionLeftBoolExp.contains("==")){
                                                    int assertionIndexEqual = assertionLeftBoolExp.indexOf("==");
                                                    assertionBoolExpOne.setOperator("==");
                                                    assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexEqual-1));
                                                    assertionBoolExpOne.setRight(assertionLeftBoolExp.substring(assertionIndexEqual+3));
                                                }else{
                                                    assertionBoolExpOne.setLeft(assertionLeftBoolExp.substring(0,assertionIndexPand-1));
                                                    assertionBoolExpOne.setRight("");
                                                    assertionBoolExpOne.setOperator("");
                                                }
                                                assertionTemp.getDependedVar().add(assertionBoolExpOne.getLeft());
                                            }
                                            if(assertionRightBoolExp.contains("not")){
                                                assertionBoolExpTwo.setNotPre(true);
                                                int assertionIndexNot = assertionRightBoolExp.indexOf("not");
                                                assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(assertionIndexNot+4));
                                                assertionBoolExpTwo.setOperator("");
                                                assertionBoolExpTwo.setRight("");
                                                assertionTemp.getDependedVar().add(assertionBoolExpTwo.getLeft());
                                            }else{
                                                assertionBoolExpTwo.setNotPre(false);
                                                if(assertionRightBoolExp.contains("==")){
                                                    int assertionIndexEqual = assertionRightBoolExp.indexOf("==");
                                                   assertionBoolExpTwo.setOperator("==");
                                                    assertionBoolExpTwo.setLeft(assertionRightBoolExp.substring(0,assertionIndexEqual-1));
                                                   assertionBoolExpTwo.setRight(assertionRightBoolExp.substring(assertionIndexEqual+3));
                                                }else{
                                                    assertionBoolExpTwo.setLeft(assertionRightBoolExp);
                                                    assertionBoolExpTwo.setRight("");
                                                    assertionBoolExpTwo.setOperator("");
                                                }
                                                assertionTemp.getDependedVar().add(assertionBoolExpTwo.getLeft());
                                            }
                                            assertionboolExpArrayList.add(assertionBoolExpOne);
                                            assertionboolExpArrayList.add(assertionBoolExpTwo);
                                        } //注释尾   */
                    else{
                        BoolExp boolExp = new BoolExp();
                        String boolExpTemp = assertionboolexp;
                        boolExp.setSuf("");
                        if(boolExpTemp.contains("not")){
                            boolExp.setNotPre(true);
                            int indexNot = boolExpTemp.indexOf("not");
                            boolExp.setLeft(boolExpTemp.substring(indexNot+4));
                            boolExp.setOperator("");
                            boolExp.setRight("");
                        }else{
                            boolExp.setNotPre(false);
                            if(boolExpTemp.contains("==")){
                                int indexEqual = boolExpTemp.indexOf("==");
                                boolExp.setOperator("==");
                                boolExp.setLeft(boolExpTemp.substring(0,indexEqual-1));
                                boolExp.setRight(boolExpTemp.substring(indexEqual+3));
                            }else{
                                boolExp.setLeft(boolExpTemp);
                                boolExp.setRight("");
                                boolExp.setOperator("");
                            }
                        }
                        assertionTemp.getDependedVar().add(boolExp.getLeft());
                        assertionboolExpArrayList.add(boolExp);
                    }
                    String instructTemp = temp.substring(indexthen+5);
                    int indexassignment = instructTemp.indexOf(":=");
                    assertionTemp.setBoolExpArrayList(assertionboolExpArrayList);
                    Instruct instructEnd = new Instruct();
                    instructEnd.setVariableReference(instructTemp.substring(0,indexassignment-1));
                    instructEnd.setExpression(instructTemp.substring(indexassignment+3));
                    instructArrayList.add(instructEnd);
                    assertionTemp.setInstructArrayList(instructArrayList);
                    assertionTemp.getDependVar().add(instructEnd.getVariableReference());
                    if(!instructEnd.getExpression().equalsIgnoreCase("TRUE")){
                        assertionTemp.getDependedVar().add(instructEnd.getExpression());
                    }
                }
                else{
                    int indexassignment = temp.indexOf(":=");
                    assertionTemp.setBoolExpArrayList(null);
                    assertionTemp.setIfExist(false);
                    Instruct instructEnd = new Instruct();
                    instructEnd.setVariableReference(temp.substring(0,indexassignment-1));
                    instructEnd.setExpression(temp.substring(indexassignment+3));
                    instructArrayList.add(instructEnd);
                    assertionTemp.setInstructArrayList(instructArrayList);
                    assertionTemp.getDependVar().add(instructEnd.getVariableReference());
                    assertionTemp.getDependedVar().add(instructEnd.getExpression());
                }
                assertionArrayList.add(assertionTemp);
            }
        }
        br.close();
        gts.setVars(vars);
        gts.setEventArrayList(eventArrayList);
        gts.setTransitionArrayList(transitionArrayList);
        gts.setAssertionArrayList(assertionArrayList);
        return gts;
    }
}
