package chess.calculate;

import alphabeta.BaseNode;
import chess.ui.Piece;
import chess.Utils;

import java.util.ArrayList;

public class ChessNode implements BaseNode {    // 棋局的状态结点
    private SimplePiece[] redPieces;
    private SimplePiece[] blackPieces;
    private SimplePiece[][] piecesAt;
    private boolean isPlayer;               // 当前下棋方，true-玩家，false-程序
    private int value;                      // 棋局的评估值
    private SimplePiece lastFrom = null;
    private SimplePiece lastTo = null;      // 生成当前棋局的前一步棋子的移动

    public ChessNode(Piece[] redPieces, Piece[] blackPieces, Piece[][] piecesAt, boolean isPlayer) {
        this.redPieces = redPieces;
        this.blackPieces = blackPieces;
        this.piecesAt = piecesAt;
        this.isPlayer = isPlayer;

        value = ChessCalculate.calculateValue(this.redPieces, this.blackPieces, this.piecesAt, this.isPlayer);
    }

    public ChessNode(ChessNode parent, SimplePiece from, SimplePiece to) {  // from-to指示的棋子移动使parent生成现在的新棋局
        this.redPieces = new SimplePiece[parent.redPieces.length];
        this.blackPieces = new SimplePiece[parent.blackPieces.length];
        this.piecesAt = new SimplePiece[9][10];

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 10; j++) {
                this.piecesAt[i][j] = new SimplePiece(parent.piecesAt[i][j]);
            }
        }
        for(int i = 0; i < redPieces.length; i++) {
            int c = parent.redPieces[i].getCol();
            int r = parent.redPieces[i].getRow();
            this.redPieces[i] = piecesAt[c - 1][r - 1];
        }
        for(int i = 0; i < blackPieces.length; i++) {
            int c = parent.blackPieces[i].getCol();
            int r = parent.blackPieces[i].getRow();
            this.blackPieces[i] = piecesAt[c - 1][r - 1];
        }

        lastFrom = new SimplePiece(from);
        lastTo = new SimplePiece(to);

        this.isPlayer = parent.isPlayer;
        move(lastFrom, lastTo);

        value = ChessCalculate.calculateValue(this.redPieces, this.blackPieces, this.piecesAt, this.isPlayer);
    }

    // 把from位置的棋子移动到move位置
    public void move(SimplePiece from, SimplePiece to) {
        int oldc = from.getCol();
        int oldr = from.getRow();
        int newc = to.getCol();
        int newr = to.getRow();

        SimplePiece eater = piecesAt[oldc - 1][oldr - 1];
        SimplePiece food = piecesAt[newc - 1][newr - 1];
        piecesAt[newc - 1][newr - 1] = eater;
        piecesAt[oldc - 1][oldr - 1] = food;
        eater.moveTo(newc, newr);
        food.moveTo(oldc, oldr);
        food.eaten();

        isPlayer = !isPlayer;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public SimplePiece getFrom() {
        return lastFrom;
    }

    public SimplePiece getTo() {
        return lastTo;
    }

    public SimplePiece[][] getPieces() {
        return piecesAt;
    }

    public SimplePiece[] getRedPieces() {
        return redPieces;
    }

    public SimplePiece[] getBlackPieces() {
        return blackPieces;
    }

    @Override
    public ArrayList<BaseNode> getChildren() {      // 获取根据棋子的基本下法生成的所有可能的后继棋局
        SimplePiece[] pieces = isPlayer ? redPieces : blackPieces;
        ArrayList<BaseNode> children = new ArrayList<>();
        for(SimplePiece from : pieces) {
            if(from.getType() == Utils.TYPE_NONE) continue;
            ArrayList<SimplePiece> arrivableList = Utils.getArrivable(piecesAt, from);
            for (SimplePiece to: arrivableList) {
                ChessNode child = new ChessNode(this, from, to);
                children.add(child);
            }
        }
        return children;
    }

    @Override
    public boolean isMax() {
        return !isPlayer;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ChessNode: from (" + lastFrom + ") , to (" + lastTo + ") , value (" + value + ")\n";
    }
}
