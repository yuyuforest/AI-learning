package Main;

import EightPuzzle.EPController;
import EightPuzzle.EPState;
import EightPuzzle.Search1.EP1Search;
import EightPuzzle.Search2.EP2Search;

import java.util.ArrayList;
import java.util.Random;

public class Controller {
    EPController ep1;
    EPController ep2;

    public Controller(EPController ep1, EPController ep2) {
        this.ep1 = ep1;
        this.ep2 = ep2;
    }

    public void init() {
        int[] endState = {1, 2, 3, 8, 0, 4, 7, 6, 5};

        //int steps = new Random().nextInt(10) + 1;
        int steps = 40;
        EPState state = new EPState(endState);
        for(int i = 0; i < steps; i++) {
            ArrayList<EPState> next = state.next();
            state = next.get(new Random().nextInt(next.size()));
        }

        int[] beginState = state.getPuzzles();

        ep1.init(beginState, endState, new EP1Search());
        ep2.init(beginState, endState, new EP2Search());
    }
}