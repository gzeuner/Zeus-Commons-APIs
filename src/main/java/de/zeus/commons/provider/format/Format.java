package de.zeus.commons.provider.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static de.zeus.commons.base.constants.IConstants.MODE_JSON;
import static de.zeus.commons.base.constants.IConstants.MODE_XML;
import static org.jdom2.output.Format.getPrettyFormat;

public class Format {

	/**
	 * Trims leading and trailing whitespaces from a given string.
	 *
	 * @param value The string to trim.
	 * @return The trimmed string, or an empty string if the input is null.
	 */
	public String trim(String value) {
		if (value == null) {
			return "";
		} else {
			return value.trim();
		}
	}

	/**
	 * Converts a given string to lowercase.
	 *
	 * @param value The string to convert.
	 * @return The lowercase version of the input string, or the input string if it's null.
	 */
	public String toLowerCase(String value) {
		if (null != value) {
			value = value.toLowerCase();
		}
		return value;
	}

	public String generateFileName(String fileType) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		return dateFormat.format(new Date()) + "-result."
				+ fileType.replace("application/", "");
	}

	public String formatContent(String content, String fileType) throws Exception {
		switch (fileType.toLowerCase()) {
			case MODE_JSON:
				JsonElement jsonElement = JsonParser.parseString(content);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				return gson.toJson(jsonElement);
			case MODE_XML:
				SAXBuilder saxBuilder = new SAXBuilder();
				Document doc = saxBuilder.build(new StringReader(content));
				XMLOutputter xmlOutputter = new XMLOutputter();
				xmlOutputter.setFormat(getPrettyFormat());
				StringWriter stringWriter = new StringWriter();
				xmlOutputter.output(doc, stringWriter);
				return stringWriter.toString();
			default:
				return null;
		}
	}

}
