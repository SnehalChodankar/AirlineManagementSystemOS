package ams.airlinemanagementsystemos;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
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

public class reservationController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int routeId;
    private int reservationId;

    ObservableList<String> flightClasses = FXCollections.observableArrayList("Business", "FirstClass", "Economics");

    @FXML
    private TableView<Reservations> tvReservations;

    @FXML
    private TableColumn<Reservations, Integer> colCode;

    @FXML
    private TableColumn<Reservations, Integer> colPassengerId;

    @FXML
    private TableColumn<Reservations, Integer> colRouteId;

    @FXML
    private TableColumn<Reservations, String> colClass;

    @FXML
    private TableColumn<Reservations, Float> colFare;

    @FXML
    private TextField tfPassengerid;

    @FXML
    private ChoiceBox<String> cbDepartureAirport;

    @FXML
    private ChoiceBox<String> cbArrivalAirport;

    @FXML
    private ChoiceBox<String> cbAirline;

    @FXML
    private Button btnCheckFlights;

    @FXML
    private TextField tfFlightCode;

    @FXML
    private TextField tfDepartureTime;

    @FXML
    private TextField tfArrivalTime;

    @FXML
    private ChoiceBox<String> cbClass;

    @FXML
    private TextField tfFare;

    @FXML
    private Button btnReservationInsert;

    @FXML
    private Button btnReservationUpdate;

    @FXML
    private Button btnReservationCancel;

    @FXML
    void handleIUDAction(ActionEvent event) {
        if(validationCheck(event.getSource())){
            if(event.getSource() == btnReservationInsert){
                insertReservationRecord();
            }
            else if(event.getSource() == btnReservationCancel){
                cancelReservationRecord();
            }
        }
    }

    private boolean validationCheck(Object source) {
        Alert a = new Alert(Alert.AlertType.ERROR);

        boolean isPassengerId = tfPassengerid.getText().chars().allMatch(Character::isDigit);


        if(source == btnReservationInsert){
            if(Objects.equals(tfPassengerid.getText(), "") || cbDepartureAirport.getValue() == null || cbArrivalAirport.getValue() == null || Objects.equals(tfFlightCode.getText(), "") || Objects.equals(tfDepartureTime.getText(), "") || Objects.equals(tfArrivalTime.getText(), "") || cbClass.getValue() == null || Objects.equals(tfFare.getText(), "")){
                a.setHeaderText("Missing Values!!!");
                a.setContentText("All fields are required, please fill all details.");
                a.show();
                return false;
            }
            else if(!checkPassengerId(tfPassengerid.getText())){
                a.setContentText("Passenger Id not registered in database!!!");
                a.show();
                return false;
            }
            else if(!isPassengerId){
                a.setContentText("Invalid Passenger Id!!!");
                a.show();
                return false;
            }
        }
        else if(source == btnReservationCancel){
            Reservations reservation = tvReservations.getSelectionModel().getSelectedItem();
            if(reservation == null){
                a.setContentText("Please select an try from the table first!!!");
                a.show();
                return false;
            }
        }
        return true;
    }

    private boolean checkPassengerId(String passId) {
        String query = "SELECT * FROM passenger WHERE Passenger_Code="+passId+"";
        Connection conn = getConnection();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(rs.next())
                return true;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
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

    private void insertReservationRecord() {
        String query = "INSERT INTO `reservation`(`Passenger_Code`, `Route_ID`, `Class`, `Fare`)VALUES ("+tfPassengerid.getText()+", "+routeId+", '"+cbClass.getValue()+"', '"+tfFare.getText()+"')";
        executeQuery(query);

        showReservations();
    }

    private void cancelReservationRecord() {
        String query = "UPDATE reservation SET Reservation_Status=0 WHERE Reservation_Code="+reservationId+"";
        executeQuery(query);
        showReservations();
    }

    @FXML
    void checkFlightsAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("reservationRoutes.fxml"));
        Parent root = loader.load();

        reservationRoutesController rrc = loader.getController();
        rrc.setAirlineCode(this, cbDepartureAirport.getValue(), cbArrivalAirport.getValue(), cbAirline.getValue());

        Stage stage = new Stage();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("passenger.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showReservations() {
        ObservableList<Reservations> list = getReservationList();

        colCode.setCellValueFactory(new PropertyValueFactory<Reservations, Integer>("Reservation_Code"));
        colPassengerId.setCellValueFactory(new PropertyValueFactory<Reservations, Integer>("Passenger_Code"));
        colRouteId.setCellValueFactory(new PropertyValueFactory<Reservations, Integer>("Route_ID"));
        colClass.setCellValueFactory(new PropertyValueFactory<Reservations, String>("FlightClass"));
        colFare.setCellValueFactory(new PropertyValueFactory<Reservations, Float>("Fare"));

        tvReservations.setItems(list);
    }

    private ObservableList<Reservations> getReservationList() {
        ObservableList<Reservations> reservationList = FXCollections.observableArrayList();
        Connection conn = getConnection();

        String query = "SELECT * FROM reservation WHERE Reservation_Status=1";

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Reservations reservations;

            while (rs.next())
            {
                reservations = new Reservations(rs.getInt("Reservation_Code"), rs.getInt("Passenger_Code"), rs.getInt("Route_ID"), rs.getString("Class"), rs.getFloat("Fare"), rs.getInt("Reservation_Status"));
                reservationList.add(reservations);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return reservationList;
    }

    private Connection getConnection() {
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

    @FXML
    void handleReservationCol(MouseEvent event){
        Reservations reservation = tvReservations.getSelectionModel().getSelectedItem();
        //System.out.println(reservation.getFlightClass());
        tfPassengerid.setText(""+reservation.getPassenger_Code());
        cbClass.setValue(reservation.getFlightClass());
        tfFare.setText(""+reservation.getFare());
        reservationId = reservation.getReservation_Code();

        setRouteDetails(reservation);
    }

    private void setRouteDetails(Reservations reservation) {
        String flightCode="";

        String query = "SELECT * FROM route WHERE Route_ID="+reservation.getRoute_ID()+"";
        Statement st;
        ResultSet rs;
        Connection conn = getConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            if(rs.next()){
                cbDepartureAirport.setValue(rs.getString("Departure_Airport"));
                cbArrivalAirport.setValue(rs.getString("Arrival_Airport"));
                tfFlightCode.setText(rs.getString("Flight_Code"));
                tfDepartureTime.setText(rs.getString("Departure_Time"));
                tfArrivalTime.setText(rs.getString("Arrival_Time"));

                flightCode = rs.getString("Flight_Code");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        setAirline(flightCode);
    }

    private void setAirline(String flightCode) {
        String query = "SELECT Carrier_Name FROM flight WHERE Flight_Code='"+flightCode+"'";
        Statement st;
        ResultSet rs;
        Connection conn = getConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            if(rs.next()){
                cbAirline.setValue(rs.getString("Carrier_Name"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void handleCalcFare(ActionEvent event){
        if(cbAirline.getValue() == null || cbClass.getValue() == null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Invalid Airline/ Class");
            a.setContentText("Please select airline and Class to calculate fare.");
            a.show();
        }
        else{
            String flightClass = cbClass.getValue();
            System.out.println(flightClass);
            String query = "SELECT "+flightClass+" FROM slab_price WHERE Airline_Code IN (SELECT Airline_Code FROM airline WHERE Airline_Name='"+cbAirline.getValue()+"')";
            Connection conn = getConnection();
            try{
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query);

                if(rs.next()){
                    System.out.println("Inside rs.next");
                    tfFare.setText(rs.getString(flightClass));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        showReservations();

        fillChoiceBoxes();

        cbClass.setItems(flightClasses);
    }

    private void fillChoiceBoxes() {
        Connection conn = getConnection();
        String query = "SELECT Airport_Code FROM airport WHERE Operational_Status=1";

        Statement st;
        ResultSet rs;

        ArrayList<String> result = new ArrayList<String>();

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            while (rs.next())
            {
                result.add(rs.getString("Airport_Code"));
            }
            ObservableList<String> airportList = FXCollections.observableArrayList(result);
            cbDepartureAirport.setItems(airportList);
            cbArrivalAirport.setItems(airportList);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        query = "SELECT Airline_Name FROM airline WHERE Operational_Status=1";
        ArrayList<String> resultAirline = new ArrayList<String>();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                resultAirline.add(rs.getString("Airline_Name"));
            }
            ObservableList<String> airportList = FXCollections.observableArrayList(resultAirline);
            cbAirline.setItems(airportList);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void setRoute(Routes route) {
        tfFlightCode.setText(route.getFlight_Code());
        tfDepartureTime.setText(""+route.getDeparture_Date());
        tfArrivalTime.setText(""+route.getArrival_Date());

        routeId = route.getRoute_ID();
    }
}
