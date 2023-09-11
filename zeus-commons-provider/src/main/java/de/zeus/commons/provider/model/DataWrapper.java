package de.zeus.commons.provider.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Wraps the data and metadata for a SQL result set.
 */
public class DataWrapper {

	// Default name for the wrapper
	private String name = "Entities";
	// Collection of metadata for each column
	private HashMap<Integer, MetaData> metaData = new HashMap<>();
	// Collection of content records
	private ArrayList<ContentRecordData> contentData = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<Integer, MetaData> getMetaData() {
		return metaData;
	}

	public void addMetaData(MetaData sqlMetaData) {
		if (!metaData.containsKey(sqlMetaData.getColumnNumber())) {
			metaData.put(sqlMetaData.getColumnNumber(), sqlMetaData);
		}
	}

	public ArrayList<ContentRecordData> getContentData() {
		return contentData;
	}

	public void addContentData(ContentRecordData contentDataObj) {
		if (!contentData.contains(contentDataObj)) {
			contentData.add(contentDataObj);
		}
	}

	// Method to retrieve metadata for a specific field by columnNumber
	public MetaData getMetaDataForField(int columnNumber) {
		return metaData.get(columnNumber);
	}

}
