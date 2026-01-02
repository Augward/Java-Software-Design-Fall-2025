import java.nio.file.*;

// Used for traveling though XML documents
import org.w3c.dom.*;

/**
 * Conversion file between xml and ascii (txt) file types
 * <p>
 *     File takes in an input and output path.  These carry the .fml file and send the ascii (txt) file
 *     to its destination.  The file extends a class full of helper methods called AllDocBookConverter
 *     and implements FileConverter.  It does this by overriding the convert method, and diving each
 *     element with a recursive tree to search for hidden/sub elements.  It the organizes them and adds
 *     them to a final string which holds everything in txt format.
 * </p>
 *
 * @author augward
 */
public class DocBookToASCII extends AllDocBookConverter implements FileConverter {

    /**
     * Overridden method from FileConverter, reads XML, transforms into ASCII text,and writes it
     *
     * @param input path to the input DocBook XML file
     * @param output path where Markdown will be written
     * @throws Exception is thrown when there is a parsing error or writing to file
     */
    @Override
    public void convert(Path input, Path output) throws Exception {
        // Parses XML into a dom document
        Document doc = parseXml(input);

        StringBuilder ascii = new StringBuilder();

        // Finds document title and adds to string builder
        String docTitle = getInfoTitle(doc);

        if (docTitle != null && !docTitle.isBlank()) {
            ascii.append(docTitle).append("\n");
            ascii.append("=".repeat(Math.min(60, docTitle.length()))).append("\n\n");
        }

        // Finds the root element and travels through tree from root
        Element root = doc.getDocumentElement();
        traverse(root, ascii, 0);

        // Writes to file in Markdown format
        Files.writeString(output, ascii.toString());
    }

    /**
     * Travels through the file starting at the top, diving deeper with recursive tree calls
     * to allow interior analysis of elements and different actions depending on type
     *
     * @param node current node in the program file
     * @param out current String builder object status
     * @param depth level of the tree deep, recursive trees
     */
    private void traverse(Node node, StringBuilder out, int depth) {
        // Gets a list of child nodes
        NodeList kids = node.getChildNodes();

        for (int i = 0; i < kids.getLength(); i++) {
            Node kid = kids.item(i);
            if (kid.getNodeType() != Node.ELEMENT_NODE) continue;

            // Turns the kid into an element for use
            Element elementKid = (Element) kid;
            String localName = elementKid.getLocalName();
            String nameSpace = elementKid.getNamespaceURI();

            // This checks if it is a DocBook <section>
            if ("section".equals(localName) && DOCBOOK_NS.equals(nameSpace)) {

                // Get the section title
                String title = getFirstChildText(elementKid, "title");

                if (title != null && !title.isBlank()) {

                    String newTitle = title.replaceAll("\\s+", " ").trim();
                    // Print a blank line, section title, and underlines it
                    out.append("\n").append(newTitle.trim()).append("\n");
                    out.append("-".repeat(Math.min(50, newTitle.length()))).append("\n\n");
                }
                // Recurse to process children
                traverse(elementKid, out, depth + 1);


                // This checks if it is a DocBook <para>
            } else if ("para".equals(localName) && DOCBOOK_NS.equals(nameSpace)) {

                // Extract paragraph
                String text = getPlainTextWithLinks(elementKid);

                if (!text.isBlank()) {
                    // Append the paragraph
                    out.append(text.trim()).append("\n\n");
                }


                // This checks if it is an ordered list
            } else if ("orderedlist".equals(localName) && DOCBOOK_NS.equals(nameSpace)) {

                // Get all items in the list
                NodeList items = elementKid.getElementsByTagNameNS(DOCBOOK_NS, "listitem");

                for (int j = 0; j < items.getLength(); j++) {
                    // Retrieves text from each item in list
                    String text = getFirstChildText((Element) items.item(j), "para");
                    if (text == null) text = getPlainTextWithLinks(items.item(j));

                    // Prints numbers: 1)
                    out.append((j + 1)).append(") ").append(text.trim()).append("\n");
                }
                out.append("\n");


                // This checks if it is code
            } else if ("programlisting".equals(localName) && DOCBOOK_NS.equals(nameSpace)) {

                // Only way this worked:
                StringBuilder code = new StringBuilder();
                NodeList codeKids = elementKid.getChildNodes();

                for (int k = 0; k < codeKids.getLength(); k++) {
                    Node ck = codeKids.item(k);

                    if (ck.getNodeType() == Node.TEXT_NODE || ck.getNodeType() == Node.CDATA_SECTION_NODE) {
                        code.append(ck.getTextContent());
                    } else if (ck.getNodeType() == Node.ELEMENT_NODE) {
                        code.append(getPlainTextWithLinks(ck));
                    }
                }
                // Pulls code without care for what's around it, since that's what the file looks like
                String block = code.toString().replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                if (!block.isBlank()) {
                    out.append("\n").append(block).append("\n\n");
                }


            } else {
                // For any other element
                traverse(elementKid, out, depth);
            }
        }
    }

    /**
     * String builder that prints out text to a file with proper care
     * for type of node and content within.
     *
     * @param parent the node that is being analysis for print
     * @return a unique string dependent on node type
     */
    private String getPlainTextWithLinks(Node parent) {
        StringBuilder sb = new StringBuilder();
        NodeList nl = parent.getChildNodes();

        // For each node in the list
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);

            // Appending content
            if (n.getNodeType() == Node.TEXT_NODE) {
                sb.append(n.getTextContent());

            // Appending element like links
            } else if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element childElem = (Element) n;
                String childLocal = childElem.getLocalName();
                String childNameSpace = childElem.getNamespaceURI();

                // Formats basic subsection titles
                if ("title".equals(childLocal) && DOCBOOK_NS.equals(childNameSpace)) {
                    String txt = getPlainTextWithLinks(childElem).trim();
                    if (!txt.isBlank()) {
                        sb.append("### ").append(txt).append("\n\n");
                    }

                    // Formats a link between two types
                } else if (DOCBOOK_NS.equals(childNameSpace) && "link".equals(childLocal)) {
                    String inner = getPlainTextWithLinks(childElem).trim();
                    String href = childElem.getAttributeNS(XLINK_NS, "href");

                    // href means brackets type
                    if (!href.isBlank()) {
                        sb.append(inner).append(" <").append(href).append(">");
                    } else {
                        sb.append(inner);
                    }

                    // Formats code
                } else if ("code".equals(childLocal) && DOCBOOK_NS.equals(childNameSpace)) {
                    String codeText = getPlainTextWithLinks(childElem).trim();
                    sb.append("`").append(codeText).append("`");

                } else {
                    sb.append(getPlainTextWithLinks(childElem));
                }
            }
        }
        return sb.toString().replaceAll("\\s+", " ").trim();
    }
}
