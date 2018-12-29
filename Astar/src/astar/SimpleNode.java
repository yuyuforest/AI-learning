package astar;

import java.util.ArrayList;

public abstract class SimpleNode<State extends SimpleState> implements Comparable<SimpleNode>{
    private State state;
    private int prev;
    private int index;
    private double fvalue;
    private double gvalue;
    private double hvalue;

    public SimpleNode(State state, int prev, int index) {
        this.state = state;
        this.prev = prev;
        this.index = index;
    }

    public String print() {
        return "index=" + index + " f=" + fvalue + " g=" + gvalue + " h=" + hvalue + "\n" + state.print();
    }

    public abstract ArrayList<State> next();

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public void setValue(double gvalue, double hvalue) {
        this.gvalue = gvalue;
        this.hvalue = hvalue;
        this.fvalue = gvalue + hvalue;
    }

    public State getState() {
        return state;
    }

    public int getPrev() {
        return prev;
    }

    public int getIndex() {
        return index;
    }

    public double getFvalue() {
        return fvalue;
    }

    public double getGvalue() {
        return gvalue;
    }

    public double getHvalue() {
        return hvalue;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SimpleNode)) return false;
        SimpleNode sn = (SimpleNode<State>) obj;
        return state == sn.state;
    }

    @Override
    public int compareTo(SimpleNode o) {
        return fvalue == o.fvalue ? 0 : (fvalue > o.fvalue ? 1 : -1);
    }
}
