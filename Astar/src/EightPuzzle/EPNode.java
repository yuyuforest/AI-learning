package EightPuzzle;

import Astar.SimpleNode;

import java.util.ArrayList;

public class EPNode extends SimpleNode<EPState> {

    public EPNode(EPState state, int prev, int index) {
        super(state, prev, index);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EPNode)) return false;
        EPNode eps = (EPNode) obj;
        return this.getState().equals(eps.getState());
    }

    @Override
    public ArrayList<EPState> next() {
        return getState().next();
    }
}
