package org.gemseeker.app.data;

/**
 *
 * @author Gem
 */
public class Vehicle extends DataEntry {
    
    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_ON_TRAVEL = "On Travel";
    public static final String STATUS_ON_REPAIR = "On Repair";
    public static final String STATUS_OUT_OF_SERVICE = "Out of Service";
    
    private int id;
    private String name;
    private String plateNo;
    private String status = STATUS_AVAILABLE;
    
    public Vehicle() {
        super("vehicles", "id");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String insertSQL() {
        return String.format("INSERT INTO %s (name, plate_no, status) VALUES ('%s', '%s', '%s')",
                tableName, name, plateNo, status);
    }

    @Override
    public String updateSQL() {
        return String.format("UPDATE %s SET name='%s', plate_no='%s', status='%s' WHERE id='%d'",
                tableName, name, plateNo, status, id);
    }

}
