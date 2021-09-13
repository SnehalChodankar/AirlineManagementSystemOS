package ams.airlinemanagementsystemos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

public class routeController {
    public String flightCode;

    flightController fc;

    public void setFlightCode(String fcode,flightController fc){
        this.flightCode = fcode;
        this.fc=fc;
        showRoutes();
    }



    @FXML
    private TableView<Routes> tvRoutes;

    @FXML
    private TableColumn<Routes, String> colFlightCode;

    @FXML
    private TableColumn<Routes, Integer> colRouteId;

    @FXML
    private TableColumn<Routes, LocalTime> colDeptartureDate;

    @FXML
    private TableColumn<Routes, String> colDepartureAirport;

    @FXML
    private TableColumn<Routes, LocalTime> colArrivalDate;

    @FXML
    private TableColumn<Routes, String> colArrivalAirport;

    @FXML
    private TableColumn<Routes, Integer> colRouteStatus;

    private void showRoutes() {
        ObservableList<Routes> list = getRoutesList();

        colFlightCode.setCellValueFactory(new PropertyValueFactory<Routes, String>("Flight_Code"));
        colRouteId.setCellValueFactory(new PropertyValueFactory<Routes, Integer>("Route_ID"));
        colDeptartureDate.setCellValueFactory(new PropertyValueFactory<Routes, LocalTime>("Departure_Date"));
        colDepartureAirport.setCellValueFactory(new PropertyValueFactory<Routes, String>("Departure_Airport"));
        colArrivalDate.setCellValueFactory(new PropertyValueFactory<Routes, LocalTime>("Arrival_Date"));
        colArrivalAirport.setCellValueFactory(new PropertyValueFactory<Routes, String>("Arrival_Airport"));
        colRouteStatus.setCellValueFactory(new PropertyValueFactory<Routes, Integer>("Route_Status"));

        tvRoutes.setItems(list);
    }

    private ObservableList<Routes> getRoutesList() {
        ObservableList<Routes> routesList = FXCollections.observableArrayList();
        Connection conn = getConnection();

        String query = "SELECT * FROM route WHERE Flight_Code='"+flightCode+"'";

        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Routes routes;

            while (rs.next())
            {
                routes = new Routes(rs.getInt("Route_ID"), rs.getString("Flight_Code"), rs.getTime("Departure_Time"), rs.getString("Departure_Airport"), rs.getTime("Arrival_Time"), rs.getString("Arrival_Airport"), rs.getInt("Route_Status"));

                routesList.add(routes);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return routesList;
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
    void handleRouteCol(MouseEvent event) {
        Routes route = tvRoutes.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2 && route != null){
            fc.setRoute(route);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
