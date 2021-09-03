package ams.airlinemanagementsystemos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class flightController {
    private Stage stage;
    private Scene scene;

    public int airlineCode;

    public void setAirline(int acode){
        this.airlineCode = acode;
        System.out.println(airlineCode);
    }

    @FXML
    void onBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("airline.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDepartsfrom;

    @FXML
    private TableColumn<?, ?> colDeparturedate;

    @FXML
    private TableColumn<?, ?> colArrivesat;

    @FXML
    private TableColumn<?, ?> colArrivaldate;

    @FXML
    private TableColumn<?, ?> colMake;

    @FXML
    private TableColumn<?, ?> colModel;

    @FXML
    private TableColumn<?, ?> colCapacity;

    @FXML
    private TableColumn<?, ?> colCarriername;

    @FXML
    private TextField tfCode;

    @FXML
    private TextField tfDepartsfrom;

    @FXML
    private TextField tfDeparturedate;

    @FXML
    private TextField tfArrivesat;

    @FXML
    private TextField tfArrivaldate;

    @FXML
    private TextField tfMake;

    @FXML
    private TextField tfModel;

    @FXML
    private TextField tfCapacity;

    @FXML
    private TextField tfCarriername;

}
