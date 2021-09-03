package ams.airlinemanagementsystemos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class passengerController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Passengers> tvPassengers;

    @FXML
    private TableColumn<Passengers, Integer> colCode;

    @FXML
    private TableColumn<Passengers, String> colFirstname;

    @FXML
    private TableColumn<Passengers, String> colMiddlename;

    @FXML
    private TableColumn<Passengers, String> colLastname;

    @FXML
    private TableColumn<Passengers, LocalDate> colDOB;

    @FXML
    private TableColumn<Passengers, Integer> colAge;

    @FXML
    private TextField tfCode;

    @FXML
    private TextField tfFirstname;

    @FXML
    private TextField tfMiddlename;

    @FXML
    private TextField tfLastname;

    @FXML
    private DatePicker tfDOB;

    @FXML
    private Button btnPassengerInsert;

    @FXML
    private Button btnPassengerUpdate;

    @FXML
    private Button btnPassengerDelete;

    public void resetTextField(){
        tfFirstname.setText("");
        tfMiddlename.setText("");
        tfLastname.setText("");
        tfDOB.setValue(null);
    }

    @FXML
    void goToReservation(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("reservation.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handlePassengerCol(MouseEvent event) {
        Passengers passenger =  tvPassengers.getSelectionModel().getSelectedItem();
        tfCode.setText(""+passenger.getPassenger_Code());
        tfFirstname.setText(passenger.getFirst_Name());
        tfMiddlename.setText(passenger.getMiddle_Name());
        tfLastname.setText(passenger.getLast_Name());
        tfDOB.setValue(passenger.getPassenger_DOB());
    }

    @FXML
    void handleBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("airline.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBtnIUDAction(ActionEvent event) {
        if(event.getSource() == btnPassengerInsert){
            insertPassengerRecord();
        }
        else if(event.getSource() == btnPassengerUpdate){
            updatePassengerRecord();
        }
        else if(event.getSource() == btnPassengerDelete){
            deletePassengerRecord();
        }
    }

    private void deletePassengerRecord() {
        String query = "DELETE FROM passenger WHERE Passenger_Code = "+tfCode.getText()+"";
        executeQuery(query);
        showPassengers();
        resetTextField();
    }

    private void updatePassengerRecord() {
        String query = "UPDATE passenger SET First_Name = '"+ tfFirstname.getText() + "', Middle_Name = '"+ tfMiddlename.getText() +"', Last_Name = '"+tfLastname.getText()+"', Passenger_DOB = '"+tfDOB.getValue()+"' WHERE Passenger_Code = "+tfCode.getText()+"";
        executeQuery(query);
        showPassengers();
        resetTextField();
    }

    private void insertPassengerRecord() {
        String query = "INSERT INTO `passenger`(`First_Name`, `Middle_Name`, `Last_Name`, `Passenger_DOB`) VALUES ('"+ tfFirstname.getText() + "','" + tfMiddlename.getText() + "','" + tfLastname.getText() + "','" + tfDOB.getValue() + "')";
        executeQuery(query);
        showPassengers();
        resetTextField();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Connection getConnection(){
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams", "root", "");
            System.out.println("Connected to db!!!");
            return conn;
        }
        catch (Exception ex){
            System.out.println("Error: "+ ex.getMessage());
            return null;
        }
    }

    public ObservableList<Passengers> getPassengersList(){
        ObservableList<Passengers> passengersList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM passenger";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Passengers passengers;

            while (rs.next())
            {
                passengers = new Passengers(rs.getInt("Passenger_Code"), rs.getString("First_Name"), rs.getString("Middle_Name"),rs.getString("Last_Name"),rs.getDate("Passenger_DOB"));
                passengersList.add(passengers);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return passengersList;
    }

    public void showPassengers(){
        ObservableList<Passengers> list = getPassengersList();

        colCode.setCellValueFactory(new PropertyValueFactory<Passengers, Integer>("Passenger_Code"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<Passengers, String>("First_Name"));
        colMiddlename.setCellValueFactory(new PropertyValueFactory<Passengers, String>("Middle_Name"));
        colLastname.setCellValueFactory(new PropertyValueFactory<Passengers, String>("Last_Name"));
        colDOB.setCellValueFactory(new PropertyValueFactory<Passengers, LocalDate>("Passenger_DOB"));

        tvPassengers.setItems(list);
    }

    @FXML
    void initialize() {
        showPassengers();
    }
}
