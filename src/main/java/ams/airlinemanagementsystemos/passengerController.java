package ams.airlinemanagementsystemos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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

    private int PassengerCode;

    @FXML
    private AnchorPane apMain;

    @FXML
    private ImageView ivMain;

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

    @FXML
    private Button btnShowActive;

    @FXML
    private Button btnShowInactive;

    @FXML
    void showActivePassengers(ActionEvent event) {
        showPassengers(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
    }

    @FXML
    void showInactivePassengers(ActionEvent event) {
        showPassengers(0);
        btnShowActive.setVisible(true);
        btnShowActive.setManaged(true);
        btnShowInactive.setVisible(false);
        btnShowInactive.setManaged(false);
    }

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
        tfFirstname.setText(passenger.getFirst_Name());
        tfMiddlename.setText(passenger.getMiddle_Name());
        tfLastname.setText(passenger.getLast_Name());
        tfDOB.setValue(passenger.getPassenger_DOB());

        PassengerCode = passenger.getPassenger_Code();
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
        if(validationCheck(event.getSource())) {
            if (event.getSource() == btnPassengerInsert) {
                insertPassengerRecord();
            } else if (event.getSource() == btnPassengerUpdate) {
                updatePassengerRecord();
            } else if (event.getSource() == btnPassengerDelete) {
                deletePassengerRecord();
            }
        }
    }

    private boolean validationCheck(Object source) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        boolean isPassengerFName = tfFirstname.getText().chars().allMatch(Character::isLetter);
        boolean isPassengerMName = tfMiddlename.getText().chars().allMatch(Character::isLetter);
        boolean isPassengerLName = tfLastname.getText().chars().allMatch(Character::isLetter);

        if(source == btnPassengerInsert){
            if(Objects.equals(tfFirstname.getText(), "") || Objects.equals(tfMiddlename.getText(), "") || Objects.equals(tfLastname.getText(), "") || Objects.equals(tfDOB.getValue(), null)){
                a.setContentText("Please fill all the fields!!! ");
                a.show();
                return false;
            }
            else if(!isPassengerFName){
                a.setHeaderText("Invalid Passenger First Name!!!");
                a.setContentText("Please enter a valid name.");
                a.show();
                return false;
            }
            else if(!isPassengerMName){
                a.setHeaderText("Invalid Passenger Middle Name!!!");
                a.setContentText("Please enter a valid name.");
                a.show();
                return false;
            }
            else if(!isPassengerLName){
                a.setHeaderText("Invalid Passenger Last Name!!!");
                a.setContentText("Please enter a valid name.");
                a.show();
                return false;
            }
            else if(checkDOB()){
                a.setContentText("Age is Invalid!!!");
                a.show();
                return false;
            }
        }
        else if(source == btnPassengerUpdate){
            if(Objects.equals(tfFirstname.getText(), "") || Objects.equals(tfMiddlename.getText(), "") || Objects.equals(tfLastname.getText(), "") || Objects.equals(tfDOB.getValue(), null)){
                a.setContentText("All the fields are required!!! ");
                a.show();
                return false;
            }
            else if(!isPassengerFName){
                a.setHeaderText("Invalid Passenger First Name!!!");
                a.setContentText("Please enter a valid name.");
                a.show();
                return false;
            }
            else if(!isPassengerMName){
                a.setHeaderText("Invalid Passenger Middle Name!!!");
                a.setContentText("Please enter a valid name.");
                a.show();
                return false;
            }
            else if(!isPassengerLName){
                a.setHeaderText("Invalid Passenger Last Name!!!");
                a.setContentText("Please enter a valid name.");
                a.show();
                return false;
            }
            else if(checkDOB()){
                a.setContentText("Age is Invalid!!!");
                a.show();
                return false;
            }
        }

        return true;
    }

    private boolean checkDOB() {
        LocalDate dob = tfDOB.getValue();

        Calendar c = Calendar.getInstance();
        LocalDate present = LocalDate.ofInstant(c.toInstant(), ZoneId.systemDefault());
        int year= Period.between(dob, present).getYears();

        if(LocalDate.now().getYear() <= dob.getYear() || year < 2 || year > 99){
            return true;
        }
        return false;
    }

    private void deletePassengerRecord() {

        String query = "UPDATE passenger SET Account_Status= Account_Status ^ 1 WHERE Passenger_Code = "+PassengerCode+"";
        executeQuery(query);
        showPassengers(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
        resetTextField();
    }

    private void updatePassengerRecord() {
        String query = "UPDATE passenger SET First_Name = '"+ tfFirstname.getText() + "', Middle_Name = '"+ tfMiddlename.getText() +"', Last_Name = '"+tfLastname.getText()+"', Passenger_DOB = '"+tfDOB.getValue()+"' WHERE Passenger_Code = "+PassengerCode+"";
        executeQuery(query);
        showPassengers(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);

        resetTextField();
    }

    private void insertPassengerRecord() {
        String query = "INSERT INTO `passenger`(`First_Name`, `Middle_Name`, `Last_Name`, `Passenger_DOB`) VALUES ('"+ tfFirstname.getText() + "','" + tfMiddlename.getText() + "','" + tfLastname.getText() + "','" + tfDOB.getValue() + "')";
        executeQuery(query);
        showPassengers(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
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

    public ObservableList<Passengers> getPassengersList(int Acc_Status){
        ObservableList<Passengers> passengersList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM passenger WHERE Account_Status="+Acc_Status+"";
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

    public void showPassengers(int Acc_Status){
        ObservableList<Passengers> list = getPassengersList(Acc_Status);

        colCode.setCellValueFactory(new PropertyValueFactory<Passengers, Integer>("Passenger_Code"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<Passengers, String>("First_Name"));
        colMiddlename.setCellValueFactory(new PropertyValueFactory<Passengers, String>("Middle_Name"));
        colLastname.setCellValueFactory(new PropertyValueFactory<Passengers, String>("Last_Name"));
        colDOB.setCellValueFactory(new PropertyValueFactory<Passengers, LocalDate>("Passenger_DOB"));
        colAge.setCellValueFactory(new PropertyValueFactory<Passengers,Integer>("age"));

        tvPassengers.setItems(list);
    }

    @FXML
    void initialize() {
        showPassengers(1);
        ivMain.fitWidthProperty().bind(apMain.widthProperty());
        ivMain.fitHeightProperty().bind(apMain.heightProperty());
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
    }
}
