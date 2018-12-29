package main;

import chess.Controller;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static chess.Utils.CELL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("人工智障，中国翔棋");

        Group root = new Group();
        Scene scene = new Scene(root, 15 * CELL, 11 * CELL, Color.ALICEBLUE);
        new Controller(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
