package ams.airlinemanagementsystemos;

public class Reservations {
    private int Reservation_Code;
    private int Passenger_Code;
    private String Flight_Class;
    private double Fare;
    private boolean Status;

    public Reservations(int reservation_Code, int passenger_Code, String flight_Class, double fare, boolean status) {
        Reservation_Code = reservation_Code;
        Passenger_Code = passenger_Code;
        Flight_Class = flight_Class;
        Fare = fare;
        Status = status;
    }

    public int getReservation_Code() {
        return Reservation_Code;
    }

    public int getPassenger_Code() {
        return Passenger_Code;
    }

    public String getFlight_Class() {
        return Flight_Class;
    }

    public double getFare() {
        return Fare;
    }

    public boolean isStatus() {
        return Status;
    }
}