package eightpuzzle;

import astar.SimpleNode;

import java.util.ArrayList;

public class EPNode extends SimpleNode<EPState> {

    public EPNode(EPState state, int prev, int index) {
        super(state, prev, index);
    }

    @Override
    public boolean equals(Object obj) {     // 状态相同的结点视为相同结点
        if(!(obj instanceof EPNode)) return false;
        EPNode eps = (EPNode) obj;
        return this.getState().equals(eps.getState());
    }

    @Override
    public ArrayList<EPState> next() {  // 获取后继状态
        return getState().next();
    }
}
