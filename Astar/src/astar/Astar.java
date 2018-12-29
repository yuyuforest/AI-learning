package astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class Astar<Node extends SimpleNode> {
    private ArrayList<Node> open;
    private ArrayList<Node> close;
    private HashMap<Integer, ArrayList<Integer>> tree = new HashMap<>();
    private ArrayList<Node> begin;
    private ArrayList<Node> end;
    private ArrayList<Node> graph;
    private Node endNode;
    private boolean finish = false;

    public void init(ArrayList<Node> begin, ArrayList<Node> end) {
        for(Node node : begin) {
            node.setValue(g(node), h(node));
        }
        Collections.sort(begin);
        this.begin = begin;
        this.open = new ArrayList<>();
        this.graph = new ArrayList<>();
        for (Node n: begin) {
            this.open.add(n);
            this.graph.add(n);
        }
        this.end = end;
        this.close = new ArrayList<>();
    }

    public int f(Node node) {
        return g(node) + h(node);
    }

    public int g(Node node) {
        ArrayList<Node> list = getGraph();
        int gvalue = 0;
        Node ptr = node;
        while(ptr.getIndex() != 0) {
            ptr = list.get(ptr.getPrev());
            gvalue++;
        }
        return gvalue;
    }

    public abstract int h(Node node);

    public String search() throws Exception{
        if(open.size() == 0) {
            throw new Exception("fail");
        }
        String str = "";
        str += "open表的结点数=" + open.size() + " 图的结点数=" + graph.size() + "\n\n";
        str += "choose node with min f value\n";
        Node node = open.remove(0);
        close.add(node);
        str += node.print();

        if(end.indexOf(node) != -1) {
            endNode = node;
            finish = true;

            return str;
        }

        ArrayList<SimpleState> states = node.next();
        for(int i = 0; i < states.size(); i++) {
            if(states.get(i) == node.getState()) continue;

            Node newNode = getNewNode(states.get(i), node.getIndex(), graph.size());
            newNode.setValue(g(newNode), h(newNode));

            int index1 = graph.indexOf(newNode);
            if(index1 == -1) {  // 不在graph里
                graph.add(newNode);
                insertOpen(newNode);
                str += "add new\n";
                if(end.indexOf(newNode) != -1) {
                    endNode = newNode;
                    finish = true;
                    str += newNode.print();
                    break;
                }
            }
            else {
                str += "find old and update\n";
                updateNode(newNode);
                newNode = graph.get(index1);
            }
            str += newNode.print();
        }
        return str;
    }

    public boolean isFinish() {
        return finish;
    }

    public ArrayList<Node> getGraph() {
        return graph;
    }

    public abstract Node getNewNode(SimpleState state, int prev, int index);

    public ArrayList<Node> getBestPath() {
        Node node = endNode;
        ArrayList<Node> bestPath = new ArrayList<>();
        while(true) {
            bestPath.add(0, node);
            if(node.getPrev() == -1) break;
            node = graph.get(node.getPrev());
        }
        return bestPath;
    }

    private void insertOpen(Node node) {
        if(open.isEmpty() || open.get(open.size() - 1).compareTo(node) <= 0) open.add(node);
        else for(int j = 0; j < open.size(); j++) {
            if(open.get(j).compareTo(node) > 0) {
                open.add(j, node);
                break;
            }
        }

        tree.put(node.getIndex(), new ArrayList<>());
        if(!tree.containsKey(node.getPrev())) {
            tree.put(node.getPrev(), new ArrayList<>());
        }
        tree.get(node.getPrev()).add(node.getIndex());
    }

    private void updateNode(Node node) {
        int openIndex = open.indexOf(node);
        Node origin;
        if(openIndex != -1) {   // node在open表里，尚未生成后继
            origin = open.get(openIndex);
        } else {                // node在close表里，已生成后继
            int closeIndex = close.indexOf(node);
            origin = close.get(closeIndex);
        }

        if(node.compareTo(origin) < 0) {    // 若这个结点有比原本更小的f值，更新这个结点
            if(origin.getPrev() >= 0) {
                tree.get(origin.getPrev()).remove(new Integer(origin.getIndex()));
                origin.setPrev(node.getPrev());
                tree.get(origin.getPrev()).add(origin.getIndex());
            }
            origin.setValue(node.getGvalue(), node.getHvalue());
        } else {
            return;
        }
        if(openIndex == -1) {   // node在close表里，已生成后继，后继结点也需要更新
            ArrayList<Integer> list = tree.get(origin.getIndex());
            for(int i = 0; i < list.size(); i++) {
                updateNode(graph.get(i));
            }
        }
    }

    public int getOpenSize() {
        return open.size();
    }

    public int getGraphSize() {
        return graph.size();
    }
}
