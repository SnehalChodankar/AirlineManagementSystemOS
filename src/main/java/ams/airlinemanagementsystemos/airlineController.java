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
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * airlineController is used to handle various functionalities associated with respect to airline,
 * like for example, insert, update airline details, renew license, change status.
 * airlineController is also used to execute the functiality to go to other pages like, airport page,
 * flights page and passenger details page.
 *
 * Methods defined in airlineController:
 * - onBackAction(ActionEvent event);               returns void
 * - goToAirports(ActionEvent event);               returns void
 * - goToFlights(ActionEvent event);                returns void
 * - goToPassengers(ActionEvent event);             returns void
 * - getConnection();                               returns Connection
 * - getAirlinesList(int status);                   returns ObservableList<Airlines>
 * - showAirlines(int status);                      returns void
 * - resetTextField();                              returns void
 * - handleAirlineCol(MouseEvent event);            returns void
 * - handleBtnIUDAction(ActionEvent event);         returns void
 * - softDeleteAirlineRecord();                     returns void
 * - renewLicense();                                returns void
 * - updateAirlineRecord();                         returns void
 * - insertAirlineRecord();                         returns void
 * - validationCheck(Object src);                   returns boolean
 * - checkAirlineCode(String ACode);                returns boolean
 * - checkAirlineName(String AName);                returns boolean
 * - convertToExpiry();                             returns LocalDate
 * - executeQuery(String query);                    returns void
 * - showInactiveAirlines(ActionEvent event);       returns void
 * - showActiveAirlines(ActionEvent event);         returns void
 * - initialize();                                  returns void
 * */
public class airlineController {
    private Stage stage;
    private Scene scene;

    //Used to fill in the cbDuration dropdown with number of months for license validity.
    ObservableList<Integer> durationList = FXCollections.observableArrayList(6,12,24);

    @FXML
    private AnchorPane apMain;

    @FXML
    private  ImageView ivMain;

    @FXML
    private TableView<Airlines> tvAirlines;

    @FXML
    private TableColumn<Airlines, String> colCode;

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
    private DatePicker dpLicenseEffectiveDate;

    @FXML
    private ChoiceBox<Integer> cbDuration;

    @FXML
    private Button btnAirlineInsert;

    @FXML
    private Button btnAirlineUpdate;

    @FXML
    private Button btnAirlineSoftDelete;

    @FXML
    private Button btnAirlineLicenseRenew;

    @FXML
    private Button btnShowInactive;

    @FXML
    private Button btnShowActive;

    @FXML
    private Button btnAirports;

    @FXML
    private Button btnFlights;

    @FXML
    private Button btnPassengers;

    @FXML
    private Button btnBack;

    /**
     * onBackAction function activates when user clicks on logout button in airline.fxml.
     * This takes the user back to the main.fxml page where user must login again to view airline.fxml.
     * */
    @FXML
    void onBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * goToAirports is initiated when user clicks on Airport button.
     * If an airline is not selected from the table, and user clicks on airport page, he will be
     * redirected to airport page where he can see all the airports that are registered into the system.
     * Once the airline is chosen, it is highlighted in the table, and then when user clicks on Airports
     * button, he will be directed to airport.fxml page where airports which are associated with selected
     * airline are displayed.
     * */
    @FXML
    void goToAirports(ActionEvent event) throws IOException {

        //Used to fetch the details of selected airline row from the table.
        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();

        /**condition checks if the airline object really contains any data inside or not.
         * If the object is null, user didn't select any airline from the table.
         * If the object is not null, user has selected an airline and he can be redirected to airport.fxml
         * */
        if(airline == null){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("airport.fxml"));
            Parent root = loader.load();

            //below two lines are used to pass the info from airline controller to airport controller
            airportController airC = loader.getController();
            airC.setAirline(null,9);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("airport.fxml"));
            Parent root = loader.load();

