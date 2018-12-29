package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eightpuzzle.EPController;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getResource("/eightpuzzle/EPScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = fxmlLoader.load();
        primaryStage.setTitle("astar");
        primaryStage.setScene(new Scene(root, 1500, 950));
        primaryStage.show();

        EPController controller = fxmlLoader.getController();
        controller.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
