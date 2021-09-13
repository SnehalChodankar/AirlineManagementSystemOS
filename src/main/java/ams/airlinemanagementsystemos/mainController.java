package ams.airlinemanagementsystemos;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class mainController {
    private Stage stage;
    private Scene scene;

    @FXML
    private AnchorPane apMain;

    @FXML
    private  ImageView ivMain;

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

        // Bind the imageView width property to gridPane width property
        // So, if width of gridPane change, the width of imageView automatically will be change
        ivMain.fitWidthProperty().bind(apMain.widthProperty());
        ivMain.fitHeightProperty().bind(apMain.heightProperty());

        // Make the ratio same with original image
        //ivMain.setPreserveRatio(true);

    }
}