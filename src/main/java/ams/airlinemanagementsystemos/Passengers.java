package ams.airlinemanagementsystemos;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class Passengers {
    private int Passenger_Code;
    private String First_Name;
    private String Middle_Name;
    private String Last_Name;
    private LocalDate Passenger_DOB;

    public Passengers(int passenger_Code, String first_Name, String middle_Name, String last_Name, Date passenger_DOB) {
        this.Passenger_Code = passenger_Code;
        this.First_Name = first_Name;
        this.Middle_Name = middle_Name;
        this.Last_Name = last_Name;

        LocalDate Pass_DOB = new Date(passenger_DOB.getTime()).toLocalDate();

        this.Passenger_DOB = Pass_DOB  ;
    }

    public int getPassenger_Code() {
        return Passenger_Code;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public String getMiddle_Name() {
        return Middle_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public LocalDate getPassenger_DOB() {
        return Passenger_DOB;
    }
}