            //below two lines are used to pass the info from airline controller to airport controller
            airportController airC = loader.getController();
            airC.setAirline(airline.getAirline_Code(),airline.getOperational_Status());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * goToFlights is initiated when user clicks on Flights button.
     * But the user won't be redirected to flight.fxml unless he chooses a specific airline
     * from the table.
     * Once the airline is choosen, it is highlighted in the table, and then when user clicks on Flights
     * button, he will be directed to flight.fxml page.
     * The checks and steps are same as goToAirport function above.
     * */
    @FXML
    void goToFlights(ActionEvent event) throws IOException {
        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();

        if(airline == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Cannot Proceed to Flights!!!");
            a.setContentText("Please select an airline to proceed to flights details.");
            a.show();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("flight.fxml"));
            Parent root = loader.load();

            flightController flightC = loader.getController();
            flightC.setAirline(airline.getAirline_Code(), airline.getAirline_Name());

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * goToPassengers is initiated when the user clicks on Passengers button.
     * Here, the user is not needed to select any airline row from the table to proceed to the
     * passengers page, that is, passenger.fxml.
     * */
    @FXML
    void goToPassengers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Passenger.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * getConnection function is used to connect to the mysql database with the default root credentials.
     * */
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams", "root", "");
            return conn;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * getAirlineList function creates a list of Airline objects which reflect the data
     * from the Airline table.
     * This function returns this list as OberservableList whenever called.
     * */
    public ObservableList<Airlines> getAirlinesList(int status) throws SQLException {
        ObservableList<Airlines> airlineList = FXCollections.observableArrayList();
        Connection conn = getConnection();

        //query for retrieving total number of aircraft owned by each airline
        String FData_query="SELECT COUNT(`Flight_Code`) FROM flight GROUP BY `Carrier_Name` HAVING `Carrier_Name`=`Airline_Name`";

        //query for retrieving total number of Flights active on the current day
        String RData_query="SELECT COUNT(DISTINCT`route`.`Flight_Code`) FROM route,flight" +
                " WHERE route.Flight_Code=flight.Flight_Code AND `Carrier_Name`= `Airline_Name` ";

        //query for retrieving all necessary information to display in GUI of the Airline Page
        String query = "SELECT `Airline_Code`, `Airline_Name`, `Airline_Address`, `Airline_City`, `Airline_State`, `Airline_Zip`, `Airline_Email`," +
                " `License_Issue`, `License_Expiry` ,`Operational_Status`" +
                ",(" + FData_query + ") AS `Total_Flights`" +
                ",(" + RData_query + ") AS `Flights_Today`" +
                " FROM airline WHERE `Operational_Status` = "+status+"";

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Airlines airlines;

            while (rs.next())
            {
                airlines = new Airlines(rs.getString("Airline_Code"), rs.getString("Airline_Name"),
                        rs.getString("Airline_Address"),rs.getString("Airline_City"),
                        rs.getString("Airline_State"), rs.getInt("Airline_Zip"),
                        rs.getString("Airline_Email"), rs.getDate("License_Issue"),
                        rs.getDate("License_Expiry"),rs.getInt("Total_Flights"),
                        rs.getInt("Flights_Today"),rs.getInt("Operational_Status"));

                airlineList.add(airlines);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return airlineList;
    }

    /**
     * showAirlines function is used to fill the table with airline details.
     * */
    public void showAirlines(int status) throws SQLException {
        ObservableList<Airlines> list = getAirlinesList(status);

        colCode.setCellValueFactory(new PropertyValueFactory<Airlines, String>("Airline_Code"));
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

    /**
     * resetTextField is used to reset the textfield to blank after every operation performed.
     * */
    public void resetTextField(){
        tfCode.setText("");
        tfName.setText("");
        tfAddress.setText("");
        tfCity.setText("");
        tfState.setText("");
        tfZip.setText("");
        tfEmail.setText("");
        dpLicenseEffectiveDate.setValue(null);
    }

    /**
     * handleAirlineCol is used to fill in the textfields with the data system gets after user selects
     * a particular airline.
     * */
    @FXML
    void handleAirlineCol(MouseEvent event) {
        Airlines airlines =  tvAirlines.getSelectionModel().getSelectedItem();
        tfCode.setText(""+airlines.getAirline_Code());
        tfName.setText(airlines.getAirline_Name());
        tfAddress.setText(airlines.getAirline_Address());
        tfCity.setText(airlines.getAirline_City());
        tfState.setText(airlines.getAirline_State());
        tfZip.setText(""+airlines.getAirline_Zip());
        tfEmail.setText(airlines.getAirline_Email());
        dpLicenseEffectiveDate.setValue(airlines.getLicense_Issue());
        cbDuration.setValue(airlines.getMonths());
    }

    /**
     * handleBtnIUDAction is initiated whenever the insert/update/renew license/change status
     * buttons are pressed.
     * */
    @FXML
    void handleBtnIUDAction(ActionEvent event) throws SQLException {
        if(validationCheck(event.getSource())) {
            if (event.getSource() == btnAirlineInsert) {
                insertAirlineRecord();
            } else if (event.getSource() == btnAirlineUpdate) {
                updateAirlineRecord();
            } else if(event.getSource() == btnAirlineLicenseRenew){
                renewLicense();
            } else if (event.getSource() == btnAirlineSoftDelete) {
                softDeleteAirlineRecord();
            }
        }
    }

    /**
     * softDeleteAirlineRecord is used to change the operational status column in the
     * database to 0 and vise versa.
     * */
    private void softDeleteAirlineRecord() throws SQLException {

        LocalDate current_date = LocalDate.now();
        LocalDate issue_date = dpLicenseEffectiveDate.getValue();
        LocalDate expiry_date = convertToExpiry();

        if(current_date.isAfter(expiry_date) || current_date.isBefore(issue_date)){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Invalid License");
            a.setContentText("License is not valid,please check License Issue Date & Duration");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.show();
        }
        else {
            String query = "UPDATE airline SET Operational_Status= Operational_Status ^ 1 WHERE Airline_Code = '" + tfCode.getText() + "'";
            executeQuery(query);
            showAirlines(1);
            btnShowActive.setVisible(false);
            btnShowActive.setManaged(false);
            btnShowInactive.setVisible(true);
            btnShowInactive.setManaged(true);
            resetTextField();
        }
    }

    /**
     * renewLicense updates the license issue date and expiry date based on user input
     * */
    private void renewLicense() throws SQLException {
        LocalDate expiry_date = convertToExpiry();
        LocalDate current_date = LocalDate.now();

        if(current_date.isAfter(expiry_date)){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Invalid License");
            a.setContentText("License is no longer valid,please check License Issue Date & Duration");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.show();
        }
        else{
            String query = "UPDATE airline SET License_Issue = '"+ dpLicenseEffectiveDate.getValue() +"', License_Expiry = '"+ expiry_date +"' WHERE Airline_Code = '"+tfCode.getText()+"'";
            executeQuery(query);
            showAirlines(1);
            btnShowActive.setVisible(false);
            btnShowActive.setManaged(false);
            btnShowInactive.setVisible(true);
            btnShowInactive.setManaged(true);
            resetTextField();
        }
    }

    /**
     * updateAirlineRecord is used to update the information related to the selected airline.
     * */
    private void updateAirlineRecord() throws SQLException {
        String query = "UPDATE airline SET Airline_Name = '"+ tfName.getText() + "', Airline_Address = '"+ tfAddress.getText() +"', Airline_City = '"+tfCity.getText()+"', Airline_State = '"+tfState.getText()+"', Airline_Zip = "+ Integer.parseInt(tfZip.getText()) +", Airline_Email = '"+ tfEmail.getText() +"' WHERE Airline_Code = '"+tfCode.getText()+"'";
        executeQuery(query);
        showAirlines(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
        resetTextField();
    }

    /**
     * insertAirlineRecord is used to insert a new airline into the database.
     * */
    private void insertAirlineRecord() throws SQLException {
        LocalDate expiry_date = convertToExpiry();
        LocalDate current_date = LocalDate.now();

        if(current_date.isAfter(expiry_date))
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Invalid License");
            a.setContentText("License is no longer valid,please check License Issue Date & Duration");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.show();
        }

        else {
            String query = "INSERT INTO airline (`Airline_Code`, `Airline_Name`, `Airline_Address`, `Airline_City`, `Airline_State`, `Airline_Zip`, `Airline_Email`, `License_Issue`, `License_Expiry`) VALUES ('" + tfCode.getText() + "','" + tfName.getText() + "','" + tfAddress.getText() + "','" + tfCity.getText() + "','" + tfState.getText() + "'," + tfZip.getText() + ",'" + tfEmail.getText() + "','" + dpLicenseEffectiveDate.getValue() + "','" + expiry_date + "')";
            executeQuery(query);
            showAirlines(1);
            btnShowActive.setVisible(false);
            btnShowActive.setManaged(false);
            btnShowInactive.setVisible(true);
            btnShowInactive.setManaged(true);
            resetTextField();
        }
    }

    /**
     * validationCheck is used to check if all the necessary fields are field by user for a
     * particular function.
     * If not, an alert box is displayed with necessary help text.
     * If any of the conditions does not get satisfied, then function returns false, else returns true.
     * */
    private boolean validationCheck(Object src) throws SQLException {
        Alert a = new Alert(Alert.AlertType.WARNING);

        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();

        boolean isAirlineCode = tfCode.getText().chars().allMatch(Character::isLetterOrDigit);
        Pattern p = Pattern.compile("^[ A-Za-z]+$");
        boolean isAirlineName = p.matcher(tfCity.getText()).matches();
        boolean isCity = p.matcher(tfCity.getText()).matches();
        boolean isState = p.matcher(tfCity.getText()).matches();
        boolean isZip = tfZip.getText().chars().allMatch(Character::isDigit);

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$";
        p = Pattern.compile(emailRegex);
        boolean isEmail = p.matcher(tfEmail.getText()).matches();

        if(src == btnAirlineInsert){

            if(Objects.equals(tfCode.getText(), "") || Objects.equals(tfName.getText(), "") || Objects.equals(tfAddress.getText(), "") || Objects.equals(tfCity.getText(), "") || Objects.equals(tfState.getText(), "") || Objects.equals(tfZip.getText(), "") || Objects.equals(tfEmail.getText(), "") || dpLicenseEffectiveDate.getValue() == null || cbDuration.getValue() == null){
                a.setContentText("Please fill all the fields!!! ");
                a.show();
                return false;
            }
            else if(!isAirlineCode){
                a.setContentText("Invalid Airline Code!!!");
                a.show();
                return false;
            }
            else if(!isAirlineName){
                a.setContentText("Invalid Airline Name!!!");
                a.show();
                return false;
            }
            else if(!isCity){
                a.setContentText("Invalid Airline City!!!");
                a.show();
                return false;
            }
            else if(!isState){
                a.setContentText("Invalid Airline State!!!");
                a.show();
                return false;
            }
            else if(!isZip || tfZip.getText().length()>6){
                a.setContentText("Invalid Airline Zip!!!");
                a.show();
                return false;
            }
            else if(!isEmail){
                a.setContentText("Invalid Airline Email!!!");
                a.show();
                return false;
            }
            else if( checkAirlineCode(tfCode.getText())){
                a.setContentText("Airline Code already exists in the database.");
                a.show();
                return false;
            }
            else if(checkAirlineName(tfName.getText())){
                a.setContentText("Airline Name already exists in the database.");
                a.show();
                return false;
            }

        }
        else if(src == btnAirlineUpdate){
            if(Objects.equals(tfCode.getText(), "") || Objects.equals(tfName.getText(), "") || Objects.equals(tfAddress.getText(), "") || Objects.equals(tfCity.getText(), "") || Objects.equals(tfState.getText(), "") || Objects.equals(tfZip.getText(), "") || Objects.equals(tfEmail.getText(), "")){
                a.setContentText("All fields are required. Check Help for more details.");
                a.show();
                return false;
            }
            else if(!checkAirlineCode(tfCode.getText())){
                a.setContentText("Airline doesn't exists in the database.");
                a.show();
                return false;
            }
            else if(checkAirlineName(tfName.getText())){
                a.setContentText("Airline Name already exists in the database.");
                a.show();
                return false;
            }
            else if(!isCity){
                a.setContentText("Invalid Airline City!!!");
                a.show();
                return false;
            }
            else if(!isState){
                a.setContentText("Invalid Airline State!!!");
                a.show();
                return false;
            }
            else if(!isZip || tfZip.getText().length()>6){
                a.setContentText("Invalid Airline Zip!!!");
                a.show();
                return false;
            }
            else if(!isEmail){
                a.setContentText("Invalid Airline Email!!!");
                a.show();
                return false;
            }
            else if(!Objects.equals(tfCode.getText(), airline.getAirline_Code()) && checkAirlineCode(tfCode.getText())){
                a.setContentText("Airline Code already exists in the database.");
                a.show();
                return false;
            }
            else if(!Objects.equals(tfName.getText(), airline.getAirline_Name()) &&  checkAirlineName(tfName.getText())){
                a.setContentText("Airline Name already exists in the database.");
                a.show();
                return false;
            }
        }
        else if(src == btnAirlineLicenseRenew){
            if(Objects.equals(tfCode.getText(), "") || dpLicenseEffectiveDate.getValue() == null || cbDuration.getValue() == null){
                a.setContentText("Missing Required data!!!.");
                a.show();
                return false;
            }
            else if(!checkAirlineCode(tfCode.getText())){
                a.setContentText("Airline doesn't exists in the database.");
                a.show();
                return false;
            }
        }
        else if(src==btnAirlineSoftDelete){
            if(tfCode.getText().equals("")){
                a.setContentText("Please fill in the Airline Code.");
                a.show();
                return false;
            }
            else if(!checkAirlineCode(tfCode.getText())){
                a.setContentText("Airline doesn't exists in the database.");
                a.show();
                return false;
            }
        }
        return true;
    }

    /**
     * checkAirlineCode is used only to check if user entered Airline code is already present
     * in the database or not.
     * If present, function returns true, else returns false.
     * */
    private boolean checkAirlineCode(String ACode) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM airline WHERE Airline_Code='"+ACode+"'";
        Statement st;
        ResultSet rs;
        st = conn.createStatement();
        rs = st.executeQuery(query);

        if(rs.next()) return true;
        else return false;
    }

    /**
     * checkAirlineName is used only to check if user entered Airline Name is already present
     * in the database or not.
     * If present, function returns true, else returns false.
     * */
    private boolean checkAirlineName(String AName) throws SQLException {
        Airlines airline =  tvAirlines.getSelectionModel().getSelectedItem();
        if(airline == null){
            return false;
        }
        if(Objects.equals(airline.getAirline_Name(), AName)){
            return false;
        }
        else{
            Connection conn = getConnection();
            String query = "SELECT * FROM airline WHERE Airline_Name='"+AName+"'";
            Statement st;
            ResultSet rs;
            st = conn.createStatement();
            rs = st.executeQuery(query);

            if(rs.next()){
                return true;
            }
            else return false;
        }
    }

    /**
     * convertToExpiry is used to calculate the license expiry date based on the issue date and duration.
     * */
    private LocalDate convertToExpiry(){
        LocalDate d= dpLicenseEffectiveDate.getValue();
        Integer dur = cbDuration.getValue();
        cbDuration.setValue(dur);
        Calendar cal = new GregorianCalendar();
        cal.set(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
        cal.add(Calendar.MONTH,dur-1);
        /**
         *duration - 1 since Calendar Object stores months as 1 index less(0-11)
         * i.e. Month 7 Entered into Calendar will be seen as Month 8 by the Calendar
         * */
        LocalDate expiry = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
        return expiry;
    }

    /**
     * executeQuery is used only to execute certain queries passed as parameter to this function.
     * */
    private void executeQuery(String query) throws SQLException {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * showInactiveAirline gets called on click of Show Inactive Airlines Button.
     * It displays the airlines with Operational_Status as zero.
     * */
    @FXML
    void showInactiveAirlines(ActionEvent event) throws SQLException {
        showAirlines(0);
        btnShowActive.setVisible(true);
        btnShowActive.setManaged(true);
        btnShowInactive.setVisible(false);
        btnShowInactive.setManaged(false);
    }

    /**
     * showActiveAirline gets called on click of Show Active Airlines Button.
     * It displays the airlines with Operational_Status as one.
     * */
    @FXML
    void showActiveAirlines(ActionEvent event) throws SQLException {
        showAirlines(1);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);
        btnShowInactive.setVisible(true);
        btnShowInactive.setManaged(true);
    }

    /**
     * initialize method contains ivMain and tooltip information to be set as soon as airline.fxml
     * is loaded.
     * */
    @FXML
    void initialize() throws SQLException {
        ivMain.fitWidthProperty().bind(apMain.widthProperty());
        ivMain.fitHeightProperty().bind(apMain.heightProperty());
        Tooltip tairport = new Tooltip("Displays selected airline association only if particular airline record selected \nElse displays all airports.");
        btnAirports.setTooltip(tairport);
        Tooltip tFlights = new Tooltip("Displays flight details of the selected airline.");
        btnFlights.setTooltip(tFlights);
        Tooltip tPassengers = new Tooltip("Displays the records of Passengers.");
        btnPassengers.setTooltip(tPassengers);
        Tooltip tInactive = new Tooltip("Displays list of all the inactive airlines.");
        btnShowInactive.setTooltip(tInactive);
        Tooltip tairlineInsert = new Tooltip("Inserts an airline record.");
        btnAirlineInsert.setTooltip(tairlineInsert);
        Tooltip tairlineUpdate = new Tooltip("Updates an airline record.");
        btnAirlineUpdate.setTooltip(tairlineUpdate);
        Tooltip tairlineRenewLicense = new Tooltip("Renews the license of the selected airline.");
        btnAirlineLicenseRenew.setTooltip(tairlineRenewLicense);
        Tooltip tStatus = new Tooltip("Changes the status of the selected airline.");
        btnAirlineSoftDelete.setTooltip(tStatus);
        Tooltip tBack = new Tooltip("Goes back to the previous page");
        btnBack.setTooltip(tBack);
        Tooltip taActive = new Tooltip("Displays list of all the active airlines.");
        btnShowActive.setTooltip(taActive);
        showAirlines(1);
        cbDuration.setItems(durationList);
        btnShowActive.setVisible(false);
        btnShowActive.setManaged(false);

    }
}
