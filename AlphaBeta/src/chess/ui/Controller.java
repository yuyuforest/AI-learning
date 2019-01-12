package chess.ui;

import alphabeta.BaseSearch;
import chess.Utils;
import chess.calculate.ChessCalculate;
import chess.calculate.ChessNode;
import chess.calculate.SimplePiece;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static chess.Utils.CELL;
import static chess.Utils.MARGIN;

public class Controller {
    private Group root;

    private ChessNode current;
    private Piece blackFrom;
    private Piece blackTo;
    private Piece redSelected;
    private ArrayList<SimplePiece> redArrivable;
    private boolean isPlayer;

    private Text computer;
    private Text human;
    private Text win;


    public Controller(@NotNull Group root) {
        this.root = root;

        drawBoard();
        drawTips();

        Piece[] redPieces = new Piece[16];
        Piece[] blackPieces = new Piece[16];
        Piece[][] piecesAt = new Piece[9][10];
        drawPieces(redPieces, blackPieces, piecesAt);
        current = new ChessNode(redPieces, blackPieces, piecesAt, true);

        redSelected = null;
        redArrivable = new ArrayList<>();

        isPlayer = true;

        System.out.println("棋局已初始化！");
    }

    public void onClick(Piece piece) {

        if(!isPlayer) return;

        int color = piece.getColor();
        if(color == Utils.COLOR_RED) {
            selectRed(piece);
        }
        else if(redArrivable.indexOf(piece) != -1) {    // 玩家移动棋子到可以着棋的地方
            humanTurn(piece);
            computerTurn();     // 人机对弈
        }
    }

    private void humanTurn(Piece piece) {
        System.out.println("human from " + redSelected);
        System.out.println("human to   " + piece);
        System.out.println();
        move(redSelected, piece);

        deselectRed();
        human.setVisible(false);
        isPlayer = false;
    }

    private void computerTurn() {   // 轮到电脑下棋
        human.setVisible(false);

        Thread thread = new Thread(()->{

            // 判断是否结束
            int result = ChessCalculate.getResult(current);
            if(result != Utils.WINNER_NONE) {
                Platform.runLater(()->{
                    String str = result == Utils.WINNER_RED ? "你赢了！" : "你输了！";
                    win.setText(str);
                    win.setVisible(true);
                    unmarkBlack();
                });
                return;
            }

            // 开始计算下一步
            Platform.runLater(()->{
                computer.setVisible(true);
            });
            ChessNode best = (ChessNode) new BaseSearch().search(current);  // 计算结果
            Platform.runLater(()->{
                System.out.println("computer from " + best.getFrom());
                System.out.println("computer to   " + best.getTo());
                System.out.println();

                current.move(best.getFrom(), best.getTo());     // 电脑走棋

                unmarkBlack();
                remarkBlack(best.getFrom(), best.getTo());
                computer.setVisible(false);

                Thread thread1 = new Thread(() -> {

                    // 判断是否结束
                    int result2 = ChessCalculate.getResult(current);
                    if(result2 != Utils.WINNER_NONE) {
                        Platform.runLater(()->{
                            String str = result2 == Utils.WINNER_RED ? "你赢了！" : "你输了！";
                            win.setText(str);
                            win.setVisible(true);
                            unmarkBlack();
                        });
                        return;
                    }

                    // 轮到玩家下棋
                    isPlayer = true;
                    Platform.runLater(()->{
                        human.setVisible(true);
                    });
                });
                thread1.start();
            });
        });

        thread.start();
    }

    private void move(Piece from, Piece to) {
        current.move(from, to);
    }

    private void selectRed(Piece piece) {
        if(redSelected != null) {
            redSelected.cancelRemark();
            for (SimplePiece i: redArrivable) {
                ((Piece)i).cancelRemark();
            }
        }

        SimplePiece[][] piecesAt = current.getPieces();

        redSelected = piece;
        piece.setRemark(true);
        redArrivable = Utils.getArrivable(piecesAt, redSelected);
        for (SimplePiece sp: redArrivable) {
            ((Piece)piecesAt[sp.getCol() - 1][sp.getRow() - 1]).setRemark(false);
        }
    }

    private void deselectRed() {
        redSelected.cancelRemark();
        for (SimplePiece i: redArrivable) {
            ((Piece)i).cancelRemark();
        }
        redSelected = null;
        redArrivable.clear();
    }

    private void remarkBlack(SimplePiece blackFrom, SimplePiece blackTo) {
        SimplePiece[][] piecesAt = current.getPieces();
        this.blackFrom = (Piece) piecesAt[blackFrom.getCol() - 1][blackFrom.getRow() - 1];
        this.blackTo = (Piece) piecesAt[blackTo.getCol() - 1][blackTo.getRow() - 1];
        this.blackFrom.setRemark(true);
        this.blackTo.setRemark(true);
    }

    private void unmarkBlack() {
        if(blackFrom == null) return;
        blackFrom.cancelRemark();
        blackTo.cancelRemark();
        blackFrom = null;
        blackTo = null;
    }

