import java.nio.file.*;

// Used for traveling though XML documents
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Helper class that puts repetitive functions in one spot.
 * <p>
 *     This file holds several methods that are universal around the 3 types of conversion files.
 *     They take in paths, documents, and nodes; to configure each of them into usable
 *     formats for each files purposes.
 * </p>
 *
 * @author augward
 */
public abstract class AllDocBookConverter implements FileConverter {

    // These can be used to safely get DocBook elements and verify parts.
    static final String DOCBOOK_NS = "http://docbook.org/ns/docbook";
    static final String XLINK_NS  = "http://www.w3.org/1999/xlink";

    /**
     * Creates and configures a new Document Builder, parsing the XML file,
     * and returning it as a Document.
     *
     * @param p is a path to an existing document
     * @return a new Document
     * @throws Exception is thrown if document builder fails
     */
    public Document parseXml(Path p) throws Exception {
        // Makes Document builders
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // Makes it aware of names, for queries
        dbf.setNamespaceAware(true);

        // Processing feature for parser
        dbf.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);

        // Parsing file and builds document
        return dbf.newDocumentBuilder().parse(p.toFile());
    }

    /**
     * Helper to locate and retrieve title
     *
     * @param doc the document which contains a title in it
     * @return the String of the title
     */
    public String getInfoTitle(Document doc) {
        // Gets info from DocBook
        NodeList infos = doc.getElementsByTagNameNS(DOCBOOK_NS, "info");
        if (infos.getLength() == 0) return null;

        // Pulls first info to get title
        Element info = (Element) infos.item(0);

        // Finds titles inside of element
        NodeList titles = info.getElementsByTagNameNS(DOCBOOK_NS, "title");
        if (titles.getLength() == 0) return null;

        // Returns the first, should be doc title
        return titles.item(0).getTextContent();
    }

    /**
     * Returns the text content of the first child that matches the name given
     *
     * @param parent the parent element of the nodes
     * @param childLocalName name of first matching child
     * @return String of the first matching child's content
     */
    public String getFirstChildText(Element parent, String childLocalName) {
        // Finds all nodes with local name
        NodeList nl = parent.getElementsByTagNameNS(DOCBOOK_NS, childLocalName);
        if (nl.getLength() == 0) return null;

        // Pulls the first node name content
        return nl.item(0).getTextContent();
    }
}