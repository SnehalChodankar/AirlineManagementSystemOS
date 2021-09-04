package ams.airlinemanagementsystemos;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainController {
    private Stage stage;
    private Scene scene;

    /**
     * onStartClick method is called when the Start button is clicked. This takes the user
     * to airline details page, that is, airline.fxml.
     * */
    @FXML
    void onStartClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("airline.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
    }
}