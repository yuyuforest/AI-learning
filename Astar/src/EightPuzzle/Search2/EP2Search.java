package EightPuzzle.Search2;

import Astar.Astar;
import Astar.SimpleState;
import EightPuzzle.EPNode;
import EightPuzzle.EPState;

import java.util.Arrays;

public class EP2Search extends Astar<EPNode> {

    @Override
    public int h(EPNode node) {
        return 0;
    }

    @Override
    public EPNode getNewNode(SimpleState state, int prev, int index) {
        return new EPNode((EPState) state, prev, index);
    }
}
