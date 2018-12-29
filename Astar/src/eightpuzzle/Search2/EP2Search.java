package eightpuzzle.Search2;

import astar.Astar;
import astar.SimpleState;
import eightpuzzle.EPNode;
import eightpuzzle.EPState;

import java.util.ArrayList;

public class EP2Search extends Astar<EPNode> {

    @Override
    public int h(EPNode node) {
        int[] puzzles = node.getState().getPuzzles();
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            list.add(puzzles[i]);
        }
        int[] origin = new int[]{1, 2, 3, 8, 0, 4, 7, 6, 5};
        int hvalue = 0;
        for(int i = 0; i < 9; i++) {
            if(origin[i] == 0) continue;
            int index = list.indexOf(origin[i]);
            int drow = index / 3 - i / 3;
            int dcol = index % 3 - i % 3;
            if(origin[i] != puzzles[i]) hvalue += Math.abs(drow) + Math.abs(dcol);
        }
        return hvalue;
    }

    @Override
    public EPNode getNewNode(SimpleState state, int prev, int index) {
        return new EPNode((EPState) state, prev, index);
    }
}
