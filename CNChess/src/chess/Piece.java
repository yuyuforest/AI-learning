package chess;

import chess.calculate.SimplePiece;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static chess.Utils.CELL;
import static chess.Utils.PIECE_RADIUS;

public class Piece extends SimplePiece {
    private Group root;
    private Circle circle;
    private Circle remark;
    private Text text;

    public Piece(Group root, int col, int row) {
        super(col, row);

        this.root = root;

        double x = col * CELL;
        double y = row * CELL;

        remark = new Circle(x, y, PIECE_RADIUS + 7);
        remark.setFill(Color.TRANSPARENT);
        remark.setStrokeWidth(2);
        remark.setVisible(false);

        circle = new Circle(x, y, PIECE_RADIUS);
        circle.setFill(Color.TRANSPARENT);

        root.getChildren().add(remark);
        root.getChildren().add(circle);
    }

    public Piece(Group root, int col, int row, int type, int color, String name) {
        super(col, row, type, color);

        this.root = root;

        double x = col * CELL;
        double y = row * CELL;

        remark = new Circle(x, y, PIECE_RADIUS + 7);
        remark.setFill(Color.TRANSPARENT);
        remark.setStrokeWidth(2);
        remark.setVisible(false);

        circle = new Circle(x, y, PIECE_RADIUS);
        circle.setFill(Color.LIGHTYELLOW);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        circle.setStrokeType(StrokeType.INSIDE);

        text = new Text(name);
        text.setFont(Font.font("KaiTi", FontWeight.BOLD, 34));
        text.setX(x - 17);
        text.setY(y + 13);
        if(color == Utils.COLOR_RED) text.setFill(Color.RED);
        else text.setFill(Color.BLACK);

        root.getChildren().add(remark);
        root.getChildren().add(circle);
        root.getChildren().add(text);
    }

    public void setOnMouseClicked(EventHandler<MouseEvent> eventHandler) {
        circle.setOnMouseClicked(eventHandler);
        if(text != null) text.setOnMouseClicked(eventHandler);
    }


    public void setRemark(boolean selected) {
        remark.setVisible(true);
        if(selected) remark.setStroke(Color.BLUE);
        else remark.setStroke(Color.ORANGE);
    }

    public void cancelRemark() {
        remark.setVisible(false);
    }

    public void eaten() {
        super.eaten();

        root.getChildren().remove(text);
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeWidth(0);
    }

    public void moveTo(int col, int row) {
        super.moveTo(col, row);

        double x = col * CELL;
        double y = row * CELL;
        circle.setCenterX(x);
        circle.setCenterY(y);
        remark.setCenterX(x);
        remark.setCenterY(y);
        if(text != null) {
            text.setX(x - 17);
            text.setY(y + 13);
        }
    }
}
