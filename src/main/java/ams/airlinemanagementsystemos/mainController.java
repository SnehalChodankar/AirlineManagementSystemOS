package ams.airlinemanagementsystemos;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


/**
 * mainController is used to handle the login functionality of the application.
 *
 * Methods defined in mainController:
 * - onStartClick(ActionEvent event);     returns void
 * - getConnection();                     returns Connection
 * - initialize();                        return void
 * */
public class mainController {
    private Stage stage;
    private Scene scene;

    @FXML
    private AnchorPane apMain;

    @FXML
    private  ImageView ivMain;

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField tfPassword;

    /**
     * onStartClick method is called when the login button is clicked. This takes the user
     * to airline details page, that is, airline.fxml only if the entered username and
     * password details are correct. Otherwise, an error alert is shown to the user.
     * */
    @FXML
    void onStartClick(ActionEvent event) throws IOException {
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        Alert a = new Alert(Alert.AlertType.ERROR);

        String query = "SELECT * FROM login WHERE username = '"+username+"'";
        Connection conn = getConnection();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(!rs.next()){
                a.setHeaderText("Invalid Username");
                a.setContentText("User doesn't exists in the database!!!");
                a.show();
            }
            else{
                if(!Objects.equals(rs.getString("password"), password)){
                    a.setHeaderText("Invalid Password");
                    a.setContentText("User doesn't exists in the database!!!");
                    a.show();
                }
                else{
                    Parent root = FXMLLoader.load(getClass().getResource("airline.fxml"));
                    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * getConnection method is called whenever a query needs to be executed in the database.
     * This method creates a connection to the ams database present in the MySQL database server
     * using the admin login credentials.
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
     * initialize method is called as soon as the main.fxml is loaded.
     * This method deals with ivMain which basically binds the imageView
     * width property to gridPane width property.
     * So, if width of gridPane change, the width of imageView automatically will be change.
     * */
    @FXML
    void initialize() {
        ivMain.fitWidthProperty().bind(apMain.widthProperty());
        ivMain.fitHeightProperty().bind(apMain.heightProperty());
    }
}