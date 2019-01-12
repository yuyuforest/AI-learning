package chess.calculate;

import chess.Utils;

import static chess.Utils.COLOR_NONE;
import static chess.Utils.TYPE_NONE;

public class SimplePiece {
    private int type;
    private int color;
    private int col;
    private int row;

    public SimplePiece(int col, int row) {
        this.col = col;
        this.row = row;
        this.type = TYPE_NONE;
        this.color = COLOR_NONE;
    }

    public SimplePiece(int col, int row, int type, int color) {
        this.col = col;
        this.row = row;
        this.type = type;
        this.color = color;
    }

    public SimplePiece(SimplePiece other) {
        this.col = other.col;
        this.row = other.row;
        this.type = other.type;
        this.color = other.color;
    }

    public int getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // 棋子被吃掉，成为虚棋
    public void eaten() {
        this.type = TYPE_NONE;
        this.color = COLOR_NONE;
    }

    // 棋子移动
    public void moveTo(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public String toString() {
        String str1 = "Piece: col " + col + " row " + row;
        String str2 = "";
        if(color != -1) {
            str2 = " color " + (color == Utils.COLOR_RED ? "红" : "黑") + " type " + Utils.PIECE_NAMES[type][color];
        }
        return str1 + str2;
    }
}
