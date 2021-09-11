package ams.airlinemanagementsystemos;

public class Reservations {
    private int Reservation_Code;
    private int Passenger_Code;
    private int Route_ID;
    private String FlightClass;
    private double Fare;
    private int Reservation_Status;

    public Reservations(int reservation_Code, int passenger_Code, int route_ID, String aClass, double fare, int reservation_Status) {
        this.Reservation_Code = reservation_Code;
        this.Passenger_Code = passenger_Code;
        this.Route_ID = route_ID;
        this.FlightClass = aClass;
        this.Fare = fare;
        this.Reservation_Status = reservation_Status;
    }

    public int getReservation_Code() {
        return Reservation_Code;
    }

    public int getPassenger_Code() {
        return Passenger_Code;
    }

    public int getRoute_ID() {
        return Route_ID;
    }

    public String getFlightClass() {
        return FlightClass;
    }

    public double getFare() {
        return Fare;
    }

    public int getReservation_Status() {
        return Reservation_Status;
    }
}