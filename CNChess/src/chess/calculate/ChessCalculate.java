package chess.calculate;

import chess.Utils;

public class ChessCalculate {

    public static int calculateValue(SimplePiece[] redPieces,
                                     SimplePiece[] blackPieces, SimplePiece[][] piecesAt, boolean isPlayer) {

        // 检查将、帅是否还在
        SimplePiece redGeneral = redPieces[4];
        SimplePiece blackGeneral = blackPieces[4];
        if(redGeneral.getType() == Utils.TYPE_NONE) {
            return Integer.MAX_VALUE;   // 红方将不在，黑方有利
        }
        if(blackGeneral.getType() == Utils.TYPE_NONE) {
            return Integer.MIN_VALUE;   // 黑方将不在，红方有利
        }

        // 检查将、帅是否对面
        if(redGeneral.getCol() == blackGeneral.getCol()) {
            int i;
            int c = redGeneral.getCol();
            for(i = blackGeneral.getRow() + 1; i < redGeneral.getRow(); i++) {
                if(piecesAt[c - 1][i - 1].getType() != Utils.TYPE_NONE) break;
            }
            if(i == redGeneral.getRow()) {
                return isPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
        }

        // 评估棋局
        int value = 0;
        int redValue;
        int blackValue;

        int v = 0;
        for(SimplePiece p : redPieces) {
            switch (p.getType()) {
                case Utils.TYPE_GENERAL :
                    v += 10000;
                    break;
                case Utils.TYPE_ADVISOR :
                    v += 150;
                    break;
                case Utils.TYPE_ELEPHANT :
                    v += 150;
                    break;
                case Utils.TYPE_HORSE :
                    v += 320;
                    break;
                case Utils.TYPE_CHARIOT :
                    v += 700;
                    break;
                case Utils.TYPE_CANNON :
                    v += 300;
                    break;
                case Utils.TYPE_SOLDIER :
                    v += 100;
                    break;
            }
        }
        redValue = v;

        v = 0;
        for(SimplePiece p : blackPieces) {
            switch (p.getType()) {
                case Utils.TYPE_GENERAL :
                    v += 10000;
                    break;
                case Utils.TYPE_ADVISOR :
                    v += 150;
                    break;
                case Utils.TYPE_ELEPHANT :
                    v += 150;
                    break;
                case Utils.TYPE_HORSE :
                    v += 320;
                    break;
                case Utils.TYPE_CHARIOT :
                    v += 700;
                    break;
                case Utils.TYPE_CANNON :
                    v += 300;
                    break;
                case Utils.TYPE_SOLDIER :
                    v += 100;
                    break;
            }
        }
        blackValue = v;

        value = blackValue - redValue;
        return value;
    }

    public static int getResult(ChessNode node) {
        SimplePiece[] redPieces = node.getRedPieces();
        SimplePiece[] blackPieces = node.getBlackPieces();
        SimplePiece[][] piecesAt = node.getPieces();

        // 检查将、帅是否还在
        SimplePiece redGeneral = redPieces[4];
        SimplePiece blackGeneral = blackPieces[4];
        if(redGeneral.getType() == Utils.TYPE_NONE) {
            return Utils.WINNER_BLACK;
        }
        else if(blackGeneral.getType() == Utils.TYPE_NONE) {
            return Utils.WINNER_RED;
        }

        // 检查将、帅是否对面
        if(redGeneral.getCol() == blackGeneral.getCol()) {
            int i;
            int c = redGeneral.getCol();
            for(i = blackGeneral.getRow() + 1; i < redGeneral.getRow(); i++) {
                if(piecesAt[c - 1][i - 1].getType() != Utils.TYPE_NONE) break;
            }
            if(i == redGeneral.getRow()) {
                return node.isPlayer() ? Utils.WINNER_RED : Utils.WINNER_BLACK;
            }
        }

        return Utils.WINNER_NONE;
    }
}
