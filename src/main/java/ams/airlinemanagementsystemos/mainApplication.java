package ams.airlinemanagementsystemos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * mainApplication loads up the start up screen, that is, the login page of the application.
 *
 * Methods defined in mainApplication:
 * - start(Stage stage);                 returns void
 * - main(String[] args);                returns void
 * */
public class mainApplication extends Application {

    /**
     * start method is used to load the first scene, main.fxml.
     * */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Airline Management System");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}