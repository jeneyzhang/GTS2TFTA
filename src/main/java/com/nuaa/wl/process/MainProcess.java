package com.nuaa.wl.process;

import com.nuaa.wl.definition.GTS;
import com.nuaa.wl.definition.Var;

import java.io.IOException;
import java.util.HashSet;

public class MainProcess {
    public static void main(String[] args) throws IOException {
         //String gtsFile = "IrrigationSystem.alt";
        String gtsFile = "IMAsysytem.alt";
       // String gtsFile = "IMA.alt";
        //String gtsFile = "GAR.alt";
        /**1.读取文件**/
        System.out.println("1.读取GTS文件");
        GTS gts = new ReadFile().readFile(gtsFile);
        System.out.println(gts);
        /**2.划分GTS**/
        HashSet<GTS> gtsHashSet = new PartitioningGTS().partitioningGTS(gts);
        System.out.println("2.划分好的GTS：");
        for(GTS gts1 : gtsHashSet){
            System.out.println(gts1);
        }
        /**3.获取可达性图**/
        System.out.println("3.获取可达性图");
        GetGraph getGraph = new GetGraph();
        getGraph.getGraph(gtsHashSet);

        /**4.编译可达性图为布尔公式**/
        System.out.println("4.编译可达性图为布尔公式");
        ComGraph comGraph = new ComGraph();
        comGraph.processGraph(gtsHashSet);

        /**5.编译独立断言为布尔公式**/
        System.out.println("4.编译独立断言为布尔公式");
        ComAssertion comAssertion = new ComAssertion();
        comAssertion.comAssertion(gtsHashSet);

        // 增加对时序条件的布尔表达式的过滤
        //System.out.println("5.过滤布尔公式“);
        //
        
    }
}
