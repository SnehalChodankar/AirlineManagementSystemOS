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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

public class airportController {
    private Stage stage;
    private Scene scene;

    public String airlineCode;
    public int operationalStatus;

    public void setAirline(String acode,int opstatus){
        this.airlineCode = acode;
        this.operationalStatus=opstatus;
        //System.out.println("Set Airline:"+airlineCode);
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
    void onBackAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("airline.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

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

    private boolean checkAirportCode(String ACode) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM airport WHERE Airport_Code="+ACode+"";
        Statement st;
        ResultSet rs;
        st = conn.createStatement();
        rs = st.executeQuery(query);

        if(rs.next()) return true;
        else return false;
    }

    private boolean validationCheck(Object src) throws SQLException {
        Alert a = new Alert(Alert.AlertType.WARNING);

        if(src == btnAirportInsert){
            if(Objects.equals(tfCode.getText(), "") || Objects.equals(tfName.getText(), "") || Objects.equals(tfCity.getText(), "") || Objects.equals(tfState.getText(), "") ){
                a.setContentText("Please fill all the fields!!! ");
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

    private void insertAirportRecord() {
        String query = "INSERT INTO airport (`Airport_Code`, `Airport_Name`, `Airport_City`, `Airport_State`) VALUES ('"+ tfCode.getText() + "','" + tfName.getText() + "','" + tfCity.getText() + "','" + tfState.getText() + "')";
        executeQuery(query);
//        if(airlineCode !=0){
//            query = "INSERT INTO `airline_airport_association`(`Airline_Code`, `Airport_Code`) VALUES ('"+ airlineCode + "','" + tfCode.getText() + "')";
//            executeQuery(query);
//        }
        showAirports();
        resetTextField();
    }

    private void updateAirportRecord() {
        String query = "UPDATE airport SET Airport_Name = '"+ tfName.getText() + "', Airport_City = '"+tfCity.getText()+"', Airport_State = '"+tfState.getText()+"' WHERE Airport_Code = '"+tfCode.getText()+"'";
        executeQuery(query);
        showAirports();
        resetTextField();
    }

    private void changeStatusAirportRecord() {
        Airports airport =  tvAirports.getSelectionModel().getSelectedItem();
        int status;
        if(airport.getOp_Status() == 1)
            status = 0;
        else
            status = 1;

        String query = "UPDATE airport SET Operational_Status = "+status+" WHERE Airport_Code = '"+tfCode.getText()+"'";
        executeQuery(query);

//        query = "Update airline_airport_association set Flag = 0 WHERE Airport_Code = '"+tfCode.getText()+"'";
//        executeQuery(query);

        showAirports();
        resetTextField();
    }

    @FXML
    void handleBtnAssociationAction(ActionEvent event) throws SQLException {
        if(event.getSource() == btnAirportInsertAssociation){
            insertAirportAssociationRecord();
        }
        else if(event.getSource() == btnAirportDeleteAssociation){
            deleteAirportAssociationRecord();
        }
    }

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

    private void deleteAirportAssociationRecord() {
        String query = "UPDATE airline_airport_association SET Flag = 0 WHERE Airport_Code = '"+tfCode.getText()+"' AND Airline_Code = '"+airlineCode+"'";
        executeQuery(query);
        showAirports();
    }

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

    public void showAirports(){
        ObservableList<Airports> list = getAirportsList();

        colCode.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_Code"));
        colName.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_Name"));
        colCity.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_City"));
        colState.setCellValueFactory(new PropertyValueFactory<Airports, String>("Airport_State"));
        colOpStatus.setCellValueFactory(new PropertyValueFactory<Airports, Integer>("Op_Status"));
        tvAirports.setItems(list);
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
        //showAirports();
    }

}
