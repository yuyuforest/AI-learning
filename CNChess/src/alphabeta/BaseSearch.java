package alphabeta;

import java.util.ArrayList;

public class BaseSearch {
    private final static int DEPTH = 5;
    BaseNode best;

    public BaseNode search(BaseNode current) {
        best = null;
        int value = alphabeta(current, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
        //if(value == Integer.MAX_VALUE || value == Integer.MIN_VALUE) return null;
        return best;
    }

    private int alphabeta(BaseNode node, int depth, int alpha, int beta) {
        ArrayList<BaseNode> children;
        int value = node.getValue();
        if(depth == 0 || value == Integer.MAX_VALUE || value == Integer.MIN_VALUE) return value;

        if(node.isMax()) {  // 极大层
            children = node.getChildren();
            if(depth == DEPTH) {
                best = children.get(0);
            }
            //System.out.println("max  depth " + depth + " alpha " + alpha + " beta " + beta + " " + best);
            for (BaseNode child: children) {
                int v = alphabeta(child, depth - 1, alpha, beta);
                if(alpha < v) {
                    alpha = v;
                    if(depth == DEPTH) {
                        best = child;
                    }
                }
                if(alpha >= beta){  // beta剪枝
                    break;
                }
            }
            return alpha;
        }
        else {              // 极小层
            children = node.getChildren();
            //System.out.println("min  depth " + depth + " alpha " + alpha + " beta " + beta + " " + best);
            for (BaseNode child: children) {
                int v = alphabeta(child, depth - 1, alpha, beta);
                if(beta > v) {
                    beta = v;
                    if(depth == DEPTH) {
                        best = child;
                    }
                }
                if(alpha >= beta){  // alpha剪枝
                    break;
                }
            }
            return beta;
        }
    }
}
