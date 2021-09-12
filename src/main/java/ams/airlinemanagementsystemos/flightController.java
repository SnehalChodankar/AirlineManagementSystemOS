package ams.airlinemanagementsystemos;

import javafx.beans.value.ObservableValue;
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
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class flightController {
    private Stage stage;
    private Scene scene;
    private int routeId;


    @FXML
    private TableView<Flights> tvFlights;

    @FXML
    private TableColumn<Flights, String> colCode;

    @FXML
    private TableColumn<Flights, String> colMake;

    @FXML
    private TableColumn<Flights , String> colModel;

    @FXML
    private TableColumn<Flights, Integer> colCapacity;

    @FXML
    private TableColumn<Flights, String> colCarrierName;

    @FXML
    private TextField tfCode;

    @FXML
    private TextField tfMake;

    @FXML
    private TextField tfModel;

    @FXML
    private TextField tfCapacity;

    @FXML
    private Button btnFlightInsert;

    @FXML
    private Button btnFlightUpdate;

    @FXML
    private Button btnFlightSoftDelete;

    @FXML
    private ChoiceBox<String> cbDepartsFrom;

    @FXML
    private ChoiceBox<Integer> cbDepartureHour;

    @FXML
    private ChoiceBox<Integer> cbDepartureMinute;

    @FXML
    private ChoiceBox<String> cbDepartureTime;

    @FXML
    private ChoiceBox<String> cbArrrivesAt;

    @FXML
    private ChoiceBox<Integer> cbArrivalHour;

    @FXML
    private ChoiceBox<Integer> cbArrivalMinute;

    @FXML
    private ChoiceBox<String> cbArrivalTime;

    @FXML
    private Button btnRouteInsert;

    @FXML
    private Button btnRouteUpdate;

    @FXML
    private Button btnRouteSoftDelete;

    @FXML
    private Button btnShowActive;

    @FXML
    private Button btnShowInactive;

    //ObservableList<String> departsFromList;

    public String airlineCode;
    public String airlineName;

    public void setAirline(String acode, String aname){
        this.airlineCode = acode;
        this.airlineName = aname;
        System.out.println(airlineCode);

        showFlights(1);

        setAirports();

        ObservableList<Integer> hours = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12);
        cbDepartureHour.setItems(hours);
        cbArrivalHour.setItems(hours);

        ObservableList<Integer> minutes = FXCollections.observableArrayList(0,5,10,15,20,25,30,35,40,45,50,55);
        cbDepartureMinute.setItems(minutes);
        cbArrivalMinute.setItems(minutes);

        ObservableList<String> time = FXCollections.observableArrayList("AM", "PM");
        cbDepartureTime.setItems(time);
        cbArrivalTime.setItems(time);
    }

    private void setAirports() {
        Connection conn = getConnection();
        String query = "SELECT Airport_Name FROM airport WHERE Airport_Code IN (SELECT Airport_Code FROM airline_airport_association WHERE Airline_Code='"+airlineCode+"' AND Flag=1)";

        Statement st;
        ResultSet rs;

        ArrayList<String> result = new ArrayList<String>();

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            while (rs.next())
            {
                result.add(rs.getString("Airport_Name"));
            }
            ObservableList<String> airportList1 = FXCollections.observableArrayList(result);
            ObservableList<String> airportList2 = FXCollections.observableArrayList(result);
            Collections.sort(airportList1);
            Collections.sort(airportList2);

            cbDepartsFrom.setItems(airportList1);
            cbArrrivesAt.setItems(airportList2);

            /**Event listener for selection of depart airport**/
            cbDepartsFrom.getSelectionModel()
                    .selectedItemProperty()
                    .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                        for (String x:cbDepartsFrom.getItems()) {
                            if(!cbArrrivesAt.getItems().contains(x)){
                                cbArrrivesAt.getItems().add(x);
                            }
                        }
                        if (cbArrrivesAt.getValue()==newValue){
                            cbArrrivesAt.setValue(null);
                        }
                        cbArrrivesAt.getItems().sort(Comparator.naturalOrder());
                        cbArrrivesAt.getItems().remove(newValue);
                    } );
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setRoute(Routes route)
    {
        routeId = route.getRoute_ID();
        String query = "SELECT Airport_Name FROM airport WHERE Airport_Code='"+route.getDeparture_Airport()+"'";
        Connection conn = getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(rs.next()){
                cbDepartsFrom.setValue(rs.getString("Airport_Name"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        String query2 = "SELECT Airport_Name FROM airport WHERE Airport_Code='"+route.getArrival_Airport()+"'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query2);

            if(rs.next()){
                cbArrrivesAt.setValue(rs.getString("Airport_Name"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        int hour = route.getDeparture_Date().getHours();
        int minute = route.getDeparture_Date().getMinutes();
        String time= "AM";

        if(hour==00)
        {
            hour = hour+12;
        }
        else if(hour>=12){
            time="PM";
            if(hour>12){
                hour = hour-12;
            }
        }

        cbDepartureHour.setValue(hour);
        cbDepartureMinute.setValue(minute);
        cbDepartureTime.setValue(time);

        hour = route.getArrival_Date().getHours();
        minute = route.getArrival_Date().getMinutes();
        time = "AM";

        if(hour==00)
        {
            hour = hour+12;
        }
        else if(hour>=12){
            time="PM";
            if(hour>12){
                hour = hour-12;
            }
        }

        cbArrivalHour.setValue(hour);
        cbArrivalMinute.setValue(minute);
        cbArrivalTime.setValue(time);
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
    void handleFlightCol(MouseEvent event) throws IOException {
        Flights flight = tvFlights.getSelectionModel().getSelectedItem();

        tfCode.setText(flight.getFlight_Code());
        tfModel.setText(flight.getFlight_Model());
        tfMake.setText(flight.getFlight_Make());
        tfCapacity.setText(""+flight.getFlight_Capacity());

        if(event.getClickCount()==2 && flight != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("route.fxml"));
            Parent root = loader.load();

            routeController rc = loader.getController();
            rc.setFlightCode(flight.getFlight_Code(),this);

            Stage stage = new Stage();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    private void resetTextField() {
        tfCode.setText("");
        tfModel.setText("");
        tfMake.setText("");
        tfCapacity.setText("");

        cbDepartsFrom.setValue(null);
        cbDepartureHour.setValue(null);
        cbDepartureMinute.setValue(null);
        cbDepartureTime.setValue(null);
        cbArrrivesAt.setValue(null);
        cbArrivalHour.setValue(null);
        cbArrivalMinute.setValue(null);
        cbArrivalTime.setValue(null);
    }



    @FXML
    void handleBtnIUDAction(ActionEvent event) throws SQLException {
        if(validationCheck(event.getSource())) {
            if (event.getSource() == btnFlightInsert) {
                insertFlightRecord();
            } else if (event.getSource() == btnFlightUpdate) {
                updateFlightRecord();
            } else if (event.getSource() == btnFlightSoftDelete) {
                softDeleteFlightRecord();
            }
        }
    }

    private boolean validationCheck(Object source) throws SQLException {
        Flights flights=tvFlights.getSelectionModel().getSelectedItem();

        Alert a = new Alert(Alert.AlertType.WARNING);

        if(source == btnFlightInsert){
            boolean isFlightCode = tfCode.getText().chars().allMatch(Character::isLetterOrDigit);
            boolean isMake = tfMake.getText().chars().allMatch(Character::isLetterOrDigit);
            boolean isModel = tfModel.getText().chars().allMatch(Character::isLetterOrDigit);
            boolean isCapacity = tfCapacity.getText().chars().allMatch(Character::isDigit);

            if(Objects.equals(tfCode.getText(), "") || Objects.equals(tfMake.getText(), "") || Objects.equals(tfModel.getText(), "") || Objects.equals(tfCapacity.getText(), "") ){
                a.setContentText("Please fill all the fields!!! ");
                a.show();
                return false;
            }
            else if(!isFlightCode){
                a.setContentText("Invalid Flight Code!!!");
                a.show();
                return false;
            }
            else if(!isMake){
                a.setContentText("Invalid Flight Make!!!");
                a.show();
                return false;
            }
            else if(!isModel){
                a.setContentText("Invalid Flight Model!!!");
                a.show();
                return false;
            }
            else if(!isCapacity){
                a.setContentText("Invalid Flight Capacity!!!");
                a.show();
                return false;
            }
            else if(checkFlightCode(tfCode.getText())){
                a.setContentText("Flight already exists in the database.");
                a.show();
                return false;
            }
        }
        else if(source == btnFlightUpdate){
            if(Objects.equals(tfCode.getText(), "") || Objects.equals(tfMake.getText(), "") || Objects.equals(tfModel.getText(), "") || Objects.equals(tfCapacity.getText(), "") ){
                a.setContentText("Please fill all the fields!!! ");
                a.show();
                return false;
            }
            else if(!checkFlightCode(tfCode.getText())){
                a.setContentText("Flight doesn't exists in the database.");
                a.show();
                return false;
            }
            else if(!Objects.equals(flights.getFlight_Code(),tfCode.getText()) && checkFlightCode(tfCode.getText())){
                a.setContentText("Flight already exists in the database.");
                a.show();
                return false;
            }
        }
        return true;
    }

    private boolean checkFlightCode(String FCode) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM flight WHERE Flight_Code='"+FCode+"'";
        Statement st;
        ResultSet rs;
        st = conn.createStatement();
        rs = st.executeQuery(query);

        if(rs.next()) return true;
        else return false;
    }

    private void insertFlightRecord() {
        //System.out.println(airlineName);
        String query = "INSERT INTO `flight`(`Flight_Code`, `Flight_Capacity`, `Carrier_Name`, `Flight_Make`, `Flight_Model`)VALUES ('"+tfCode.getText()+"', "+tfCapacity.getText()+", '"+airlineName+"', '"+tfMake.getText()+"', '"+tfModel.getText()+"')";
        executeQuery(query);

        showFlights(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
        resetTextField();
    }

    private void updateFlightRecord() {
        String query = "UPDATE flight SET Flight_Capacity="+tfCapacity.getText()+", Flight_Make='"+tfMake.getText()+"', Flight_Model='"+tfModel.getText()+"' WHERE Flight_Code='"+tfCode.getText()+"'";
        executeQuery(query);

        showFlights(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
        resetTextField();
    }

    private void softDeleteFlightRecord() {
        if(Objects.equals(tfCode.getText(), "")){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Flight not Selected!!!");
            a.setContentText("Please select a flight to change the status");
            a.show();
        }
        String query = "UPDATE flight SET Availability=Availability ^ 1 WHERE Flight_Code = '"+tfCode.getText()+"'";
        executeQuery(query);
        showFlights(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
        resetTextField();
    }

    @FXML
    void handleBtnIUDActionRoutes(ActionEvent event) throws ParseException, SQLException {
        if(event.getSource()==btnRouteInsert){
            insertRoute();
        }
        else if(event.getSource()==btnRouteUpdate){
            updateRoute();
        }
        else if(event.getSource()==btnRouteSoftDelete){
            softDeleteRoute();
        }
    }

    private void insertRoute() throws ParseException, SQLException {
        if(Objects.equals(tfCode.getText(), "")){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Flight not Selected!!!");
            a.setContentText("Please select a flight to insert route.");
            a.show();
        }
        else if(cbDepartsFrom.getValue()==null || cbArrivalHour.getValue()==null || cbArrivalMinute.getValue()==null || cbArrivalTime.getValue()==null || cbArrrivesAt.getValue()==null || cbDepartureHour.getValue()==null || cbDepartureMinute.getValue()==null || cbDepartureTime.getValue()==null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Route error!!!");
            a.setContentText("Please check route details");
            a.show();
        }
        else if(cbArrivalHour.getValue()==cbDepartureHour.getValue() && cbArrivalMinute.getValue()==cbDepartureMinute.getValue() && cbArrivalTime.getValue()==cbDepartureTime.getValue()){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Invalid time!!!");
            a.setContentText("Please check Departure and arrival time");
            a.show();
        }
        else {
            int hour = cbDepartureHour.getValue();
            int minute = cbDepartureMinute.getValue();
            String time = cbDepartureTime.getValue();

            if (Objects.equals(time, "PM") && hour != 12) {
                hour = hour + 12;
            }
            if (Objects.equals(time, "AM") && hour == 12) {
                hour = hour - 12;
            }
            String dept_time = hour + ":" + minute + ":00";
            java.sql.Time departureTime = java.sql.Time.valueOf(dept_time);

            hour = cbArrivalHour.getValue();
            minute = cbArrivalMinute.getValue();
            time = cbArrivalTime.getValue();

            if (Objects.equals(time, "PM") && hour != 12) {
                hour = hour + 12;
            }
            if (Objects.equals(time, "AM") && hour == 12) {
                hour = hour - 12;
            }

            String arr_time = hour + ":" + minute + ":00";
            java.sql.Time arrivalTime = java.sql.Time.valueOf(arr_time);

            if (checkRoute(tfCode.getText(), departureTime)) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Invalid time!!!");
                a.setContentText("Departure time clashes with current schedule");
                a.show();
            } else {
                String departureAirport = getAirportCode(cbDepartsFrom.getValue());
                String arrivalAirport = getAirportCode(cbArrrivesAt.getValue());
                //System.out.println(departureAirport);
                //System.out.println(arrivalAirport);

                String query = "INSERT INTO route (`Flight_Code`, `Departure_Time`, `Departure_Airport`, `Arrival_Time`, `Arrival_Airport`) VALUES ('" + tfCode.getText() + "', '" + departureTime + "', '" + departureAirport + "', '" + arrivalTime + "', '" + arrivalAirport + "')";
                executeQuery(query);
                showFlights(1);
            }
        }
    }

    private void updateRoute() throws SQLException {
        if(Objects.equals(tfCode.getText(), "")){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Flight not Selected!!!");
            a.setContentText("Please select a flight to update route.");
            a.show();
        }
        else if(cbDepartsFrom.getValue()==null || cbArrivalHour.getValue()==null || cbArrivalMinute.getValue()==null || cbArrivalTime.getValue()==null || cbArrrivesAt.getValue()==null || cbDepartureHour.getValue()==null || cbDepartureMinute.getValue()==null || cbDepartureTime.getValue()==null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Route error!!!");
            a.setContentText("Please check route details");
            a.show();
        }

        else if(cbArrivalHour.getValue()==cbDepartureHour.getValue() && cbArrivalMinute.getValue()==cbDepartureMinute.getValue() && cbArrivalTime.getValue()==cbDepartureTime.getValue()){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Invalid time!!!");
            a.setContentText("Please check Departure and arrival time");
            a.show();
        }

        else{
            int hour = cbDepartureHour.getValue();
            int minute = cbDepartureMinute.getValue();
            String time = cbDepartureTime.getValue();

            if(Objects.equals(time, "PM") && hour!=12){
                hour = hour+12;
            }
            if(Objects.equals(time, "AM") && hour==12){
                hour = hour-12;
            }
            String dept_time = hour+":"+minute+":00";
            java.sql.Time departureTime = java.sql.Time.valueOf(dept_time);

            hour = cbArrivalHour.getValue();
            minute = cbArrivalMinute.getValue();
            time = cbArrivalTime.getValue();

            if(Objects.equals(time, "PM") && hour!=12){
                hour = hour+12;
            }
            if(Objects.equals(time, "AM") && hour==12){
                hour = hour-12;
            }

            String arr_time = hour+":"+minute+":00";
            java.sql.Time arrivalTime = java.sql.Time.valueOf(arr_time);

            if(checkRoute(tfCode.getText(),departureTime)){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Invalid time!!!");
                a.setContentText("Departure time clashes with current schedule");
                a.show();
            }

            else{
                String departureAirport = getAirportCode(cbDepartsFrom.getValue());
                String arrivalAirport = getAirportCode(cbArrrivesAt.getValue());

                String query = "UPDATE route SET Departure_Time='" + departureTime + "', Departure_Airport='" + departureAirport + "', Arrival_Time='" + arrivalTime + "', Arrival_Airport='" + arrivalAirport + "' WHERE Route_ID=" + routeId + "";
                executeQuery(query);
                showFlights(1);
            }
        }
    }

    private void softDeleteRoute() {
        if(Objects.equals(tfCode.getText(), "")){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Flight not Selected!!!");
            a.setContentText("Please select a flight for route selection");
            a.show();
        }
        else if(cbDepartsFrom.getValue()==null || cbArrivalHour.getValue()==null || cbArrivalMinute.getValue()==null || cbArrivalTime.getValue()==null || cbArrrivesAt.getValue()==null || cbDepartureHour.getValue()==null || cbDepartureMinute.getValue()==null || cbDepartureTime.getValue()==null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Route error!!!");
            a.setContentText("Please select a route");
            a.show();
        }
        else{
            String query = "UPDATE route SET Route_Status=Route_Status ^ 1 WHERE Route_ID="+routeId+"";
            executeQuery(query);
            showFlights(1);
        }
    }

    private boolean checkRoute(String FCode,java.sql.Time DTime) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM route WHERE Flight_Code='"+FCode+"'";
        Statement st;
        ResultSet rs;
        st = conn.createStatement();
        rs = st.executeQuery(query);

        while (rs.next())
        {
            if((DTime.after(rs.getTime("Departure_Time")) || DTime.equals(rs.getTime("Departure_Time"))) && (DTime.before(rs.getTime("Arrival_Time")) || DTime.equals(rs.getTime("Arrival_Time"))))
            {
                return true;
            }
        }

        return false;
    }


    private String getAirportCode(String Aname){
        String query = "SELECT Airport_Code FROM airport WHERE Airport_Name='"+Aname+"'";
        Connection conn = getConnection();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(rs.next()){
                return rs.getString("Airport_Code");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
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

    @FXML
    void showActiveFlights(ActionEvent event) {
        showFlights(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
    }

    @FXML
    void showInactiveFlights(ActionEvent event) {
        showFlights(0);
        btnShowActive.setVisible(true);
        btnShowActive.setManaged(true);
        btnShowInactive.setVisible(false);
        btnShowInactive.setManaged(false);
    }

    @FXML
    void initialize() {
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
    }

    private void showFlights(int availability) {
        ObservableList<Flights> list = getFlightsList(availability);

        colCode.setCellValueFactory(new PropertyValueFactory<Flights, String>("Flight_Code"));
        colMake.setCellValueFactory(new PropertyValueFactory<Flights, String>("Flight_Make"));
        colModel.setCellValueFactory(new PropertyValueFactory<Flights, String>("Flight_Model"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<Flights, Integer>("Flight_Capacity"));
        colCarrierName.setCellValueFactory(new PropertyValueFactory<Flights, String>("Carrier_Name"));

        tvFlights.setItems(list);
    }

    private ObservableList<Flights> getFlightsList(int availability) {
        ObservableList<Flights> flightsList = FXCollections.observableArrayList();
        Connection conn = getConnection();

        String query = "SELECT * FROM flight WHERE Carrier_Name='"+airlineName+"' AND Availability="+availability+"";

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Flights flights;

            while (rs.next())
            {
                flights = new Flights(rs.getString("Flight_Code"), rs.getInt("Flight_Capacity"), rs.getString("Carrier_Name"), rs.getString("Flight_Make"), rs.getString("Flight_Model"), rs.getInt("Availability"));

                flightsList.add(flights);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return flightsList;
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
}
