package chess;

import chess.calculate.SimplePiece;

import java.util.ArrayList;

public class Utils {
    public static final int CELL = 90;
    public static final int MARGIN = 10;
    public static final int PIECE_RADIUS = 35;
    public static final String[][] PIECE_NAMES = {{"帥","将"},{"仕","士"},{"相","象"},{"馬","马"},
            {"俥","車"},{"炮","砲"},{"兵","卒"}};
    public static final int TYPE_NONE = -1;     // 虚棋
    public static final int TYPE_GENERAL = 0;   // 帅/将
    public static final int TYPE_ADVISOR = 1;   // 士
    public static final int TYPE_ELEPHANT = 2;  // 相/象
    public static final int TYPE_HORSE = 3;     // 马
    public static final int TYPE_CHARIOT = 4;   // 车
    public static final int TYPE_CANNON = 5;    // 炮
    public static final int TYPE_SOLDIER = 6;   // 兵/卒
    public static final int COLOR_NONE = -1;    // 虚棋
    public static final int COLOR_RED = 0;      // 红方棋子
    public static final int COLOR_BLACK = 1;    // 黑方棋子
    public static final int[][] INITIAL_BLACK_BOARD = {     // 初始的黑方棋盘
            {1, 1, TYPE_CHARIOT},
            {2, 1, TYPE_HORSE},
            {3, 1, TYPE_ELEPHANT},
            {4, 1, TYPE_ADVISOR},
            {5, 1, TYPE_GENERAL},
            {6, 1, TYPE_ADVISOR},
            {7, 1, TYPE_ELEPHANT},
            {8, 1, TYPE_HORSE},
            {9, 1, TYPE_CHARIOT},
            {2, 3, TYPE_CANNON},
            {8, 3, TYPE_CANNON},
            {1, 4, TYPE_SOLDIER},
            {3, 4, TYPE_SOLDIER},
            {5, 4, TYPE_SOLDIER},
            {7, 4, TYPE_SOLDIER},
            {9, 4, TYPE_SOLDIER}
    };
    public static final int[][] INITIAL_RED_BOARD = {     // 初始的红方棋盘
            {1, 10, TYPE_CHARIOT},
            {2, 10, TYPE_HORSE},
            {3, 10, TYPE_ELEPHANT},
            {4, 10, TYPE_ADVISOR},
            {5, 10, TYPE_GENERAL},
            {6, 10, TYPE_ADVISOR},
            {7, 10, TYPE_ELEPHANT},
            {8, 10, TYPE_HORSE},
            {9, 10, TYPE_CHARIOT},
            {2, 8, TYPE_CANNON},
            {8, 8, TYPE_CANNON},
            {1, 7, TYPE_SOLDIER},
            {3, 7, TYPE_SOLDIER},
            {5, 7, TYPE_SOLDIER},
            {7, 7, TYPE_SOLDIER},
            {9, 7, TYPE_SOLDIER}
    };
    public static final int WINNER_NONE = -1;   // 未决出胜负
    public static final int WINNER_RED = 0;     // 红方胜利
    public static final int WINNER_BLACK = 1;   // 黑方胜利

