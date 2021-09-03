package ams.airlinemanagementsystemos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class airportController {
    private Stage stage;
    private Scene scene;

    public int airlineCode;

    public void setAirline(int acode){
        this.airlineCode = acode;
        System.out.println(airlineCode);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private Button btnAirportDelete;

    @FXML
    void onBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("airline.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBtnIUDAction(ActionEvent event) {
        if(event.getSource() == btnAirportInsert){
            insertAirportRecord();
        }
        else if(event.getSource() == btnAirportUpdate){
            updateAirportRecord();
        }
        else if(event.getSource() == btnAirportDelete){
            deleteAirportRecord();
        }
    }

    public ObservableList<Airports> getAirportsList(){
        ObservableList<Airports> airportList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM airport WHERE Airport_Code IN (SELECT Airport_Code FROM airline_airport_association WHERE Airline_Code='" + airlineCode +"' AND Flag=1)" ;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Airports airports;

            while (rs.next())
            {
                airports = new Airports(rs.getString("Airport_Code"), rs.getString("Airport_Name"), rs.getString("Airport_City"),rs.getString("Airport_State"));
                airportList.add(airports);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return airportList;
    }

    public void showAirports(){
        ObservableList<Airports> list = getAirportsList();

        colCode.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_Code"));
        colName.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_Name"));
        colCity.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_City"));
        colState.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_State"));
        tvAirports.setItems(list);
    }

    private void deleteAirportRecord() {
        String query = "Update airport set Operational_Status = 0 WHERE Airport_Code = '"+tfCode.getText()+"'";
        executeQuery(query);
        query = "Update airline_airport_association set Flag = 0 WHERE Airport_Code = '"+tfCode.getText()+"'";
        executeQuery(query);
        showAirports();
        resetTextField();
    }

    private void updateAirportRecord() {
        String query = "UPDATE airport SET Airport_Name = '"+ tfName.getText() + "', Airport_City = '"+tfCity.getText()+"', Airport_State = '"+tfState.getText()+"' WHERE Airport_Code = '"+tfCode.getText()+"'";
        executeQuery(query);
        showAirports();
        resetTextField();
    }

    private void insertAirportRecord() {
        String query = "INSERT INTO airport(`Airport_Code`, `Airport_Name`, `Airport_City`, `Airport_State`) VALUES ('"+ tfCode.getText() + "','" + tfName.getText() + "','" + tfCity.getText() + "','" + tfState.getText() + "')";
        executeQuery(query);
        query = "INSERT INTO `airline_airport_association`(`Airline_Code`, `Airport_Code`) VALUES ('"+ airlineCode + "','" + tfCode.getText() + "')";
        executeQuery(query);
        showAirports();
        resetTextField();
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
    //Autofill data in the respective fields when an entry is clicked in the table
    void handleAirportCol(MouseEvent event) {
        Airports airports =  tvAirports.getSelectionModel().getSelectedItem();
        tfCode.setText(""+airports.getAirport_Code());
        tfName.setText(airports.getAirport_Name());
        tfCity.setText(airports.getAirport_City());
        tfState.setText(airports.getAirport_State());
    }

    public void resetTextField(){
        tfCode.setText("");
        tfName.setText("");
        tfCity.setText("");
        tfState.setText("");
    }

    @FXML
    void initialize() {
        showAirports();
    }

}
