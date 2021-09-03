package ams.airlinemanagementsystemos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class reservationController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPassengerid;

    @FXML
    private TableColumn<?, ?> colFlightcode;

    @FXML
    private TableColumn<?, ?> colDepartureairport;

    @FXML
    private TableColumn<?, ?> colDeparturedate;

    @FXML
    private TableColumn<?, ?> colArrivalairport;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colFare;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TextField tfCode;

    @FXML
    private TextField tfPassengerid;

    @FXML
    private DatePicker tfDeparturedate;

    @FXML
    private ChoiceBox<?> tfDepartureairport;

    @FXML
    private ChoiceBox<?> tfArrivalairport;

    @FXML
    private ChoiceBox<?> tfAirline;

    @FXML
    private ChoiceBox<?> tfFlightcode;

    @FXML
    private DatePicker tfArrivaldate;

    @FXML
    private ChoiceBox<?> tfClass;

    @FXML
    private TextField tfFare;

    @FXML
    private ChoiceBox<?> tfStatus;

    @FXML
    void handleBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("passenger.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void initialize() {

    }
}
