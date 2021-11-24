package com.nuaa.wl.process;


import java.util.*;

public class MyAdjGraphic {
    //如果两个节点之间没有边，权值为null
    static final String MAXWEIGHT = "null";
    //存放节点的集合
    List vertices = new ArrayList();
    //邻接矩阵的二维数组
    String[][] edegs;
    //边的数量
    int numOfEdges;

    ArrayList<int[]> paths = new ArrayList<>();

    public MyAdjGraphic(int numOfEdges) {
        init(numOfEdges);
    }

    private void init(int numOfEdges) {
        this.edegs = new String[numOfEdges][numOfEdges];
        for (int i = 0; i < numOfEdges; i++) {
            for (int j = 0; j < numOfEdges; j++) {
                //对角线上的元素
                if (i == j) {
                    this.edegs[i][j] = "null";
                } else {
                    this.edegs[i][j] = MAXWEIGHT;
                }
            }
        }
        this.numOfEdges = 0;
    }

    //返回边的数量
    public int getNumOfEdges() {
        return this.numOfEdges;
    }

    //返回节点的数量
    public int getVertices() {
        return this.vertices.size();
    }

    //返回节点的值
    public Object getValueOfVertices(int index) {
        return this.vertices.get(index);
    }

    //获取某条边的权值
    public String getWeightOfVertices(int row, int col) {
        if ((row < 0 || row >= vertices.size()) || (col < 0 || col >= vertices.size())) {
            throw new IndexOutOfBoundsException("row或者col参数不合法");
        }
        return this.edegs[row][col];
    }

    //插入节点
    public void insertVertices(Object obj) {
        this.vertices.add(obj);
    }

    //插入带权值的边
    public void insertEdges(int row, int col, String weight) {


        if ((row < 0 || row >= vertices.size()) || (col < 0 || col >= vertices.size())) {
            throw new IndexOutOfBoundsException("row或者col参数不合法");
        }

            this.edegs[row][col] = weight;
            this.numOfEdges++;


    }

    //打印邻接矩阵
    public void print() {
        for (int i = 0; i < edegs.length; i++) {
            for (int j = 0; j < edegs[i].length; j++) {
                System.out.print(edegs[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //取下一个邻接节点
    public int getNextNeighbor(int row, int col) {
        if (col == -1) {//找到row的第一个邻接点
            for (int i = 0; i < this.vertices.size(); i++) {
                if (this.edegs[row][i] != "null") {//row节点有邻接点
                    if (!states.get(i))//假设这个点没有被访问过
                        return i;
                }
            }
        }
        for (int result = col + 1; result < this.vertices.size(); result++) {
            if (this.edegs[row][result] != "null") {
                return result;
            }
        }

        return -1;
    }

    //代表某节点是否在stack中,避免产生回路
    public Map<Integer, Boolean> states = new HashMap();


    public Stack<Integer> stack = new Stack();//存放放入stack中的节点
    public Stack<String> pathStack = new Stack();//存放某条路径的所有边


    public ArrayList<ArrayList<String >> getAllPaths(int start, int end, Paths endPaths) {
        ArrayList<String> path = new ArrayList<>();
        ArrayList<ArrayList<String>> pathSet = new ArrayList<>();
        //所有节点初始化为未访问
        for (int i = 0; i < vertices.size(); i++)
            states.put(i, false);

        //stack top元素
        int top_node;
        //存放当前top元素已经访问过的邻接点,若不存在则置-1,此时代表访问该top元素的第一个邻接点
        int adjvex_node = -1;
        int next_node;
        int next_arc;
        //start节点入栈
        stack.push(start);
        //pathStack.push("null");//暂时存放0
        states.put(start, true);

        while (!stack.isEmpty()) {
            top_node = stack.peek();
            if (top_node == end) {//该节点为end节点
                //输出路径

                String pathString = printPathNodes(stack);
                endPaths.paths.add(pathString);
                endPaths.count++;


                path = printPath(pathStack);
                pathSet.add(path);
                adjvex_node = stack.pop();
                if (!pathStack.isEmpty())
                    pathStack.pop();
                states.put(adjvex_node, false);//重新设置为未访问
            } else {//不是end节点
                next_node = getNextNeighbor(top_node, adjvex_node);
                if (next_node != -1) {//top_node存在除adjvex_node外的另一个相邻节点
                    stack.push(next_node);
                    pathStack.push(this.edegs[top_node][next_node]);//将top->next_node的边存放进去
                    states.put(next_node, true);
                    adjvex_node = -1;//临接点重置
                } else {//无其他邻接点
                    adjvex_node = stack.pop();
                    if (!pathStack.isEmpty())
                        pathStack.pop();
                    states.put(adjvex_node, false);
                }
            }
        }
        return pathSet;
    }

    public String printPathNodes(Stack<Integer> stack) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : stack) {
            sb.append(i + "->");
        }
        sb.delete(sb.length() - 2, sb.length());
        System.out.print(sb.toString());
        return sb.toString();
    }

    public ArrayList<String> printPath(Stack<String> stack) {
        if (stack.isEmpty())
            return null;
        StringBuilder sb = new StringBuilder();
        for (String i : stack) {
            sb.append(i + "&&");
        }
        sb.delete(sb.length() - 2, sb.length());
        ArrayList<String> path = new ArrayList<>();
        path.add(sb.toString());
//        System.out.print(sb.toString()+",");
        return path;
    }

}
