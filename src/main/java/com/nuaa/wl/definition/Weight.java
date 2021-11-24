package com.nuaa.wl.definition;
import com.nuaa.wl.process.MyAdjGraphic;

public class Weight {
    int row; //横坐标
    int col; //纵坐标
    String weight; //权值

    public Weight(int row, int col, String weight) {
        this.row = row;
        this.col = col;
        this.weight = weight;
    }

    /**
     * @param myAdjGraphic  临界矩阵对象类
     * @param vertices      节点对象数组
     * @param weights       权值数组
     * @param numOfVertices 节点的数量
     * @param numOfEdges    边的数量
     * @Description: 创建矩阵
     * @Return: void

     * @CreateDate: 2018/7/14 14:35
     */
    public static void createAdjGraphic(MyAdjGraphic myAdjGraphic, Object vertices[],
                                        Weight weights[], int numOfVertices, int numOfEdges) {

        //初始化节点
        for (int i = 0; i < numOfVertices; i++) {
            myAdjGraphic.insertVertices(vertices[i]);
        }

        //初始化所有的边
        for (int j = 0; j < numOfEdges; j++) {
            myAdjGraphic.insertEdges(weights[j].row, weights[j].col, weights[j].weight);
        }
    }
}
