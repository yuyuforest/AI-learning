package EightPuzzle;

import Astar.SimpleState;

import java.util.ArrayList;
import java.util.Arrays;

public class EPState implements SimpleState {
    private int[] puzzles;

    public EPState(int[] puzzles) {
        this.puzzles = Arrays.copyOf(puzzles, puzzles.length);
    }

    public EPState(EPState state) {
        puzzles = Arrays.copyOf(state.puzzles, state.puzzles.length);
    }

    @Override
    public String print() {
        return puzzles[0] + " " + puzzles[1] + " " + puzzles[2] + "\n"
                + puzzles[3] + " " + puzzles[4] + " " + puzzles[5] + "\n"
                + puzzles[6] + " " + puzzles[7] + " " + puzzles[8] + "\n\n";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EPState)) return false;
        EPState eps = (EPState) obj;
        return Arrays.equals(puzzles, eps.puzzles);
    }

    public int[] getPuzzles() {
        return puzzles;
    }

    public ArrayList<EPState> next() {
        ArrayList<EPState> nextStates = new ArrayList<>();
        int space = 0;
        for(int i = 0; i < puzzles.length; i++) {
            if(puzzles[i] == 0) {
                space = i;
                break;
            }
        }

        if(space % 3 == 0 || space % 3 == 1) {  // 右移
            EPState state = new EPState(this);
            state.puzzles[space] = state.puzzles[space + 1];
            state.puzzles[space + 1] = 0;
            nextStates.add(state);
        }
        if(space % 3 == 1 || space % 3 == 2) {   // 左移
            EPState state = new EPState(this);
            state.puzzles[space] = state.puzzles[space - 1];
            state.puzzles[space - 1] = 0;
            nextStates.add(state);
        }
        if(space > 2) {  // 上移
            EPState state = new EPState(this);
            state.puzzles[space] = state.puzzles[space - 3];
            state.puzzles[space - 3] = 0;
            nextStates.add(state);
        }
        if(space < 6) {    // 下移
            EPState state = new EPState(this);
            state.puzzles[space] = state.puzzles[space + 3];
            state.puzzles[space + 3] = 0;
            nextStates.add(state);
        }

        return nextStates;
    }
}
