package org.gemseeker.app.data;

import java.time.LocalDate;

/**
 *
 * @author Gem
 */
public class Employee extends DataEntry {

    private int id; // unique id in database
    private String empId = ""; // employee id, can be null
    
    // basic information
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String extName = "";
    private String address = "";
    private String phone = "";
    private LocalDate birthday;
    
    // employment information
    private String office = "";
    private String position = "";
    private String status = ""; // REG, COS, JO, OTHER
    private LocalDate dateHired;
    private LocalDate dateResigned;
    private LocalDate dateRetired;
    
    // emergency information
    private String contactPerson;
    private String emergencyNo;
    private String emergencyAddress;
    
    // accounts information
    private String tin;
    private String gsis;
    
    public Employee() {
        super("employees", "id");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateHired() {
        return dateHired;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    public LocalDate getDateResigned() {
        return dateResigned;
    }

    public void setDateResigned(LocalDate dateResigned) {
        this.dateResigned = dateResigned;
    }

    public LocalDate getDateRetired() {
        return dateRetired;
    }

    public void setDateRetired(LocalDate dateRetired) {
        this.dateRetired = dateRetired;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmergencyNo() {
        return emergencyNo;
    }

    public void setEmergencyNo(String emergencyNo) {
        this.emergencyNo = emergencyNo;
    }

    public String getEmergencyAddress() {
        return emergencyAddress;
    }

    public void setEmergencyAddress(String emergencyAddress) {
        this.emergencyAddress = emergencyAddress;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getGsis() {
        return gsis;
    }

    public void setGsis(String gsis) {
        this.gsis = gsis;
    }
    
    @Override
    public String insertSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName).append(" (emp_id, first_name, "
                + "middle_name, last_name, ext_name, address, phone");
        if (birthday != null) sb.append(", birthday");
        sb.append(", office, position, status");
        if (dateHired != null) sb.append(", date_hired");
        if (dateResigned != null) sb.append(", date_resigned");
        if (dateRetired != null) sb.append(", date_retired");
        sb.append(", contact_person, emergency_no, emergency_address, "
                + "tin, gsis) VALUES (");
        
        // VALUES
        sb.append(String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                empId, firstName, middleName, lastName, extName, address, phone));
        if (birthday != null) sb.append(String.format(", '%s'", birthday));
        sb.append(String.format(",'%s', '%s', '%s'", office, position, status));
        if (dateHired != null) sb.append(String.format(", '%s'", dateHired));
        if (dateResigned != null) sb.append(String.format(", '%s'", dateResigned));
        if (dateRetired != null) sb.append(String.format(", '%s'", dateRetired));
        sb.append(String.format(", '%s', '%s', '%s', '%s', '%s')",
                contactPerson, emergencyNo, emergencyAddress, tin, gsis));
        return sb.toString();
    }

    @Override
    public String updateSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName).append(" SET ");
        sb.append(String.format("emp_id='%s', ", empId));
        sb.append(String.format("first_name='%s', ", firstName));
        sb.append(String.format("middle_name='%s', ", middleName));
        sb.append(String.format("last_name='%s', ", lastName));
        sb.append(String.format("ext_name='%s', ", extName));
        sb.append(String.format("address='%s', ", address));
        sb.append(String.format("phone='%s', ", phone));
        if (birthday != null) sb.append(String.format("birthday='%s', ", birthday));
        sb.append(String.format("office='%s', ", office));
        sb.append(String.format("position='%s', ", position));
        sb.append(String.format("status='%s', ", status));
        if (dateHired != null) sb.append(String.format("date_hired='%s', ", dateHired));
        if (dateHired != null) sb.append(String.format("date_resigned='%s', ", dateResigned));
        if (dateHired != null) sb.append(String.format("date_retired='%s', ", dateRetired));
        sb.append(String.format("contact_person='%s', ", contactPerson));
        sb.append(String.format("emergency_no='%s', ", emergencyNo));
        sb.append(String.format("emergency_address='%s', ", emergencyAddress));
        sb.append(String.format("tin='%s', ", tin));
        sb.append(String.format("gsis='%s' ", gsis));
        sb.append(String.format("WHERE id='%d'", id));
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder name = new StringBuilder(); // last, first middle initial.
        name.append(lastName);
        if (!extName.isEmpty()) name.append(String.format(" %s., ", extName));
        else name.append(", ");
        name.append(firstName);
        if (!middleName.isEmpty()) name.append(String.format(" %s.", middleName.charAt(0)));
        return name.toString();
    }

}
