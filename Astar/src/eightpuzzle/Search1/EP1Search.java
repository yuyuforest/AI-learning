package eightpuzzle.Search1;

import astar.Astar;
import astar.SimpleState;
import eightpuzzle.EPNode;
import eightpuzzle.EPState;

public class EP1Search extends Astar<EPNode> {

    @Override
    public int h(EPNode node) {
        int[] puzzles = node.getState().getPuzzles();
        int[] origin = new int[]{1, 2, 3, 8, 0, 4, 7, 6, 5};
        int hvalue = 0;
        for(int i = 0; i < 9; i++) {
            if(origin[i] != puzzles[i] && puzzles[i] != 0) hvalue++;
        }
        return hvalue;
    }

    @Override
    public EPNode getNewNode(SimpleState state, int prev, int index) {
        return new EPNode((EPState) state, prev, index);
    }
}
