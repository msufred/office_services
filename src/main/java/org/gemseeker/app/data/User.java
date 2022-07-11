package org.gemseeker.app.data;

/**
 *
 * @author Gem
 */
public class User extends DataEntry {

    private int id;
    private String username;
    private String password;
    private String authority = "guest";
    
    public User() {
        super("users", "id");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    
    @Override
    public String insertSQL() {
        return String.format("INSERT INTO %s (username, password, authority) "
                + "VALUES ('%s', '%s', '%s')",tableName, username, password, authority);
    }

    @Override
    public String updateSQL() {
        return String.format("UPDATE %s SET username='%s', password='%s', authority='%s' WHERE id='%s'",
                tableName, username, password, authority, id);
    }

}
