import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DocumentType;
import org.w3c.dom.ProcessingInstruction;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Debug utility to explore DocBook XML via the DOM API.
 * 
 * This tool reads a DocBook XML file into a DOM {@code Document} and prints a
 * human-readable tree to stdout. It is for debugging/exploration only and is
 * not part of the conversion pipeline.
*/
public class DumpDocBookXMLObjectModelDriver {

    /**
     * Reads the given XML file into a DOM Document.
     *
     * @param xmlPath path to the XML file
     * @return parsed DOM Document
     * @throws Exception if parsing fails
     */
    public static Document readXmlToDocument(Path xmlPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Recommended secure settings
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        // Dis-Allow DOCTYPE declarations by disabling the disallow-doctype feature
        // factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        // Allow DOCTYPE declarations by disabling the disallow-doctype feature
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);

        // Disable external entity resolution to prevent XXE attacks
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlPath.toFile());
    }

    /**
     * Recursively traverses the provided DOM and prints the contents of each node.
     * This includes elements, attributes, text, CDATA, processing instructions, and document type.
     *
     * @param document the DOM Document to traverse
     */
    public static void printDocumentTree(Document document) {
        printNode(document, 0);
    }

    private static void printNode(Node node, int depth) {
        String indent = "  ".repeat(Math.max(0, depth));

        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE -> {
                System.out.println(indent + "#document");
                // Print DocumentType, if present
                DocumentType doctype = ((Document) node).getDoctype();
                if (doctype != null) {
                    System.out.println(indent + "  " + "DocType: name=" + doctype.getName()
                            + (doctype.getPublicId() != null ? " publicId=" + doctype.getPublicId() : "")
                            + (doctype.getSystemId() != null ? " systemId=" + doctype.getSystemId() : ""));
                }
                // Recurse into children (typically DocumentElement)
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    printNode(children.item(i), depth + 1);
                }
            }
            case Node.ELEMENT_NODE -> {
                // Print element with its qualified name
                System.out.println(indent + "Element: <" + node.getNodeName() + ">");
                // Print attributes (if any)
                NamedNodeMap attrs = node.getAttributes();
                if (attrs != null && attrs.getLength() > 0) {
                    for (int i = 0; i < attrs.getLength(); i++) {
                        Attr a = (Attr) attrs.item(i);
                        System.out.println(indent + "  @"
                                + a.getName() + "=\"" + a.getValue() + "\"");
                    }
                }
                // Recurse into children
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    printNode(children.item(i), depth + 1);
                }
            }
            case Node.TEXT_NODE -> {
                // Print non-empty text nodes
                String data = ((Text) node).getWholeText();
                String trimmed = data == null ? "" : data.trim();
                if (!trimmed.isEmpty()) {
                    System.out.println(indent + "Text: " + trimmed);
                }
            }
            case Node.CDATA_SECTION_NODE -> {
                String data = ((CDATASection) node).getData();
                System.out.println(indent + "CDATA: " + data);
            }
            case Node.PROCESSING_INSTRUCTION_NODE -> {
                ProcessingInstruction pi = (ProcessingInstruction) node;
                System.out.println(indent + "PI: target=" + pi.getTarget() + " data=" + pi.getData());
            }
            case Node.COMMENT_NODE -> {
                // Comments are ignored by factory.setIgnoringComments(true), but handle just in case
                System.out.println(indent + "Comment");
            }
            case Node.ENTITY_REFERENCE_NODE -> {
                System.out.println(indent + "EntityRef: " + node.getNodeName());
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    printNode(children.item(i), depth + 1);
                }
            }
            case Node.DOCUMENT_FRAGMENT_NODE -> {
                System.out.println(indent + "#document-fragment");
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    printNode(children.item(i), depth + 1);
                }
            }
            default -> {
                // Fallback for other node types
                System.out.println(indent + "Node(" + node.getNodeType() + "): " + node.getNodeName());
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    printNode(children.item(i), depth + 1);
                }
            }
        }
    }

    // Example usage
    public static void main(String[] args) {
        System.out.println("This demo program reads a complex XML document and prints a representation of it to" +
                "the system output stream.  This is NOT a good example of how to do the file conversion required for" +
                "completing this assignment, but it has some useful features for debugging and exploring the DOM API." +
                "It is *ONLY* intended to be used as a starting point for understanding the DOM API and for debugging.\n\n");

        try {
            Path xml = args.length > 0 ? Paths.get(args[0]) : Paths.get("src/complex_document.xml");

            Document inputDocumentMemoryModel = readXmlToDocument(xml);

            {  // Do something with the Document (example: print the root element)
                final String root = inputDocumentMemoryModel.getDocumentElement() != null
                        ? inputDocumentMemoryModel.getDocumentElement().getNodeName() : "(no root)";
                System.out.println("Parsed XML. Root element: " + root);
            }

            // Print the entire document tree
            printDocumentTree(inputDocumentMemoryModel);
        } catch (Exception e) {
            System.err.println("Failed to parse XML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
