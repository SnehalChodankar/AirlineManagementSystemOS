package ams.airlinemanagementsystemos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import javafx.fxml.FXML;

/**
 * airportController used to handle various functionalities associated with respect to airport,
 * like for example, insert, update airport details when visited directly without selecting airline
 * from previous page, and insert, delete association when visited after selecting an airline.
 *
 * Methods defined in airportController:
 * - setAirline(String acode,int opstatus);         returns void
 * - onBackAction(ActionEvent event);               returns void
 * - handleBtnIUDAction(ActionEvent event);         returns void
 * - checkAirportCode(String ACode);                returns boolean
 * - validationCheck(Object src);                   returns boolean
 * - insertAirportRecord();                         returns void
 * - updateAirportRecord();                         returns void
 * - changeStatusAirportRecord();                   returns void
 * - handleBtnAssociationAction(ActionEvent event); returns void
 * - insertAirportAssociationRecord();              returns void
 * - deleteAirportAssociationRecord();              returns void
 * - getAirportsList();                             returns ObservableList<Airports>
 * - showAirports();                                returns void
 * - getConnection();                               returns Connection
 * - executeQuery(String query);                    returns void
 * - handleAirportCol(MouseEvent event);            returns void
 * - resetTextField();                              returns void
 * - initialize();                                  returns void
 * */
public class airportController {
    private Stage stage;
    private Scene scene;

    public String airlineCode;
    public int operationalStatus;

    /**
     * setAirline is called in the previous page controller in order to send the data from that
     * controller to this airport controller.
     * */
    public void setAirline(String acode,int opstatus){
        this.airlineCode = acode;
        this.operationalStatus=opstatus;
        showAirports();
        if(airlineCode == null){
            btnAirportInsertAssociation.setVisible(false);
            btnAirportDeleteAssociation.setVisible(false);
        }
        else{
            if(operationalStatus==0){
                btnAirportInsertAssociation.setDisable(true);
                btnAirportDeleteAssociation.setDisable(true);
            }
            btnAirportInsert.setVisible(false);
            btnAirportInsert.setManaged(false);

            btnAirportUpdate.setVisible(false);
            btnAirportUpdate.setManaged(false);

            btnAirportChangeStatus.setVisible(false);
            btnAirportChangeStatus.setManaged(false);
        }
    }

    @FXML
    private AnchorPane apMain;

    @FXML
    private ImageView ivMain;

    @FXML
    private TableView<Airports> tvAirports;

    @FXML
    private TableColumn<Airports, String> colCode;

    @FXML
    private TableColumn<Airports, String> colName;

    @FXML
    private TableColumn<Airports, String> colCity;

    @FXML
    private TableColumn<Airports, String> colState;

    @FXML
    private TableColumn<Airports, Integer> colOpStatus;

    @FXML
    private TextField tfCode;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfCity;

    @FXML
    private TextField tfState;

    @FXML
    private Button btnAirportInsert;

    @FXML
    private Button btnAirportUpdate;

    @FXML
    private Button btnAirportChangeStatus;

    @FXML
    private Button btnAirportInsertAssociation;

    @FXML
    private Button btnAirportDeleteAssociation;

    @FXML
    private Button btnBack;

    /**
     * onBackAction function takes user back to airline page.
     * */
    @FXML
    void onBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("airline.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * handleBtnIUDAction is initiated whenever the insert/update/change status
     * buttons are pressed.
     * */
    @FXML
    void handleBtnIUDAction(ActionEvent event) throws SQLException {
        if(validationCheck(event.getSource())) {
            if (event.getSource() == btnAirportInsert) {
                insertAirportRecord();
            } else if (event.getSource() == btnAirportUpdate) {
                updateAirportRecord();
            } else if (event.getSource() == btnAirportChangeStatus) {
                changeStatusAirportRecord();
            }
        }
    }

    /**
     * checkAirportCode is used only to check if user entered Airport code is already present
     * in the database or not.
     * If present, function returns true, else returns false.
     * */
    private boolean checkAirportCode(String ACode) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM airport WHERE Airport_Code='"+ACode+"'";
        Statement st;
        ResultSet rs;
        st = conn.createStatement();
        rs = st.executeQuery(query);

        if(rs.next()) return true;
        else return false;
    }

