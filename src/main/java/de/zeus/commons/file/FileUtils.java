package de.zeus.commons.file;

import de.zeus.commons.provider.format.Format;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.JDOMException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


/**
 * Utility class for writing content to files.
 */
public class FileUtils {

    private static final Log LOG = LogFactory.getLog(FileUtils.class) ;
    /**
     * Reference to Format-Helper
     */
    private final Format format = new Format();

    /**
     * Writes a given string content to a file with a dynamically generated file name.
     * The file type is specified as an argument and could be either JSON or XML.
     *
     * @param content  The content to be written to the file.
     * @param fileType The type of the file (JSON or XML).
     * @throws IOException  If an I/O error occurs.
     * @throws JDOMException If an XML error occurs.
     */
    public void writeContentToFile(String content, String fileType) throws Exception {
        String fileName = format.generateFileName(fileType);
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName),
                        StandardCharsets.UTF_8))) {

            String formattedContent = format.formatContent(content, fileType);
            if (formattedContent != null) {
                writer.write(formattedContent);
            } else {
                LOG.error("Invalid file type. Only 'json' and 'xml' are supported.");
            }
        }
    }
    /**
     * Reads the content of a file or a resource within the JAR and returns it as a string.
     *
     * @param path The path to the file or the resource within the JAR.
     * @return The content of the file or resource as a string.
     * @throws IOException If an input/output error occurs.
     */
    public String readFileToString(String path) throws IOException {
        // First try to read the resource as a file
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            return readFromFile(file);
        } else {
            // Attempts to read the resource from the JAR
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                throw new FileNotFoundException("File or resource not found: " + path);
            }
            return readFromInputStream(inputStream);
        }
    }

    private String readFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining());
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining());
        }
    }

}
