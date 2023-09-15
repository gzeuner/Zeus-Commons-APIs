package de.zeus.commons.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import static de.zeus.commons.provider.config.IProviderConstants.MODE_JSON;
import static de.zeus.commons.provider.config.IProviderConstants.MODE_XML;

/**
 * Utility class for writing content to files.
 */
public class FileWriterUtil {

    private static final Log LOG = LogFactory.getLog(FileWriterUtil.class) ;

    /**
     * Writes a given string content to a file with a dynamically generated file name.
     * The file type is specified as an argument and could be either JSON or XML.
     *
     * @param content  The content to be written to the file.
     * @param fileType The type of the file (JSON or XML).
     * @throws IOException  If an I/O error occurs.
     * @throws JDOMException If an XML error occurs.
     */
    public void writeStringToFile(String content, String fileType) throws IOException, JDOMException {
        // Generate a file name based on the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String fileName = dateFormat.format(new Date()) + "-result." + fileType.replace("application/", "");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            switch (fileType.toLowerCase()) {
                case MODE_JSON:
                    // Parse and pretty-print JSON content
                    JsonElement jsonElement = JsonParser.parseString(content);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonPretty = gson.toJson(jsonElement);
                    writer.write(jsonPretty);
                    break;
                case MODE_XML:
                    // Parse and pretty-print XML content
                    SAXBuilder saxBuilder = new SAXBuilder();
                    Document doc = saxBuilder.build(new StringReader(content));
                    XMLOutputter xmlOutputter = new XMLOutputter();
                    xmlOutputter.setFormat(Format.getPrettyFormat());
                    xmlOutputter.output(doc, writer);
                    break;
                default:
                    LOG.error("Invalid file type. Only 'json' and 'xml' are supported.");
            }
        }
    }
}