    /**
     * validationCheck is used to check if all the necessary fields are field by user for a
     * particular function.
     * If not, an alert box is displayed with necessary help text.
     * If any of the conditions does not get satisfied, then function returns false, else returns true.
     * */
    private boolean validationCheck(Object src) throws SQLException {
        Alert a = new Alert(Alert.AlertType.WARNING);

        if(src == btnAirportInsert){
            boolean isAirportCode = tfCode.getText().chars().allMatch(Character::isLetterOrDigit);
            boolean isAirportName = tfCode.getText().chars().allMatch(Character::isLetter);
            boolean isCity = tfCode.getText().chars().allMatch(Character::isLetter);
            boolean isState = tfCode.getText().chars().allMatch(Character::isLetter);

            if(Objects.equals(tfCode.getText(), "") || Objects.equals(tfName.getText(), "") || Objects.equals(tfCity.getText(), "") || Objects.equals(tfState.getText(), "") ){
                a.setContentText("Please fill all the fields!!! ");
                a.show();
                return false;
            }
            else if(!isAirportCode){
                a.setContentText("Invalid Airport Code!!!");
                a.show();
                return false;
            }
            else if(!isAirportName){
                a.setContentText("Invalid Airport Name!!!");
                a.show();
                return false;
            }
            else if(!isCity){
                a.setContentText("Invalid Airport City!!!");
                a.show();
                return false;
            }
            else if(!isState){
                a.setContentText("Invalid Airport State!!!");
                a.show();
                return false;
            }
            else if(checkAirportCode(tfCode.getText())){
                a.setContentText("Airport already exists in the database.");
                a.show();
                return false;
            }
        }
        else if(src == btnAirportUpdate){
            if(Objects.equals(tfCode.getText(), "") || Objects.equals(tfName.getText(), "") || Objects.equals(tfCity.getText(), "") || Objects.equals(tfState.getText(), "") ){
                a.setContentText("All fields are required. Check Help for more details.");
                a.show();
                return false;
            }
            else if(!checkAirportCode(tfCode.getText())){
                a.setContentText("Airport doesn't exists in the database.");
                a.show();
                return false;
            }
        }

        else if(src==btnAirportChangeStatus){
            if(tfCode.getText().equals("")){
                a.setContentText("Please fill in the Airport Code.");
                a.show();
                return false;
            }
            else if(!checkAirportCode(tfCode.getText())){
                a.setContentText("Airport doesn't exists in the database.");
                a.show();
                return false;
            }
        }
        return true;
    }

    /**
     * insertAirportRecord is used to insert a new airport into the database.
     * */
    private void insertAirportRecord() {
        String query = "INSERT INTO airport (`Airport_Code`, `Airport_Name`, `Airport_City`, `Airport_State`) VALUES ('"+ tfCode.getText() + "','" + tfName.getText() + "','" + tfCity.getText() + "','" + tfState.getText() + "')";
        executeQuery(query);
        showAirports();
        resetTextField();
    }

    /**
     * updateAirportRecord is used to update the information related to the selected airport.
     * */
    private void updateAirportRecord() {
        String query = "UPDATE airport SET Airport_Name = '"+ tfName.getText() + "', Airport_City = '"+tfCity.getText()+"', Airport_State = '"+tfState.getText()+"' WHERE Airport_Code = '"+tfCode.getText()+"'";
        executeQuery(query);
        showAirports();
        resetTextField();
    }

    /**
     * changeStatusAirportRecord is used to change the operational status column in the
     * database to 0 and vise versa.
     * */
    private void changeStatusAirportRecord() {
        String query = "UPDATE airport SET Operational_Status = Operational_Status ^ 1 WHERE Airport_Code = '"+tfCode.getText()+"'";
        executeQuery(query);
        showAirports();
        resetTextField();
    }

    /**
     * handleBtnAssociationAction is initiated whenever the insert association/delete association
     * buttons are pressed.
     * */
    @FXML
    void handleBtnAssociationAction(ActionEvent event) throws SQLException {
        if(event.getSource() == btnAirportInsertAssociation){
            insertAirportAssociationRecord();
        }
        else if(event.getSource() == btnAirportDeleteAssociation){
            deleteAirportAssociationRecord();
        }
    }

    /**
     * insertAirportAssociationRecord is used to insert a new airport-airline association
     * into the database.
     * */
    private void insertAirportAssociationRecord() throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM airport WHERE Airport_Code='"+tfCode.getText()+"' AND Operational_Status=1";
        Statement st;
        ResultSet rs;
        st = conn.createStatement();
        rs = st.executeQuery(query);

