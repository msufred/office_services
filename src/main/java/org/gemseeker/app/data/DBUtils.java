package org.gemseeker.app.data;

/**
 *
 * @author Gem
 */
public final class DBUtils {

    public static String[] tables() {
        return new String[] {
            createUsersTable(),
            createOfficesTable(),
            createPositionsTable(),
            createEmployeesTable(),
        };
    }
    
    public static String createUsersTable() {
        return "CREATE TABLE IF NOT EXISTS users ("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "username VARCHAR(255) NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "authority VARCHAR(100) NOT NULL, "
                + "PRIMARY KEY (id)"
                + ")";
    }
    
    public static String createOfficesTable() {
        return "CREATE TABLE IF NOT EXISTS offices ("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "name VARCHAR(255) NOT NULL, "
                + "code VARCHAR(100), "
                + "PRIMARY KEY (id)"
                + ")";
    }
    
    public static String createPositionsTable() {
        return "CREATE TABLE IF NOT EXISTS positions ("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "name VARCHAR(255) NOT NULL, "
                + "code VARCHAR(100), "
                + "PRIMARY KEY (id)"
                + ")";
    }
    
    public static String createEmployeesTable() {
        return "CREATE TABLE IF NOT EXISTS employees ("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "emp_id VARCHAR(100), "
                + "first_name VARCHAR(255), "
                + "middle_name VARCHAR(255), "
                + "last_name VARCHAR(255), "
                + "ext_name VARCHAR(5), "
                + "address VARCHAR(255), "
                + "phone VARCHAR(12), "
                + "birthday DATE, "
                + "office VARCHAR(255), "
                + "position VARCHAR(255), "
                + "status VARCHAR(100), "
                + "date_hired DATE, "
                + "date_resigned DATE, "
                + "date_retired DATE, "
                + "contact_person VARCHAR(255), "
                + "emergency_no VARCHAR(12), "
                + "emergency_address VARCHAR(255), "
                + "tin VARCHAR(100), "
                + "gsis VARCHAR(100), "
                + "PRIMARY KEY (id)"
                + ")";
    }
}
