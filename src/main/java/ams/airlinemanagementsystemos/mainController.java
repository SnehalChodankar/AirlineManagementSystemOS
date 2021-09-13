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
     * onStartClick method is called when the Start button is clicked. This takes the user
     * to airline details page, that is, airline.fxml.
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

    @FXML
    void initialize() {

        // Bind the imageView width property to gridPane width property
        // So, if width of gridPane change, the width of imageView automatically will be change
        ivMain.fitWidthProperty().bind(apMain.widthProperty());
        ivMain.fitHeightProperty().bind(apMain.heightProperty());

        // Make the ratio same with original image
        //ivMain.setPreserveRatio(true);

    }
}