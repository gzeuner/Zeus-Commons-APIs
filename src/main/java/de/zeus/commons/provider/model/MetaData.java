package de.zeus.commons.provider.model;

import de.zeus.commons.provider.format.Format;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the metadata for a SQL table column.
 */
public class MetaData {

	// The index of the column
	private int columnNumber;
	// The SQL name of the column
	private String columnName;
	// The label for the column
	private String columnLabel;
	// The SQL data type of the column
	private int columnSqlDataType;
	// The SQL data type name
	private String columnSqlDataTypeName;
	// The Java class name corresponding to this column
	private String columnClassName;
	// Whether this column is a primary key
	private boolean isPrimaryKey = false;
	// List of table information related to this column
	private final List<TableInfo> tableInfoList = new ArrayList<>();

	// Formatting object
	private Format format = new Format();

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = format.toLowerCase(format.trim(columnName));
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public void setColumnLabel(String columnLabel) {
		this.columnLabel = format.toLowerCase(format.trim(columnLabel));
	}

	public int getColumnSqlDataType() {
		return columnSqlDataType;
	}

	public void setColumnSqlDataType(int columnSqlDataType) {
		this.columnSqlDataType = columnSqlDataType;
	}

	public String getColumnSqlDataTypeName() {
		return columnSqlDataTypeName;
	}

	public void setColumnSqlDataTypeName(String columnSqlDataTypeName) {
		this.columnSqlDataTypeName = format.trim(columnSqlDataTypeName);
	}

	public String getColumnClassName() {
		return columnClassName;
	}

	public void setColumnClassName(String columnClassName) {
		this.columnClassName = format.trim(columnClassName);
	}

	public List<TableInfo> getTableInfoList() {
		return tableInfoList;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	// Method to check if this column is a primary key in any of the related tables
	public void checkPrimaryKey() {
		for(TableInfo tableInfo : this.tableInfoList) {
			isPrimaryKey = tableInfo.isFieldPartOfPK(columnName);
			if (isPrimaryKey) {
				break;
			}
		}
	}

	// Method to add table info to the tableInfoList
	public void addInfoToTableList(TableInfo tableInfo) {
		if(!this.tableInfoList.contains(tableInfo)) {
			this.tableInfoList.add(tableInfo);
		}
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.isPrimaryKey = primaryKey;
	}

	@Override
	public String toString() {
		return "MetaData{" +
				"columnNumber=" + columnNumber +
				", columnName='" + columnName + '\'' +
				", columnLabel='" + columnLabel + '\'' +
				", columnSqlDataType=" + columnSqlDataType +
				", columnSqlDataTypeName='" + columnSqlDataTypeName + '\'' +
				", columnClassName='" + columnClassName + '\'' +
				", isPrimaryKey=" + isPrimaryKey +
				'}';
	}

}
