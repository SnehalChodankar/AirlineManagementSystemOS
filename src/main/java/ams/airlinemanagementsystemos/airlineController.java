package ams.airlinemanagementsystemos;

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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class airlineController {
    private Stage stage;
    private Scene scene;

    ObservableList<Integer> durationList = FXCollections.observableArrayList(6,12,24);
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Airlines> tvAirlines;

    @FXML
    private TableColumn<Airlines, Integer> colCode;

    @FXML
    private TableColumn<Airlines, String> colName;

    @FXML
    private TableColumn<Airlines, String> colAddress;

    @FXML
    private TableColumn<Airlines, String> colCity;

    @FXML
    private TableColumn<Airlines, String> colState;

    @FXML
    private TableColumn<Airlines, Integer> colZip;

    @FXML
    private TableColumn<Airlines, String> colEmail;

    @FXML
    private TableColumn<Airlines, LocalDate> colLicenseEffectiveDate;

    @FXML
    private TableColumn<Airlines, LocalDate> colLicenseExpiryDate;

    @FXML
    private TableColumn<Airlines, String> colDuration;

    @FXML
    private TableColumn<Airlines, Integer> colTotalFlights;

    @FXML
    private TableColumn<Airlines, Integer> colFlightsDepartToday;

    @FXML
    private TextField tfCode;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfCity;

    @FXML
    private TextField tfState;

    @FXML
    private TextField tfZip;

    @FXML
    private TextField tfEmail;

    @FXML
    private DatePicker tfLicenseEffectiveDate;

    @FXML
    private DatePicker tfLicenseExpiryDate;

    @FXML
    private ChoiceBox<Integer> tfDuration;

    @FXML
    private Button btnAirlineInsert;

    @FXML
    private Button btnAirlineUpdate;

    @FXML
    private Button btnAirlineDelete;

    @FXML
    // Handler for Back button i.e. to navigate back to Main Page
        void onBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Handler for Airport button i.e. to navigate to Airports Page
    void goToAirports(ActionEvent event) throws IOException {
        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();

        // Only allowed to navigate to Airport Page if an Airline has been selected
        // Airport page consists of details of airports partnered with a particular airline
        if(airline == null){
            System.out.println("Please select an airline to proceed to airport details.");
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("airport.fxml"));
            Parent root = loader.load();

            airportController airC = loader.getController();
            airC.setAirline(airline.getAirline_Code());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }

    @FXML
    // Handler for Flights button i.e. to navigate to Flights Page
    void goToFlights(ActionEvent event) throws IOException {
        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();

        // Only allowed to navigate to Flight Page if an Airline has been selected
        // Flight page consists of details of Flights owned by a particular airline
        if(airline == null){
            System.out.println("Please select an airline to proceed to airport details.");
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("flight.fxml"));
            Parent root = loader.load();

            flightController flightC = loader.getController();
            flightC.setAirline(airline.getAirline_Code());

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    @FXML
    // Handler for Passengers button i.e. to navigate to Passengers Page
    void goToPassengers(ActionEvent event) throws IOException {
        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();

        // Only allowed to navigate to Passenger Page if an Airline has been selected
        // Passenger page consists of Airline Customer Details
        // i.e. Customers who have created accounts with the Airline
        if(airline == null){
            System.out.println("Please select an airline to proceed to airport details.");
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Passenger.fxml"));
            Parent root = loader.load();

            passengerController passengerC = loader.getController();
            passengerC.setAirline(airline.getAirline_Code());
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    //Using JDBC to create connection with database
    public Connection getConnection(){
        Connection conn;
        try {

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams", "root", "");
            System.out.println("Connected to db!!!");
            return conn;
        }
    //Error Message if connection couldn't be established
        catch (Exception ex){
            System.out.println("Error: "+ ex.getMessage());
            return null;
        }
    }

    //Creates a list of Airline objects which reflect the data from the Airline table
    public ObservableList<Airlines> getAirlinesList(){
        ObservableList<Airlines> airlineList = FXCollections.observableArrayList();
        Connection conn = getConnection();

        //query for retrieving total number of aircraft owned by each airline
        String FData_query="SELECT COUNT(`Flight_Code`) FROM flight GROUP BY `Carrier_Name` HAVING `Carrier_Name`=`Airline_Name`";

        //query for retrieving total number of Flights active on the current day
        String RData_query="SELECT COUNT(DISTINCT`route`.`Flight_Code`) FROM route,flight" +
                " WHERE route.Flight_Code=flight.Flight_Code AND `Carrier_Name`= `Airline_Name` AND DATE(`Departure_Date`) = DATE(NOW())";

        //query for retrieving all necessary information to display in GUI of the Airline Page
        String query = "SELECT `Airline_Code`, `Airline_Name`, `Airline_Address`, `Airline_City`, `Airline_State`, `Airline_Zip`, `Airline_Email`," +
                " `License_Issue`, `License_Expiry`" +
                ",(" + FData_query + ") AS `Total_Flights`" +
                ",(" + RData_query + ") AS `Flights_Today`" +
                " FROM airline WHERE `Operational_Status` = 1";

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);        //resultant dataset from the query stored in rs
            Airlines airlines;

            while (rs.next())           //Airline object created for each row entry in rs & added to the list
            {
               airlines = new Airlines(rs.getInt("Airline_Code"), rs.getString("Airline_Name"),
                       rs.getString("Airline_Address"),rs.getString("Airline_City"),
                       rs.getString("Airline_State"), rs.getInt("Airline_Zip"),
                       rs.getString("Airline_Email"), rs.getDate("License_Issue"),
                       rs.getDate("License_Expiry"),rs.getInt("Total_Flights"),
                       rs.getInt("Flights_Today"));

               airlineList.add(airlines);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return airlineList;
    }

    //Displays Data from Airline Model
    public void showAirlines(){
        ObservableList<Airlines> list = getAirlinesList();          //Retrieve data from DB in the form of a list of Airline objects

        //Transfer data from objects to table in GUI
        colCode.setCellValueFactory(new PropertyValueFactory<Airlines, Integer>("Airline_Code"));
        colName.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_Name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_Address"));
        colCity.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_City"));
        colState.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_State"));
        colZip.setCellValueFactory(new PropertyValueFactory<Airlines, Integer>("Airline_Zip"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_Email"));
        colLicenseEffectiveDate.setCellValueFactory(new PropertyValueFactory<Airlines, LocalDate>("License_Issue"));
        colLicenseExpiryDate.setCellValueFactory(new PropertyValueFactory<Airlines, LocalDate>("License_Expiry"));
        colDuration.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Duration"));
        colTotalFlights.setCellValueFactory(new PropertyValueFactory<Airlines, Integer>("Total_Flights"));
        colFlightsDepartToday.setCellValueFactory(new PropertyValueFactory<Airlines, Integer>("Flights_Today"));

        tvAirlines.setItems(list);
    }

    //to reset text fields after each operation
    public void resetTextField(){
        tfCode.setText("");
        tfName.setText("");
        tfAddress.setText("");
        tfCity.setText("");
        tfState.setText("");
        tfZip.setText("");
        tfEmail.setText("");
        tfLicenseEffectiveDate.setValue(null);
    }

    @FXML
    //Autofills data in the respective fields when an entry is clicked in the table
    void handleAirlineCol(MouseEvent event) {
        Airlines airlines =  tvAirlines.getSelectionModel().getSelectedItem();
        tfCode.setText(""+airlines.getAirline_Code());
        tfName.setText(airlines.getAirline_Name());
        tfAddress.setText(airlines.getAirline_Address());
        tfCity.setText(airlines.getAirline_City());
        tfState.setText(airlines.getAirline_State());
        tfZip.setText(""+airlines.getAirline_Zip());
        tfEmail.setText(airlines.getAirline_Email());
        tfLicenseEffectiveDate.setValue(airlines.getLicense_Issue());
        tfLicenseExpiryDate.setValue(airlines.getLicense_Expiry());
    }

    @FXML
    //Handler for Insertion, Updation & Deletion of records
    void handleBtnIUDAction(ActionEvent event) {
        if(event.getSource() == btnAirlineInsert){
            insertAirlineRecord();
        }
        else if(event.getSource() == btnAirlineUpdate){
            updateAirlineRecord();
        }
        else if(event.getSource() == btnAirlineDelete){
            deleteAirlineRecord();
        }
    }

    //Performs Deletion of record by using information from the necessary text field
    private void deleteAirlineRecord() {
        String query = "UPDATE airline SET Operational_Status=0 WHERE Airline_Code = "+tfCode.getText()+"";
        executeQuery(query);
        showAirlines();
        resetTextField();
    }

    //Performs Updation of record by using information from the necessary text field
    private void updateAirlineRecord() {
        LocalDate expiry_date = convertToExpiry();
        String query = "UPDATE airline SET Airline_Name = '"+ tfName.getText() + "', Airline_Address = '"+ tfAddress.getText() +"', Airline_City = '"+tfCity.getText()+"', Airline_State = '"+tfState.getText()+"', Airline_Zip = "+ Integer.parseInt(tfZip.getText()) +", Airline_Email = '"+ tfEmail.getText() +"', License_Issue = '"+ tfLicenseEffectiveDate.getValue() +"', License_Expiry = '"+ expiry_date +"' WHERE Airline_Code = "+tfCode.getText()+"";
        executeQuery(query);
        showAirlines();
        resetTextField();
    }

    //Performs Insertion of record by using information from the necessary text field
    private void insertAirlineRecord() {
        LocalDate expiry_date = convertToExpiry();
        String query = "INSERT INTO airline(`Airline_Code`, `Airline_Name`, `Airline_Address`, `Airline_City`, `Airline_State`, `Airline_Zip`, `Airline_Email`, `License_Issue`, `License_Expiry`) VALUES ("+ tfCode.getText() + ",'" + tfName.getText() + "','" + tfAddress.getText() + "','" + tfCity.getText() + "','" + tfState.getText() + "',"+ tfZip.getText() + ",'" + tfEmail.getText() + "','" + tfLicenseEffectiveDate.getValue() + "','" + expiry_date + "')";
        executeQuery(query);
        showAirlines();
        resetTextField();
    }

    //Computes Expiry Date When License Issue Date and License Duration is input
    private LocalDate convertToExpiry(){
        LocalDate d= tfLicenseEffectiveDate.getValue();
        Integer dur = tfDuration.getValue();
        tfDuration.setValue(dur);
        Calendar cal = new GregorianCalendar();
        cal.set(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
        cal.add(Calendar.MONTH,dur-1);                  //duration - 1 since Calendar Object stores months as 1 index less(0-11)
                                                               //i.e. Month 7 Entered into Calendar will be seen as Month 8 by the Calendar
        LocalDate expiry = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
        return expiry;
    }

    //executes query
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

    @FXML
    //initializer fn
    void initialize() {
        showAirlines();
        tfDuration.setItems(durationList);
    }
}
