import java.nio.file.*;
import java.nio.charset.*;

// Used for traveling though XML documents
import org.w3c.dom.*;

/**
 * Conversion file between xml and json file types
 * <p>
 *     File takes in an input and output path.  These carry the .fml file and send the json file
 *     to its destination.  The file extends a class full of helper methods called AllDocBookConverter
 *     and implements FileConverter.  It does this by overriding the convert method, singling out the
 *     elements in the original file and collecting relevant ones to make a table of contents.
 * </p>
 *
 * @author augward
 */
public class DocBookToTOCJSON extends AllDocBookConverter implements FileConverter {

    /**
     * Overridden method from FileConverter, reads XML, collects title and top sections,
     * builds JSON string, and writes it
     *
     * @param input path to the input DocBook XML file
     * @param output path where Markdown will be written
     * @throws Exception is thrown when there is a parsing error or writing to file
     */
    @Override
    public void convert(Path input, Path output) throws Exception {
        // Parses XML into a dom document
        Document doc = parseXml(input);

        StringBuilder json = new StringBuilder();

        // Finds document title and sections and adds to string builder
        String docTitle = getInfoTitle(doc);
        json.append("{\n");
        json.append("  \"title\": ").append(quote(docTitle)).append(",\n");
        json.append("  \"sections\": [\n");

        // Finds the root element and travels through tree from root
        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();
        boolean first = true;

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeType() != Node.ELEMENT_NODE) continue;
            Element childElement = (Element) child;

            // Checks that element is a DocBook
            if(DOCBOOK_NS.equals(childElement.getNamespaceURI()) && "section".equals(childElement.getLocalName())) {
                // If not first, commas and new line
                if (!first) json.append(",\n");
                first = false;


                String id = childElement.hasAttribute("xml:id") ? childElement.getAttribute("xml:id") : "";
                String title = getFirstChildText(childElement, "title");

                json.append("    { \"id\": ").append(quote(id)).append(", \"title\": ").append(quote(title)).append(" }");
            }

        }

        // Writes to file in Markdown format
        json.append("\n  ]\n");
        json.append("}\n");

        Files.writeString(output, json.toString());
    }

    /**
     * Simple qute helper method
     *
     * @param s String of a quote
     * @return String with quotes around it
     */
    private String quote(String s) {
        if (s == null) s = "";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
