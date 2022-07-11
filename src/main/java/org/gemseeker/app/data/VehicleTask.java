package org.gemseeker.app.data;

import java.time.LocalDate;

/**
 *
 * @author Gem
 */
public class VehicleTask extends DataEntry {
    
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_DONE = "Done";
    
    private int id;
    private LocalDate date;
    private int vehicleId;
    private String driver;
    private String destination;
    private double distance; // in kilometers
    private String passenger;
    private LocalDate dateOut;
    private LocalDate dateIn; // or date returned
    private String status = STATUS_PENDING;
    
    public VehicleTask() {
        super("vehicle_tasks", "id");
        
        // by default, we set all dates to current
        date = LocalDate.now();
        dateOut = LocalDate.now();
        dateIn = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public LocalDate getDateOut() {
        return dateOut;
    }

    public void setDateOut(LocalDate dateOut) {
        this.dateOut = dateOut;
    }

    public LocalDate getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDate dateIn) {
        this.dateIn = dateIn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String insertSQL() {
        return String.format("INSERT INTO %s (date, vehicle_id, driver, "
                + "destination, distance, passenger, date_out, date_in, status) "
                + "VALUES ('%s', '%d', '%s', '%s', '%f', '%s', '%s', '%s', '%s')",
                tableName, date, vehicleId, driver, destination, distance, passenger,
                dateOut, dateIn, status);
    }

    @Override
    public String updateSQL() {
        return String.format("UPDATE %s SET date='%s', vehicle_id='%d', driver='%s', "
                + "destination='%s', distance='%f', passenger='%s', date_out='%s', "
                + "date_in='%s', status='%s' WHERE id='%d'",
                tableName, date, vehicleId, driver, destination, distance, passenger,
                dateOut, dateIn, status, id);
    }

}
