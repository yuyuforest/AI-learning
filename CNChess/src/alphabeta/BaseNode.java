package alphabeta;

import java.util.ArrayList;

public interface BaseNode {
    ArrayList<BaseNode> getChildren();
    boolean isMax();
    int getValue();
}
