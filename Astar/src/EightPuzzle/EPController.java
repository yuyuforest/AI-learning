package EightPuzzle;

import Astar.Astar;
import Main.Controller;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class EPController {
    public TextArea searchProcess;
    public TextArea bestPath;
    public Label pos0;
    public Label pos1;
    public Label pos2;
    public Label pos3;
    public Label pos4;
    public Label pos5;
    public Label pos6;
    public Label pos7;
    public Label pos8;

    private Astar<EPNode> epSearch;
    private ArrayList<EPNode> bestlist;
    private int index = 0;

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void init(int[] beginstate, int[] endstate, Astar<EPNode> search) {
        EPState begin = new EPState(beginstate);
        EPNode node = new EPNode(begin, -1, 0);
        ArrayList<EPNode> beginlist = new ArrayList<>();
        beginlist.add(node);

        EPState end = new EPState(endstate);
        EPNode node2 = new EPNode(end, -1, 0);
        ArrayList<EPNode> endlist = new ArrayList<>();
        endlist.add(node2);
        epSearch = search;
        epSearch.init(beginlist, endlist);

        index = 0;

        int[] puzzles = beginstate;
        pos0.setText(puzzles[0] == 0 ? " " : Integer.toString(puzzles[0]));
        pos1.setText(puzzles[1] == 0 ? " " : Integer.toString(puzzles[1]));
        pos2.setText(puzzles[2] == 0 ? " " : Integer.toString(puzzles[2]));
        pos3.setText(puzzles[3] == 0 ? " " : Integer.toString(puzzles[3]));
        pos4.setText(puzzles[4] == 0 ? " " : Integer.toString(puzzles[4]));
        pos5.setText(puzzles[5] == 0 ? " " : Integer.toString(puzzles[5]));
        pos6.setText(puzzles[6] == 0 ? " " : Integer.toString(puzzles[6]));
        pos7.setText(puzzles[7] == 0 ? " " : Integer.toString(puzzles[7]));
        pos8.setText(puzzles[8] == 0 ? " " : Integer.toString(puzzles[8]));

        searchProcess.setText("");
        bestPath.setText("");
    }

    public void onInitClick() {
        if(!epSearch.isFinish()) return;
        controller.init();
    }


    public void onSearchClick() throws Exception {
        if(epSearch.isFinish()) return;
       while(true) {
           String str = epSearch.search();
           searchProcess.setText(searchProcess.getText() + str + "======================\n");
           searchProcess.setScrollTop(searchProcess.getScrollTop() + searchProcess.getHeight());
           if(epSearch.isFinish()) {
               bestlist = epSearch.getBestPath();
               bestlist.remove(0);
               searchProcess.setText(searchProcess.getText() + "搜索结束");
               bestPath.setText("最优路径的长度为："+ bestlist.size() + "\n\n");
               break;
           }
       }
    }

    public void onBestClick() {
        if(!epSearch.isFinish()) {
            bestPath.setText("搜索未结束");
            return;
        } else if(index == bestlist.size()){
            return;
        }
        EPNode node = bestlist.get(index++);
        bestPath.setText(bestPath.getText() + node.print());
        int[] puzzles = node.getState().getPuzzles();
        pos0.setText(puzzles[0] == 0 ? " " : Integer.toString(puzzles[0]));
        pos1.setText(puzzles[1] == 0 ? " " : Integer.toString(puzzles[1]));
        pos2.setText(puzzles[2] == 0 ? " " : Integer.toString(puzzles[2]));
        pos3.setText(puzzles[3] == 0 ? " " : Integer.toString(puzzles[3]));
        pos4.setText(puzzles[4] == 0 ? " " : Integer.toString(puzzles[4]));
        pos5.setText(puzzles[5] == 0 ? " " : Integer.toString(puzzles[5]));
        pos6.setText(puzzles[6] == 0 ? " " : Integer.toString(puzzles[6]));
        pos7.setText(puzzles[7] == 0 ? " " : Integer.toString(puzzles[7]));
        pos8.setText(puzzles[8] == 0 ? " " : Integer.toString(puzzles[8]));
    }
}
