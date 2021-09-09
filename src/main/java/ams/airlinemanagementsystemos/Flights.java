package ams.airlinemanagementsystemos;

public class Flights {
    private String Flight_Code;
    private int Flight_Capacity;
    private String Carrier_Name;
    private String Flight_Make;
    private String Flight_Model;
    private int Availability;

    public Flights(String flight_Code, int flight_Capacity, String carrier_Name, String flight_Make, String flight_Model, int availability) {
        this.Flight_Code = flight_Code;
        this.Flight_Capacity = flight_Capacity;
        this.Carrier_Name = carrier_Name;
        this.Flight_Make = flight_Make;
        this.Flight_Model = flight_Model;
        this.Availability = availability;
    }

    public String getFlight_Code() {
        return Flight_Code;
    }

    public int getFlight_Capacity() {
        return Flight_Capacity;
    }

    public String getCarrier_Name() {
        return Carrier_Name;
    }

    public String getFlight_Make() {
        return Flight_Make;
    }

    public String getFlight_Model() {
        return Flight_Model;
    }

    public int getAvailability() {
        return Availability;
    }
}
