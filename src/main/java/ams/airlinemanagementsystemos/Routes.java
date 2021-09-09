package ams.airlinemanagementsystemos;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Routes {
    private int Route_ID;
    private String Flight_Code;
    private Time Departure_Date;
    private String Departure_Airport;
    private Time Arrival_Date;
    private String Arrival_Airport;
    private int Route_Status;

    public Routes(int route_ID, String flight_Code, Time departure_Date, String departure_Airport, Time arrival_Date, String arrival_Airport, int route_Status) {
        Route_ID = route_ID;
        Flight_Code = flight_Code;
        Departure_Date = departure_Date;
        Departure_Airport = departure_Airport;
        Arrival_Date = arrival_Date;
        Arrival_Airport = arrival_Airport;
        Route_Status = route_Status;
    }

    public int getRoute_ID() {
        return Route_ID;
    }

    public int getRoute_Status() {
        return Route_Status;
    }

    public String getFlight_Code() {
        return Flight_Code;
    }

    public Time getDeparture_Date() {
        return Departure_Date;
    }

    public String getDeparture_Airport() {
        return Departure_Airport;
    }

    public Time getArrival_Date() {
        return Arrival_Date;
    }

    public String getArrival_Airport() {
        return Arrival_Airport;
    }
}
