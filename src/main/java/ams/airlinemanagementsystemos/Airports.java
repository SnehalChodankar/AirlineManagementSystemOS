package ams.airlinemanagementsystemos;

/**
 * Airports is the model class of the airportController.
 * This class contains the constructor to initialize all the varaibles associated with airport table
 * in the database.
 * It also contains the getter methods used for accessing the values from the variables.
 *
 * Methods defined in Airports:
 * - Airports - Parameterized Constructor
 * - getter Methods for all the variables declared.
 * */
public class Airports {
    private String Airport_Code;
    private String Airport_Name;
    private String Airport_City;
    private String Airport_State;
    private int Op_Status;
    private String Airline_Code;

    public Airports(String airport_Code, String airport_Name, String airport_City, String airport_State, int status) {
        this.Airport_Code = airport_Code;
        this.Airport_Name = airport_Name;
        this.Airport_City = airport_City;
        this.Airport_State = airport_State;
        this.Op_Status = status;
    }

    public String getAirport_Code() {
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

    public int getOp_Status() { return Op_Status; }
}
