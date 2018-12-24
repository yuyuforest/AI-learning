package Main;

import EightPuzzle.Search1.EP1Search;
import EightPuzzle.Search2.EP2Search;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import EightPuzzle.EPController;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getResource("../EightPuzzle/EPScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = fxmlLoader.load();
        primaryStage.setTitle("h1=放错位置的码个数");
        primaryStage.setScene(new Scene(root, 900, 900));
        primaryStage.setX(60);
        primaryStage.setY(50);
        primaryStage.show();

        EPController controller1 = fxmlLoader.getController();
        int[] beginState = {2,8,3,1,6,4,7,0,5};
        int[] endState = {1,2,3,8,0,4,7,6,5};
        controller1.init(beginState, endState, new EP1Search());

        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root2 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("h2=0（广度优先搜索）");
        stage.setScene(new Scene(root2, 900, 900));
        stage.setX(primaryStage.getX() + primaryStage.getWidth());
        primaryStage.setY(primaryStage.getY());
        stage.show();

        EPController controller2 = fxmlLoader.getController();
        controller2.init(beginState, endState, new EP2Search());

        Controller c = new Controller(controller1, controller2);
        controller1.setController(c);
        controller2.setController(c);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