    // 计算对当前棋子piece，它下一步可以移动到的地方
    public static ArrayList<SimplePiece> getArrivable(SimplePiece[][] piecesAt, SimplePiece piece) {
        ArrayList<SimplePiece> list = new ArrayList<>();

        switch(piece.getType()) {
            case TYPE_GENERAL:  // 将不出营
            {
                int[] horizontal = {-1, 1, 0, 0};
                int[] vertical = {0, 0, -1, 1};
                int[] hbound = {4, 6};
                int[] vbound;
                if(piece.getColor() == COLOR_BLACK) {
                    vbound = new int[]{1, 3};
                } else {
                    vbound = new int[]{8, 10};
                }
                for(int i = 0; i < 4; i++) {
                    int newc = piece.getCol() + horizontal[i];
                    int newr = piece.getRow() + vertical[i];
                    if(newc < hbound[0] || newc > hbound[1] || newr < vbound[0] || newr > vbound[1]) continue;
                    SimplePiece next = piecesAt[newc - 1][newr - 1];
                    if(isObstacle(next, piece, true)) continue;
                    list.add(next);
                }
                break;
            }
            case TYPE_ADVISOR:  // 士走斜线护将边
            {
                int[][] points;
                if(piece.getColor() == COLOR_BLACK) {
                    points = new int[][]{{4, 1}, {4, 3}, {5, 2}, {6, 1}, {6, 3}};
                } else {
                    points = new int[][]{{4, 8}, {4, 10}, {5, 9}, {6, 8}, {6, 10}};
                }
                for(int i = 0; i < 5; i++) {
                    int newc = points[i][0];
                    int newr = points[i][1];
                    int delta = Math.abs(newc - piece.getCol());
                    if(delta != 1) continue;
                    SimplePiece next = piecesAt[newc - 1][newr - 1];
                    if(isObstacle(next, piece, true)) continue;
                    list.add(next);
                }
                break;
            }
            case TYPE_ELEPHANT: // 象走田
            {
                int[] horizontal = {-2, -2, 2, 2};
                int[] vertical = {-2, 2, -2, 2};
                int[] hbound = {1, 9};
                int[] vbound;
                if(piece.getColor() == COLOR_BLACK) {
                    vbound = new int[]{1, 5};
                } else {
                    vbound = new int[]{6, 10};
                }
                for(int i = 0; i < 4; i++) {
                    int newc = piece.getCol() + horizontal[i];
                    int newr = piece.getRow() + vertical[i];
                    if(newc < hbound[0] || newc > hbound[1] || newr < vbound[0] || newr > vbound[1]) continue;
                    SimplePiece next = piecesAt[newc - 1][newr - 1];
                    if(isObstacle(next, piece, true)) continue;
                    SimplePiece mid = piecesAt[piece.getCol() + horizontal[i] / 2 - 1][piece.getRow() + vertical[i] / 2 - 1];
                    if(isObstacle(mid, piece, false)) continue; // 中间不能有任何棋子
                    list.add(next);
                }
                break;
            }
            case TYPE_HORSE:    // 马走日
            {
                int[][] points = {
                        {0, -1, 1, -1},
                        {0, -1, -1, -1},
                        {0, 1, 1, 1},
                        {0, 1, -1, 1},
                        {1, 0, 1, -1},
                        {1, 0, 1, 1},
                        {-1, 0, -1, -1},
                        {-1, 0, -1, 1}};
                for(int i = 0; i < 8; i++) {
                    int midc = piece.getCol() + points[i][0];
                    int midr = piece.getRow() + points[i][1];
                    if(midc < 1 || midc > 9 || midr < 1 || midr > 10) continue;
                    SimplePiece mid = piecesAt[midc - 1][midr - 1];
                    if(isObstacle(mid, piece, false)) continue;
                    int newc = midc + points[i][2];
                    int newr = midr + points[i][3];
                    if(newc < 1 || newc > 9 || newr < 1 || newr > 10) continue;
                    SimplePiece next = piecesAt[newc - 1][newr - 1];
                    if(isObstacle(next, piece, true)) continue;
                    list.add(next);
                }
                break;
            }
            case TYPE_CHARIOT:  // 车走直路
            {
                int c = piece.getCol();
                int r = piece.getRow();
                for(int i = piece.getCol() - 1; i >= 1; i--) {
                    SimplePiece next = piecesAt[i - 1][r - 1];
                    if(!isObstacle(next, piece, false)) { // 棋子为空，则车可以到达它
                        list.add(next);
                    }
                    else{            // 棋子为不同阵营，则车可以到达它，之后不再搜索该方向
                        if(!isObstacle(next, piece, true)) list.add(next);
                        break;
                    }
                }
                for(int i = piece.getCol() + 1; i <= 9; i++) {
                    SimplePiece next = piecesAt[i - 1][r - 1];
                    if(!isObstacle(next, piece, false)) {
                        list.add(next);
                    }
                    else{
                        if(!isObstacle(next, piece, true)) list.add(next);
                        break;
                    }
                }
                for(int i = piece.getRow() - 1; i >= 1; i--) {
                    SimplePiece next = piecesAt[c - 1][i - 1];
                    if(!isObstacle(next, piece, false)) {
                        list.add(next);
                    }
                    else{
                        if(!isObstacle(next, piece, true)) list.add(next);
                        break;
                    }
                }
                for(int i = piece.getRow() + 1; i <= 10; i++) {
                    SimplePiece next = piecesAt[c - 1][i - 1];
                    if(!isObstacle(next, piece, false)) {
                        list.add(next);
                    }
                    else{
                        if(!isObstacle(next, piece, true)) list.add(next);
                        break;
                    }
                }
                break;
            }
            case TYPE_CANNON:   // 炮翻山
            {
                int c = piece.getCol();
                int r = piece.getRow();
                for(int i = piece.getCol() - 1; i >= 1; i--) {
                    SimplePiece next = piecesAt[i - 1][r - 1];
                    if(!isObstacle(next, piece, false)) list.add(next); // 棋子为空，则炮可以到达它
                    else {  // 棋子不为空，跳过它
                        for(i -= 1; i >= 1; i--) {
                            SimplePiece next2 = piecesAt[i - 1][r - 1];
                            if(isObstacle(next2, piece, false) && !isObstacle(next2, piece, true)) {
                                list.add(next2); // 棋子不为空，且棋子可吃，则炮可以到达它
                                break;
                            }
                        }
                        break;
                    }
                }
                for(int i = piece.getCol() + 1; i <= 9; i++) {
                    SimplePiece next = piecesAt[i - 1][r - 1];
                    if(!isObstacle(next, piece, false)) list.add(next);
                    else {  // 棋子不为空，跳过它
                        for(i += 1; i <= 9; i++) {
                            SimplePiece next2 = piecesAt[i - 1][r - 1];
                            if(isObstacle(next2, piece, false) && !isObstacle(next2, piece, true)) {
                                list.add(next2); // 棋子不为空，且棋子可吃，则炮可以到达它
                                break;
                            }
                        }
                        break;
                    }
                }
                for(int i = piece.getRow() - 1; i >= 1; i--) {
                    SimplePiece next = piecesAt[c - 1][i - 1];
                    if(!isObstacle(next, piece, false)) list.add(next);
                    else {  // 棋子不为空，跳过它
                        for(i -= 1; i >= 1; i--) {
                            SimplePiece next2 = piecesAt[c - 1][i - 1];
                            if(isObstacle(next2, piece, false) && !isObstacle(next2, piece, true)) {
                                list.add(next2); // 棋子不为空，且棋子可吃，则炮可以到达它
                                break;
                            }
                        }
                        break;
                    }
                }
                for(int i = piece.getRow() + 1; i <= 10; i++) {
                    SimplePiece next = piecesAt[c - 1][i - 1];
                    if(!isObstacle(next, piece, false)) list.add(next);
                    else {  // 棋子不为空，跳过它
                        for(i += 1; i <= 10; i++) {
                            SimplePiece next2 = piecesAt[c - 1][i - 1];
                            if(isObstacle(next2, piece, false) && !isObstacle(next2, piece, true)) {
                                list.add(next2); // 棋子不为空，且棋子可吃，则炮可以到达它
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case TYPE_SOLDIER:  // 小卒一去不复还
            {
                // 前进一步
                int newc = piece.getCol();
                int newr = piece.getRow() + (piece.getColor() == COLOR_RED ?  - 1 : 1);
                if(piece.getColor() == COLOR_RED && newr >= 1 || piece.getColor() == COLOR_BLACK && newr <= 10) {
                    SimplePiece front = piecesAt[newc - 1][newr - 1];
                    if(!isObstacle(front, piece, true)) list.add(front);
                }
                if(piece.getRow() >= 6 && piece.getColor() == COLOR_RED || piece.getRow() <= 5 && piece.getColor() == COLOR_BLACK) break;
                // 若已过河，则可横走
                newc = piece.getCol() - 1;
                newr = piece.getRow();
                if(newc >= 1 && newc <= 9) {
                    SimplePiece left = piecesAt[newc - 1][newr - 1];
                    if(!isObstacle(left, piece, true)) list.add(left);
                }
                newc = piece.getCol() + 1;
                if(newc >= 1 && newc <= 9) {
                    SimplePiece right = piecesAt[newc - 1][newr - 1];
                    if(!isObstacle(right, piece, true)) list.add(right);
                }
                break;
            }
            default:
                break;
        }

        return list;
    }

    private static boolean isObstacle(SimplePiece to, SimplePiece from, boolean eatable) {
        if(eatable) return to.getType() != TYPE_NONE && from.getColor() == to.getColor();
        else return to.getType() != TYPE_NONE;
    }
}
