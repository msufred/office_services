package org.gemseeker.app.data;

/**
 *
 * @author Gem
 */
public abstract class DataEntry {

    protected final String tableName;
    protected final String keyColumn;
    
    public DataEntry(String tableName, String keyColumn) {
        this.tableName = tableName;
        this.keyColumn = keyColumn;
    }
    
    public abstract String insertSQL();
    public abstract String updateSQL();
}
