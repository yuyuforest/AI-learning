package eightpuzzle;

import astar.Astar;
import eightpuzzle.Search1.EP1Search;
import eightpuzzle.Search2.EP2Search;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class EPController {
    @FXML
    private TextArea searchProcess1, searchProcess2, bestPath1, bestPath2;
    @FXML
    private Text open1, open2, graph1, graph2;
    @FXML
    private Label pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17, pos18,
            pos20, pos21, pos22, pos23, pos24, pos25, pos26, pos27, pos28;

    private Label[] puzzles1 = {pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17, pos18};
    private Label[] puzzles2 = {pos20, pos21, pos22, pos23, pos24, pos25, pos26, pos27, pos28};

    private Astar<EPNode> h1search, h2search;
    private ArrayList<EPNode> bestlist;
    private static final int[] endstate = {1, 2, 3, 8, 0, 4, 7, 6, 5};
    private Boolean[] showing = {false, false};
    private Boolean[] searching = {false, false};
    private String[] process = {"", ""};

    public void init() {
        puzzles1 = new Label[]{pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17, pos18};
        puzzles2 = new Label[]{pos20, pos21, pos22, pos23, pos24, pos25, pos26, pos27, pos28};

        int[] beginstate = initPuzzles();
        EPNode node = new EPNode(new EPState(beginstate), -1, 0);
        ArrayList<EPNode> beginlist1 = new ArrayList<>();
        beginlist1.add(node);
        ArrayList<EPNode> beginlist2 = new ArrayList<>();
        beginlist2.add(node);

        EPState end = new EPState(endstate);
        EPNode node2 = new EPNode(end, -1, 0);
        ArrayList<EPNode> endlist1 = new ArrayList<>();
        endlist1.add(node2);
        ArrayList<EPNode> endlist2 = new ArrayList<>();
        endlist2.add(node2);

        h1search = new EP1Search();
        h2search = new EP2Search();
        h1search.init(beginlist1, endlist1);
        h2search.init(beginlist2, endlist2);

        int[] puzzles = beginstate;
        for(int i = 0; i < 9; i++) {
            if(puzzles1[i].getText() == "") {
                puzzles1[i].getStyleClass().remove("label-space");
                puzzles2[i].getStyleClass().remove("label-space");
            }
            String str = puzzles[i] == 0 ? "" : Integer.toString(puzzles[i]);
            puzzles1[i].setText(str);
            puzzles2[i].setText(str);
            if(puzzles[i] == 0) {
                puzzles1[i].getStyleClass().add("label-space");
                puzzles2[i].getStyleClass().add("label-space");
            }
        }

        searchProcess1.setText("");
        searchProcess2.setText("");
        bestPath1.setText("");
        bestPath2.setText("");

        open1.setText("open表的结点数：0");
        open2.setText("open表的结点数：0");
        graph1.setText("图的结点数：0");
        graph2.setText("图的结点数：0");
    }


    private int[] initPuzzles() {

        int steps = 60;
        EPState state = new EPState(endstate);
        for(int i = 0; i < steps; i++) {
            ArrayList<EPState> next = state.next();
            state = next.get(new Random().nextInt(next.size()));
        }

        if(Arrays.equals(state.getPuzzles(), endstate)) return new int[]{0, 1, 3, 8, 5, 2, 7, 6, 4};
        return state.getPuzzles();
    }

    public void onInitClick() {
        if(searching[0] || searching[1] || showing[0] || showing[1]) return;
        init();
    }

    public void onSearch1Click() {
        if(!searching[0] && !h1search.isFinish()) {
            searching[0] = true;
            beginSearch(h1search, searchProcess1, bestPath1, open1, graph1, 0);
        }
    }

    public void onSearch2Click() {
        if(!searching[1] && !h2search.isFinish()) {
            searching[1] = true;
            beginSearch(h2search, searchProcess2, bestPath2, open2, graph2, 1);
        }
    }

    public void onBest1Click() {
        if(!showing[0]) {
            showing[0] = true;
            showBestPath(h1search, bestPath1, puzzles1, 0);
        }
    }

    public void onBest2Click() {
        if(!showing[1]) {
            showing[1] = true;
            showBestPath(h2search, bestPath2, puzzles2, 1);
        }
    }

    public void beginSearch(Astar<EPNode> search, TextArea searchProcess, TextArea bestPath, Text open, Text graph, int index) {
        if(search.isFinish()) return;
        Thread thread = new Thread(() -> {
            try{
                process[index] = "";
                while(true) {
                    String str;
                    str = search.search();
                    process[index] += str;
                    process[index] += "======================\n";
                    if(search.isFinish()) {
                        bestlist = search.getBestPath();
                        bestlist.remove(0);
                        Platform.runLater(()->{
                            searchProcess.setText(process[index] + "搜索结束");
                            bestPath.setText("最优路径的长度为："+ bestlist.size() + "\n\n");
                        });
                        searching[index] = false;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        Thread thread2 = new Thread(() -> {
           try{
               do{
                   Thread.sleep(100);
                   Platform.runLater(() -> {
                       open.setText("open表的结点数：" + search.getOpenSize());
                       graph.setText("图的结点数：" + search.getGraphSize());
                   });
               } while(!search.isFinish());
           } catch (Exception e) {
               e.printStackTrace();
           }
        });
        thread2.start();
    }

    private void showBestPath(Astar<EPNode> search, TextArea bestPath, Label[] puzzles, int index) {
        if(!search.isFinish()) {
            bestPath.setText("搜索未结束");
            showing[index] = false;
            return;
        }

        Thread thread = new Thread(() -> {
            try{
                for(EPNode node : bestlist) {
                    int[] result = node.getState().getPuzzles();
                    if(node.getFvalue() > bestlist.size()) {
                        throw new Exception("f(n) > f*(s0)");
                    }
                    Platform.runLater(()->{
                        bestPath.setText(bestPath.getText() + node.print());
                        for(int i = 0; i < 9; i++) {
                            String str = result[i] == 0 ? "" : Integer.toString(result[i]);
                            puzzles[i].setText(str);
                            puzzles[i].setText(str);
                            puzzles[i].getStyleClass().remove("label-space");
                            if(result[i] == 0) {
                                puzzles[i].getStyleClass().add("label-space");
                            }
                        }

                    });
                    Thread.sleep(300);
                }
                showing[index] = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
