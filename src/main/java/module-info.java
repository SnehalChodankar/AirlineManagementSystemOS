module ams.airlinemanagementsystemos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ams.airlinemanagementsystemos to javafx.fxml;
    exports ams.airlinemanagementsystemos;
}