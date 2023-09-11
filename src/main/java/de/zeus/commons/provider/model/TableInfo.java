package de.zeus.commons.provider.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the metadata information for a SQL table.
 * Contains detail such as the catalog, schema, and table name,
 * as well as the fields that are part of the primary key.
 */
public class TableInfo {

    // The catalog to which this table belongs
    private String catalog;
    // The schema to which this table belongs
    private String schema;
    // The name of the table
    private String table;
    // List of primary key field
    private final List<String> primaryKeyFields = new ArrayList<>();

    public TableInfo(String catalog, String schema, String table) {
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getPrimaryKeyFields() {
        return primaryKeyFields;
    }

    public void addFieldToPimaryKey(String primaryKey) {
        this.primaryKeyFields.add(primaryKey);
    }

    public void addAllFieldsToPrimaryKey(List<String> primaryKeyFields) {
        this.primaryKeyFields.addAll(primaryKeyFields);
    }

    public boolean isFieldPartOfPK(String fieldName) {

        for(String field : this.primaryKeyFields) {
            if(field.equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableInfo tableInfo = (TableInfo) o;
        return Objects.equals(catalog, tableInfo.catalog) && Objects.equals(schema, tableInfo.schema) && Objects.equals(table, tableInfo.table) && Objects.equals(primaryKeyFields, tableInfo.primaryKeyFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalog, schema, table, primaryKeyFields);
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "catalog='" + catalog + '\'' +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", primaryKeyFields=" + primaryKeyFields +
                '}';
    }
}
