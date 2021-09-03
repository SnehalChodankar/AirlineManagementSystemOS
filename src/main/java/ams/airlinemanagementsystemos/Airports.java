package ams.airlinemanagementsystemos;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class Airports {
    private int Airport_Code;
    private String Airport_Name;
    private String Airport_City;
    private String Airport_State;
    private String Airline_Code;

    public Airports(int airport_Code, String airport_Name, String airport_City, String airport_State, String airline_Code) {
        this.Airport_Code = airport_Code;
        this.Airport_Name = airport_Name;
        this.Airport_City = airport_City;
        this.Airport_State = airport_State;
        this.Airline_Code = airline_Code;
    }

    public int getAirport_Code() {
        return Airport_Code;
    }

    public String getAirport_Name() {
        return Airport_Name;
    }

    public String getAirport_City() {
        return Airport_City;
    }

    public String getAirport_State() {
        return Airport_State;
    }

    public String getAirline_Code() {
        return Airline_Code;
    }
}
