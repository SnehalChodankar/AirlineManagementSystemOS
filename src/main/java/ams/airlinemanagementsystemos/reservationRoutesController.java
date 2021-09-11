package ams.airlinemanagementsystemos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;

public class reservationRoutesController {
    reservationController rc;
    String departureAirport, arrivalAirport, airline;

    public void setAirlineCode(reservationController reservationController, String dAirport, String aAirport, String airline) {
        this.rc = reservationController;

        this.departureAirport = dAirport;
        this.arrivalAirport = aAirport;
        this.airline = airline;

        showRoutes();
    }

    private void showRoutes() {
        ObservableList<Routes> list = getRoutesList();

        colFlightCode.setCellValueFactory(new PropertyValueFactory<Routes, String>("Flight_Code"));
        colRouteId.setCellValueFactory(new PropertyValueFactory<Routes, Integer>("Route_ID"));
        colDepartureTime.setCellValueFactory(new PropertyValueFactory<Routes, LocalTime>("Departure_Date"));
        colDepartureAirport.setCellValueFactory(new PropertyValueFactory<Routes, String>("Departure_Airport"));
        colArrivalTime.setCellValueFactory(new PropertyValueFactory<Routes, LocalTime>("Arrival_Date"));
        colArrivalAirport.setCellValueFactory(new PropertyValueFactory<Routes, String>("Arrival_Airport"));

        tvReservationRoute.setItems(list);
    }

    private ObservableList<Routes> getRoutesList() {
        ObservableList<Routes> routesList = FXCollections.observableArrayList();
        Connection conn = getConnection();

        String query = "SELECT * FROM route WHERE Departure_Airport='"+departureAirport+"' AND Arrival_Airport='"+arrivalAirport+"' AND Flight_Code IN (SELECT Flight_Code FROM flight WHERE Carrier_Name='"+airline+"')";

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
    private TableView<Routes> tvReservationRoute;

    @FXML
    private TableColumn<Routes, String> colFlightCode;

    @FXML
    private TableColumn<Routes, String> colDepartureAirport;

    @FXML
    private TableColumn<Routes, String> colArrivalAirport;

    @FXML
    private TableColumn<Routes, LocalTime> colDepartureTime;

    @FXML
    private TableColumn<Routes, LocalTime> colArrivalTime;

    @FXML
    private TableColumn<Routes, Integer> colRouteId;

    @FXML
    void handleRouteCol(MouseEvent event) {
        Routes route = tvReservationRoute.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2 && route != null){
            rc.setRoute(route);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
