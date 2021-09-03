package ams.airlinemanagementsystemos;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class Airlines {
    private int Airline_Code;
    private String Airline_Name;
    private String Airline_Address;
    private String Airline_City;
    private String Airline_State;
    private int Airline_Zip;
    private String Airline_Email;
    private LocalDate License_Issue;
    private LocalDate License_Expiry;
    private String durationFinal;
    private int days;
    private int months;
    private int years;

    public Airlines(int airline_Code, String airline_Name, String airline_Address, String airline_City, String airline_State, int airline_Zip, String airline_Email, Date license_Issue, Date license_Expiry) {
        this.Airline_Code = airline_Code;
        this.Airline_Name = airline_Name;
        this.Airline_Address = airline_Address;
        this.Airline_City = airline_City;
        this.Airline_State = airline_State;
        this.Airline_Zip = airline_Zip;
        this.Airline_Email = airline_Email;

        LocalDate licIss = new java.sql.Date(license_Issue.getTime()).toLocalDate();
        LocalDate licExp = new java.sql.Date(license_Expiry.getTime()).toLocalDate();

        this.License_Issue = licIss;
        this.License_Expiry = licExp;

        this.days = Period.between(licIss, licExp).getDays();
        this.months = Period.between(licIss, licExp).getMonths();
        this.years = Period.between(licIss, licExp).getYears();

        this.durationFinal = days+" Days, "+ months +" Months, "+ years +" Years";
    }

    public int getAirline_Code() {
        return Airline_Code;
    }

    public String getAirline_Name() {
        return Airline_Name;
    }

    public String getAirline_Address() {
        return Airline_Address;
    }

    public String getAirline_City() {
        return Airline_City;
    }

    public String getAirline_State() {
        return Airline_State;
    }

    public int getAirline_Zip() {
        return Airline_Zip;
    }

    public String getAirline_Email() {
        return Airline_Email;
    }

    public LocalDate getLicense_Issue() {
        return License_Issue;
    }

    public LocalDate getLicense_Expiry() {
        return License_Expiry;
    }

    public  String getDuration(){ return durationFinal; }

}