    private void drawPieces(Piece[] redPieces, Piece[] blackPieces, Piece[][] piecesAt) {
        for(int i = 0; i < Utils.INITIAL_BLACK_BOARD.length; i++) {
            int[] info = Utils.INITIAL_BLACK_BOARD[i];
            int col = info[0];
            int row = info[1];
            int type = info[2];
            int color = Utils.COLOR_BLACK;

            Piece piece = new Piece(root, col, row, type, color, Utils.PIECE_NAMES[type][color]);
            piecesAt[col - 1][row - 1] = piece;
            blackPieces[i] = piece;
        }
        for(int i = 0; i < Utils.INITIAL_RED_BOARD.length; i++) {
            int[] info = Utils.INITIAL_RED_BOARD[i];
            int col = info[0];
            int row = info[1];
            int type = info[2];
            int color = Utils.COLOR_RED;

            Piece piece = new Piece(root, col, row, type, color, Utils.PIECE_NAMES[type][color]);
            piecesAt[col - 1][row - 1] = piece;
            redPieces[i] = piece;
        }
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 10; j++) {
                if(piecesAt[i][j] == null) {
                    Piece piece = new Piece(root, i + 1, j + 1);
                    piecesAt[i][j] = piece;
                }
            }
        }
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 10; j++) {
                Piece piece = piecesAt[i][j];
                piece.setOnMouseClicked(event -> {
                    onClick(piece);
                });
            }
        }
    }

    private void drawBoard() {
        Line line;

        // 竖线
        for(int i = 2; i <= 8; i++) {
            line = new Line();
            line.setStartX(i * CELL);
            line.setEndX(line.getStartX());
            line.setStartY(CELL);
            line.setEndY(5 * CELL);
            root.getChildren().add(line);

            line = new Line();
            line.setStartX(i * CELL);
            line.setEndX(line.getStartX());
            line.setStartY(6 * CELL);
            line.setEndY(10 * CELL);
            root.getChildren().add(line);
        }
        for(int i = 1; i <= 9; i += 8) {
            line = new Line();
            line.setStartX(i * CELL);
            line.setEndX(line.getStartX());
            line.setStartY(CELL);
            line.setEndY(10 * CELL);
            line.setStrokeWidth(3);
            root.getChildren().add(line);
        }
        line = new Line();
        line.setStartX(CELL - MARGIN);
        line.setEndX(line.getStartX());
        line.setStartY(CELL - MARGIN);
        line.setEndY(10 * CELL + MARGIN);
        line.setStrokeWidth(5);
        root.getChildren().add(line);

        line = new Line();
        line.setStartX(9 * CELL + MARGIN);
        line.setEndX(line.getStartX());
        line.setStartY(CELL - MARGIN);
        line.setEndY(10 * CELL + MARGIN);
        line.setStrokeWidth(5);
        root.getChildren().add(line);

        // 横线
        for(int i = 1; i <= 10; i++) {
            line = new Line();
            line.setStartX(CELL);
            line.setEndX(9 * CELL);
            line.setStartY(i * CELL);
            line.setEndY(line.getStartY());
            root.getChildren().add(line);
            if(i % 5 == 1 || i % 5 == 0) {
                line.setStrokeWidth(3);
            }
        }
        line = new Line();
        line.setStartX(CELL - MARGIN);
        line.setEndX(9 * CELL + MARGIN);
        line.setStartY(CELL - MARGIN);
        line.setEndY(line.getStartY());
        line.setStrokeWidth(5);
        root.getChildren().add(line);

        line = new Line();
        line.setStartX(CELL - MARGIN);
        line.setEndX(9 * CELL + MARGIN);
        line.setStartY(10 * CELL + MARGIN);
        line.setEndY(line.getStartY());
        line.setStrokeWidth(5);
        root.getChildren().add(line);

        // 斜线
        line = new Line(4 * CELL, CELL, 6 * CELL, 3 * CELL);
        root.getChildren().add(line);
        line = new Line(4 * CELL, 3 * CELL, 6 * CELL, CELL);
        root.getChildren().add(line);
        line = new Line(4 * CELL, 8 * CELL, 6 * CELL, 10 * CELL);
        root.getChildren().add(line);
        line = new Line(4 * CELL, 10 * CELL, 6 * CELL, 8 * CELL);
        root.getChildren().add(line);
    }

    private void drawTips() {
        computer = new Text(10 * CELL, 3 * CELL, "电脑走步中…");
        human = new Text(10 * CELL, 8.5 * CELL, "请走步！");
        win = new Text(10 * CELL, 6 * CELL, "");
        Text restart = new Text(14 * CELL, 5 * CELL, "重\n新\n开\n始");

        computer.setFont(Font.font("KaiTi", CELL * 0.5));
        computer.setVisible(false);
        human.setFont(Font.font("KaiTi", CELL * 0.5));
        win.setFont(Font.font("KaiTi", CELL * 0.5));
        win.setVisible(false);
        restart.setFont(Font.font("KaiTi", CELL * 0.5));
        restart.setOnMouseClicked((event -> {
            Scene scene = root.getScene();
            root = new Group();
            scene.setRoot(root);
            new Controller(root);
        }));

        root.getChildren().add(computer);
        root.getChildren().add(human);
        root.getChildren().add(win);
        root.getChildren().add(restart);
    }
}