        if(rs.next()){
            query = "SELECT * FROM airline_airport_association WHERE Airport_Code = '"+tfCode.getText()+"' AND Airline_Code = '"+airlineCode+"'";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                query = "UPDATE airline_airport_association SET Flag = 1 WHERE Airport_Code = '"+tfCode.getText()+"' AND Airline_Code = '"+airlineCode+"'";
                executeQuery(query);
            }
            else {
                query = "INSERT INTO `airline_airport_association`(`Airline_Code`, `Airport_Code`) VALUES ('" + airlineCode + "','" + tfCode.getText() + "')";
                executeQuery(query);
            }
            showAirports();
        }
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Airport is not registered!!!");
            a.setContentText("Airport code is not present in airport table.");
            a.show();
        }
    }

    /**
     * deleteAirportAssociationRecord is used to delete an airport-airline association
     * from the database.
     * */
    private void deleteAirportAssociationRecord() {
        String query = "UPDATE airline_airport_association SET Flag = 0 WHERE Airport_Code = '"+tfCode.getText()+"' AND Airline_Code = '"+airlineCode+"'";
        executeQuery(query);
        showAirports();
    }

    /**
     * getAirportsList function creates a list of Airport objects which reflect the data
     * from the Airport table.
     * This function returns this list as OberservableList whenever called.
     * */
    public ObservableList<Airports> getAirportsList(){
        ObservableList<Airports> airportList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query;
        System.out.println("Air Code"+airlineCode);
        if(airlineCode != null){
            query = "SELECT * FROM airport WHERE Airport_Code IN (SELECT Airport_Code FROM airline_airport_association WHERE Airline_Code='" + airlineCode +"' AND Flag=1)" ;
        }
        else{
            query = "SELECT * FROM airport";
        }

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Airports airports;

            while (rs.next())
            {
                airports = new Airports(rs.getString("Airport_Code"), rs.getString("Airport_Name"), rs.getString("Airport_City"),rs.getString("Airport_State"), rs.getInt("Operational_Status"));
                airportList.add(airports);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return airportList;
    }

    /**
     * showAirports function is used to fill the table with airport details.
     * */
    public void showAirports(){
        ObservableList<Airports> list = getAirportsList();

        colCode.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_Code"));
        colName.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_Name"));
        colCity.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_City"));
        colState.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_State"));
        colOpStatus.setCellValueFactory(new PropertyValueFactory<Airports, Integer>("Op_Status"));
        tvAirports.setItems(list);
    }

    /**
     * getConnection function is used to connect to the mysql database with the default root credentials.
     * */
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

    /**
     * executeQuery is used only to execute certain queries passed as parameter to this function.
     * */
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

    /***
     * handleAirportCol Autofill data in the respective fields when an entry is clicked in the table.
     */
    @FXML
    void handleAirportCol(MouseEvent event) {
        Airports airports =  tvAirports.getSelectionModel().getSelectedItem();
        tfCode.setText(airports.getAirport_Code());
        tfName.setText(airports.getAirport_Name());
        tfCity.setText(airports.getAirport_City());
        tfState.setText(airports.getAirport_State());
    }

    /**
     * resetTextField is used to reset the textfield to blank after every operation performed.
     * */
    public void resetTextField(){
        tfCode.setText("");
        tfName.setText("");
        tfCity.setText("");
        tfState.setText("");
    }

    /**
     * initialize method contains ivMain and tooltip information to be set as soon as airport.fxml
     * is loaded.
     * */
    @FXML
    void initialize() {
        Tooltip tInsert = new Tooltip("Inserts an airport record.");
        btnAirportInsert.setTooltip(tInsert);
        Tooltip tUpdate = new Tooltip("Updates the airport details.");
        btnAirportUpdate.setTooltip(tUpdate);
        Tooltip tStatus = new Tooltip("Changes the status of the selected airline.");
        btnAirportChangeStatus.setTooltip(tStatus);
        Tooltip tinsertAssociation = new Tooltip("Inserts the association with the selected airline");
        btnAirportInsertAssociation.setTooltip(tinsertAssociation);
        Tooltip tdeleteAssociation = new Tooltip("Deletes the association with the selected airline.");
        btnAirportDeleteAssociation.setTooltip(tdeleteAssociation);
        Tooltip tBack = new Tooltip("Goes back to the previous page.");
        btnBack.setTooltip(tBack);
        showAirports();
        ivMain.fitWidthProperty().bind(apMain.widthProperty());
        ivMain.fitHeightProperty().bind(apMain.heightProperty());
    }

}
