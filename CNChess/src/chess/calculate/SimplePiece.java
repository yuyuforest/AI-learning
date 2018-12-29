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

    public void eaten() {
        this.type = TYPE_NONE;
        this.color = COLOR_NONE;
    }

    public void moveTo(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public String toString() {
        return "Piece: col " + col + " row " + row + " color " + color + " type " + type + " " + (color == -1 ? "" : Utils.PIECE_NAMES[type][color]);
    }
}
