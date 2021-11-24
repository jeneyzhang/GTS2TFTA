package com.nuaa.wl.process;

import com.nuaa.wl.definition.GTS;
import com.nuaa.wl.definition.Next;
import com.nuaa.wl.definition.Vnode;
import com.nuaa.wl.definition.Weight;
import java.util.*;

public class ComGraph {
    public void processGraph(HashSet<GTS> gtsHashSet) {
        ArrayList<ArrayList<String>> path;

        for (GTS gtsTemp : gtsHashSet) {
            if (!gtsTemp.getGTSName().equals("independentAssertion")) {
                System.out.println(gtsTemp.getGTSName() + ":");
                ArrayList<Vnode> vnodes = gtsTemp.getGraph();
                ArrayList<Next> arcs = new ArrayList<>();
                for (Vnode vnode : vnodes) {
                    ArrayList<Next> nexts = processNext(vnode.getNexts());
                    vnode.setNexts(nexts);
                    arcs.addAll(nexts);
                }
//节点的数量
                int numOfVertices = vnodes.size();
//边的数量
                int numOfEdges = arcs.size();
                MyAdjGraphic myAdjGraphic = new MyAdjGraphic(numOfVertices);
//节点数组
                Object[] vertices = new Object[numOfVertices];
                for (int i = 0; i < numOfVertices; i++) {
                    vertices[i] = vnodes.get(i).getNodeName();
                }
//权值数组
                Weight[] weights = new Weight[numOfEdges];
                int count = 0;

                for (Next next : arcs) {
                    System.out.println(next);
                    String rowName = next.getOutNode();
                    String colName = next.getInNode();
                    String weight = next.getTransitionName();
                    int row = Integer.parseInt(rowName.substring(1));
                    int col = Integer.parseInt(colName.substring(1));
                    weights[count] = new Weight(row, col, weight);
                    count++;
                }
                Weight.createAdjGraphic(myAdjGraphic, vertices, weights, numOfVertices, numOfEdges);
                System.out.println("该邻接矩阵如下：");
                myAdjGraphic.print();
                System.out.println("邻接矩阵的节点数量为：" + myAdjGraphic.getVertices());
                System.out.println("邻接矩阵的边的数量为：" + myAdjGraphic.getNumOfEdges());
                try {
                    System.out.println("邻接矩阵的深度优先遍历算法结果是：");
                    HashMap<Integer, Paths> pathsHashMap = new HashMap<>();
                    for (int i = 0; i < vnodes.size(); i++) {
                        Vnode vnode = vnodes.get(i);

                        System.out.print("Ø" + vnode.getNodeName() + "=");
                        int num = Integer.parseInt(vnode.getNodeName().substring(1));
                        Paths endPaths = pathsHashMap.get(num);
                        if (endPaths == null) {
                            endPaths = new Paths(num);
                            pathsHashMap.put(num, endPaths);
                        }
                        path = myAdjGraphic.getAllPaths(0, num, endPaths);


                        System.out.println(path);
                        //System.out.println("YES");
                        //注释
                        // if(path.size()>1) {
                        String[] arr = new String[path.size()];
                        for (int y = 0; y < path.size(); y++) {
                            String ss = String.valueOf(path.get(y));
                            arr[y] = ss.substring(1, ss.length() - 1);
                        }
                        //System.out.print("arr is:");
                        //for(String str:arr){
                        //     System.out.println(str));
                        //   }
                        String[][] b = new String[arr.length][];

                        for (int t = 0; t < arr.length; t++) {
                            String[] second = arr[t].split("&&");
                            b[t] = new String[second.length];
                            for (int j = 0; j < second.length; j++) {
                                b[t][j] = second[j];
                            }
                        }
                        // for (int m = 0; m < path.size(); m++) {
                        //  for(int n=0;n<b[m].length;n++){
                        //    System.out.println("h is:" + b[m][n]);
                        //   }

                        //   }
                        String[] path11 = new String[path.size()];
                        for (int x = 0; x < path.size(); x++) {
                            String temp = b[x][0];
                            String tmp="";
                            int flag=0;
                            for (int j = 0; j < path.size(); j++) {

                                if (x == j) {
                                    continue;
                                }
                                if (x != j) {
                                    for (int k = 1; k < b[x].length; k++) {
                                        if(!b[x][0].equals(b[j][0])) {
                                            if (b[x][k].equals(b[j][0])) {
                                                if (!tmp.equals(b[j][0])) {
                                                    temp = temp + '<' + b[j][0];
                                                    flag=1;
                                                    tmp = b[j][0];
                                                    continue;
                                                }
                                            } else {
                                                String s = "";
                                                for (int a = 1; a < b[j].length; a++) {
                                                    s = s + b[j][a] + "&&";
                                                }
                                                if (String.valueOf(path.get(x)).contains(s.substring(0, s.length() - 2))) {
                                                    if (!tmp.equals(b[j][0])) {
                                                        temp = '(' + temp + '|' + b[j][0] + ')';
                                                        tmp = b[j][0];
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                                //else{
                                //    continue;
                                //  }
                            }
                            if(flag==0) {
                                for (int l = 1; l < b[x].length; l++) {
                                    temp = temp + "&&" + b[x][l];

                                }
                                path11[x] = temp;
                            }else{
                                path11[x]=temp;
                            }
                            //        }
                        }
                        if(path.size()>1){
                            ArrayList<ArrayList<String>> path22=new ArrayList<>();
                            for(int g=0;g<path11.length;g++){
                                ArrayList<String> pathOne = new ArrayList<>();
                                pathOne.add(path11[g]);
                                path22.add(pathOne);
                            }
                            path=path22;
                     //  ArrayList<ArrayList<String>> pathOne = new ArrayList<String>(Arrays.asList(path11));
                      //  path=pathOne;
                       // System.out.println("pathone is:" + pathOne);
                        //ArrayList<ArrayList<String>> path2=new ArrayList<>();
                    }
                        System.out.println("path is"+path);
                        vnode.setPathSet(path);
                    //    ArrayList<String> p=new ArrayList<>();  //注释
                    //    ArrayList<ArrayList<String>> path1 = new ArrayList<>();
                    //    for (int j = 0; j < endPaths.count; j++) {
                    //        String conPath = getConPath(endPaths, myAdjGraphic, j);
                            //p.add(conPath);


                          //  if(conPath!=null && conPath.contains("|")){
                          //      conPath.replaceAll("|","<");
                                //System.out.println("yes");
                          //  }
                         //   p.add(conPath);
                          //  System.out.println(conPath);
                      //  }
                     //   path1.add(p);
                      //  System.out.println(path1);
                     //   path=path1;
                     //   vnode.setPathSet(path);
                    }//for_Vnodes

                    System.out.println("");


                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println();
            }
            System.out.println();
        }//for (GTS gtsTemp : gtsHashSet)
    }

  /*  private String getConPath(Paths path, MyAdjGraphic graph, int num) {
        if (path.count <= 1) {
            return getPath(path.paths.get(0), graph);
        }
         String pathStr = getPath(path.paths.get(num), graph);
       // String pathStr = "p1&&ccf";      //做測試用

        for (int i = 0; i < path.count; i++) {
            //不与自己做合取
            if (i == num)
                break;

            String currentPath = getPath(path.paths.get(0), graph);
         //   String currentPath = "cf&&p1";     //做測試用
            String[] events = currentPath.split("&&");
            String[] pathEvents = pathStr.split("&&");
            //初度事件不能相同
            if (events[0].equals(pathEvents[0]))
                break;
            for (String event : events) {
                //如果沒有直接跳出
                if (!pathStr.contains(event))
                    break;


                for (int j = 0; j < pathEvents.length - 1; j++) {
                    if (pathEvents[j + 1].equals(event)) {
                        pathEvents[j] = "(" + pathEvents[j] + "|" + events[0] + ")";
                    }
                }
                if (pathEvents[0].equals(event)) {
                    pathEvents[0] = "(" + pathEvents[0] + "<" + events[0] + ")";
                }
                StringBuilder sb = new StringBuilder();
                for (String pathEvent : pathEvents) {
                    sb.append(pathEvent + "&&");
                }
                sb.delete(sb.length() - 2, sb.length());
                pathStr = sb.toString();
            }


        }
        return pathStr;


    }*/

    private String getPath(String s, MyAdjGraphic graph) {

        StringBuilder sb = new StringBuilder();
        String[] nodes = s.split("->");
        if (nodes.length == 1)
            return null;
        for (int i = 0; i < nodes.length - 1; i++) {
            int strat = Integer.parseInt(nodes[i]);
            int end = Integer.parseInt(nodes[i + 1]);
            String str = graph.getWeightOfVertices(strat, end);
            sb.append(str + "&&");

        }
        sb.delete(sb.length() - 2, sb.length());

        return sb.toString();

    }


    public ArrayList<Next> processNext(ArrayList<Next> arcs) {
        HashMap<String, Next> hashMaps = new HashMap<>();
        ArrayList<Next> nexts = new ArrayList<>();
        for (Next next : arcs) {
            String out = next.getOutNode();
            String in = next.getInNode();
            String name = out + "---" + in;
            String transitionName = next.getTransitionName();
            if (hashMaps.get(name) != null) {
                String s = "(" + hashMaps.get(name).getTransitionName() + "||" + transitionName + ")";
                hashMaps.get(name).setTransitionName(s);
            } else {
                hashMaps.put(name, next);
            }
        }
        Iterator iter = hashMaps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object val = entry.getValue();
            nexts.add((Next) val);
        }
        return nexts;
    }
}

