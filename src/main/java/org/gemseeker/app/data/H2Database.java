package org.gemseeker.app.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import org.gemseeker.app.Utils;

/**
 *
 * @author Gem
 */
public class H2Database {
    
    private Connection connection;
    private Properties properties;
    private static H2Database instance;
    
    private H2Database() throws ClassNotFoundException, SQLException, IOException {
        initProperties();
        openDatabase();
        create();
        update();
    }

    public static H2Database getInstance() throws ClassNotFoundException,
            SQLException, IOException {
        if (instance == null) instance = new H2Database();
        return instance;
    }
    
    private void initProperties() throws IOException{
//        Settings settings = Settings.getInstance();
//        properties = new Properties();
//        properties.put("user", settings.getDatabaseValue("user"));
//        properties.put("password", settings.getDatabaseValue("password"));
        properties = new Properties();
        properties.put("user", "admin");
        properties.put("password", "admin");
    }
    
    private void openDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        String dbUrl = "jdbc:h2:file:" + Utils.DATABASE_PATH;
        connection = DriverManager.getConnection(dbUrl, properties);
    }
    
    public void closeDatabase() throws SQLException {
        if (connection != null) connection.close();
    }
    
    private void create() throws SQLException {
        if (connection != null) {
            for (String sql : DBUtils.tables()) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(sql);
                }
            }
        }
    }
    
    private void update() throws SQLException {
        // TODO
    }
    
    public boolean executeQuery(String sql) throws SQLException {
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                return statement.executeUpdate(sql) > 0;
            }
        }
        return false;
    }
    
    public boolean addEntry(DataEntry entry) throws SQLException {
        return executeQuery(entry.insertSQL());
    }
    
    public int addEntryReturnId(DataEntry entry) throws SQLException {
        if (connection != null) {
            try (PreparedStatement statement = connection.prepareStatement(entry.insertSQL(), PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
    
    public boolean updateEntry(DataEntry entry) throws SQLException {
        return executeQuery(entry.updateSQL());
    }
    
    public boolean updateEntry(String table, String col, Object colValue, String keyCol, Object keyValue) throws SQLException {
        String sql = String.format("UPDATE %s SET %s='%s' WHERE %s='%s'", table, col, colValue, keyCol, keyValue);
        return executeQuery(sql);
    }
    
    public boolean deleteEntry(String table, String keyColumn, Object keyValue) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s='%s'", table, keyColumn, keyValue);
        return executeQuery(sql);
    }
    
    //==========================================================================
    
    public User getUser(String username, String password) throws SQLException {
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format("SELECT * FROM users WHERE username='%s' AND password='%s' LIMIT 1",
                        username, password);
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt(1));
                    user.setUsername(rs.getString(2));
                    user.setPassword(rs.getString(3));
                    user.setAuthority(rs.getString(4));
                    return user;
                }
            }
        }
        return null;
    }
    
    //==========================================================================
    
    public ArrayList<Office> getAllOffices() throws SQLException {
        ArrayList<Office> offices = new ArrayList<>();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM offices");
                while (rs.next()) {
                    Office office = fetchOfficeInfo(rs);
                    offices.add(office);
                }
            }
        }
        return offices;
    }
    
    public Office getOfficeByName(String name) throws SQLException {
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format("SELECT * FROM offices WHERE name='%s'", name);
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) return fetchOfficeInfo(rs);
            }
        }
        return null;
    }
    
    private Office fetchOfficeInfo(ResultSet rs) throws SQLException {
        Office office = new Office();
        office.setId(rs.getInt(1));
        office.setName(rs.getString(2));
        office.setCode(rs.getString(3));
        return office;
    }
    
    //==========================================================================
    
    public ArrayList<Position> getAllPositions() throws SQLException {
        ArrayList<Position> positions = new ArrayList<>();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM positions");
                while (rs.next()) {
                    Position pos = fetchPositionInfo(rs);
                    positions.add(pos);
                }
            }
        }
        return positions;
    }
    
    public Position getPositionByName(String name) throws SQLException {
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format("SELECT * FROM positions WHERE name='%s'", name);
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) return fetchPositionInfo(rs);
            }
        }
        return null;
    }
    
    private Position fetchPositionInfo(ResultSet rs) throws SQLException {
        Position pos = new Position();
        pos.setId(rs.getInt(1));
        pos.setName(rs.getString(2));
        pos.setCode(rs.getString(3));
        return pos;
    }
    
    //==========================================================================
    
    public ArrayList<Employee> getAllEmployees() throws SQLException {
        ArrayList<Employee> employees = new ArrayList<>();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM employees");
                while (rs.next()) {
                    Employee emp = fetchEmployeeInfo(rs);
                    employees.add(emp);
                }
            }
        }
        return employees;
    }
    
    public Employee getEmployee(int id) throws SQLException {
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                String sql = String.format("SELECT * FROM employees WHERE id='%d' LIMIT 1", id);
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) return fetchEmployeeInfo(rs);
            }
        }
        return null;
    }
    
    private Employee fetchEmployeeInfo(ResultSet rs) throws SQLException {
        int index = 1;
        Employee emp = new Employee();
        emp.setId(rs.getInt(index++));
        emp.setEmpId(rs.getString(index++));
        emp.setFirstName(rs.getString(index++));
        emp.setMiddleName(rs.getString(index++));
        emp.setLastName(rs.getString(index++));
        emp.setExtName(rs.getString(index++));
        emp.setAddress(rs.getString(index++));
        emp.setPhone(rs.getString(index++));
        Date birthday = rs.getDate(index++);
        if (birthday != null) emp.setBirthday(birthday.toLocalDate());
        emp.setOffice(rs.getString(index++));
        emp.setPosition(rs.getString(index++));
        emp.setStatus(rs.getString(index++));
        Date dateHired = rs.getDate(index++);
        if (dateHired != null) emp.setDateHired(dateHired.toLocalDate());
        Date dateResigned = rs.getDate(index++);
        if (dateResigned != null) emp.setDateHired(dateResigned.toLocalDate());
        Date dateRetired = rs.getDate(index++);
        if (dateRetired != null) emp.setDateHired(dateRetired.toLocalDate());
        emp.setContactPerson(rs.getString(index++));
        emp.setEmergencyNo(rs.getString(index++));
        emp.setEmergencyAddress(rs.getString(index++));
        emp.setTin(rs.getString(index++));
        emp.setGsis(rs.getString(index++));
        return emp;
    }
    
    //==========================================================================
}
