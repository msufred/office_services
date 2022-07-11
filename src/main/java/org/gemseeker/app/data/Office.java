package org.gemseeker.app.data;

/**
 *
 * @author Gem
 */
public class Office extends DataEntry {
    
    private int id;
    private String name;
    private String code = ""; // or nickname; can be null
    
    public Office() {
        super("offices", "id");
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String insertSQL() {
        return String.format("INSERT INTO %s (name, code) VALUES ('%s', '%s')",
                tableName, name, code);
    }

    @Override
    public String updateSQL() {
        return String.format("UPDATE %s SET name='%s', code='%s' WHERE id='%d'",
                tableName, name, code, id);
    }

    @Override
    public String toString() {
        return name;
    }

}
