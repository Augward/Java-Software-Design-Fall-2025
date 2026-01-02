import java.nio.file.*;

// Used for traveling though XML documents
import org.w3c.dom.*;

/**
 * Conversion file between xml and markdown file types
 * <p>
 *     File takes in an input and output path.  These carry the .fml file and send the ascii (txt) file
 *     to its destination.  The file extends a class full of helper methods called AllDocBookConverter
 *     and implements FileConverter.  It does this by overriding the convert method,
 * </p>
 *
 * @author augward
 */
public class DocBookToMarkdown extends AllDocBookConverter implements FileConverter {

    /**
     * Overridden method from FileConverter, reads XML, transforms into Markdown, and writes it
     *
     * @param input path to the input DocBook XML file
     * @param output path where Markdown will be written
     * @throws Exception is thrown when there is a parsing error or writing to file
     */
    @Override
    public void convert(Path input, Path output) throws Exception {
        // Parses XML into a dom document
        Document doc = parseXml(input);

        StringBuilder md = new StringBuilder();

        // Finds document title and adds to string builder
        String docTitle = getInfoTitle(doc);
        md.append("# ").append(docTitle.trim()).append("\n\n");

        // Finds the root element and travels through tree from root
        Element root = doc.getDocumentElement();
        traverse(root, md, 1);

        // Writes to file in Markdown format
        Files.writeString(output, md.toString());
    }

    /**
     * Traverses children of the given node and appends to Markdown
     *
     * @param parent current node to travers
     * @param out String builder receiving already generate MMarkdown
     * @param level current section level, top starts at 1
     */
    private void traverse(Node parent, StringBuilder out,  int level) {
        // Get all child nodes of the parent element
        NodeList children = parent.getChildNodes();

        // Iterate through children
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) continue;

            // Cast the node to Element for element-specific methods
            Element childElem = (Element) child;
            String localName = childElem.getLocalName();
            String nameSpace = childElem.getNamespaceURI();

            // This checks if it is a DocBook <section>
            if (DOCBOOK_NS.equals(nameSpace) && "section".equals(localName)) {
                // Extract the first <title>
                String title = getFirstChildText(childElem, "title");
                if (title != null && !title.isBlank()) {

                    out.append("\n");
                    out.append("#".repeat(Math.max(0, level)));
                    out.append(" ").append(title.trim()).append("\n\n");
                }

                // Recurse to process children
                traverse(childElem, out, level + 1);


                // This checks if it is a DocBook <para>
            } else if (DOCBOOK_NS.equals(nameSpace) && "para".equals(localName)) {
                // Other method takes care of converting it
                String text = renderInlineNodes(childElem);
                if (!text.isBlank()) {
                    out.append(text.trim()).append("\n\n");
                }


                // This checks if it is an ordered list
            } else if (DOCBOOK_NS.equals(nameSpace) && "orderedlist".equals(localName)) {
                // Convert ordered list to Markdown ordered list
                renderOrderedList(childElem, out);
                out.append("\n");


                // This checks if it is code
            } else if (DOCBOOK_NS.equals(nameSpace) && "programlisting".equals(localName)) {

                StringBuilder code = new StringBuilder();
                NodeList codeKids = childElem.getChildNodes();

                for (int k = 0; k < codeKids.getLength(); k++) {
                    Node ck = codeKids.item(k);
                    if (ck.getNodeType() == Node.TEXT_NODE || ck.getNodeType() == Node.CDATA_SECTION_NODE) {
                        code.append(ck.getTextContent());
                    } else if (ck.getNodeType() == Node.ELEMENT_NODE) {
                        code.append(renderInlineNodes(ck));
                    }
                }

                String block = code.toString().replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                if (!block.isBlank()) {
                    out.append("```");
                    out.append("\n").append(block).append("\n\n");
                }


            } else {
                // For any other element
                traverse(childElem, out, level);
            }
        }
    }

    /**
     * Rending inline children line text, special looking words, and links
     *
     * @param parent node that's children/text should be added to Markdown
     * @return rendered text for Markdown
     */
    private String renderInlineNodes(Node parent) {
        StringBuilder sb = new StringBuilder();

        NodeList nl = parent.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node child = nl.item(i);

            switch (child.getNodeType()) {
                case Node.TEXT_NODE:
                    // Plain text
                    sb.append(child.getTextContent());
                    break;

                case Node.ELEMENT_NODE:
                    // Element node: inspect namespace and local name to decide handling
                    Element childElem = (Element) child;
                    String nameSpace = childElem.getNamespaceURI();
                    String localName = childElem.getLocalName();

                    // Handle DocBook <emphasis>
                    if (DOCBOOK_NS.equals(nameSpace) && "emphasis".equals(localName)) {
                        // attribute for words
                        String role = childElem.getAttribute("role");
                        String inner = renderInlineNodes(childElem).trim();

                        if ("strong".equalsIgnoreCase(role)) {
                            // Bold
                            sb.append("**").append(inner).append("**");
                        } else if ("strikethrough".equalsIgnoreCase(role)) {
                            // Strikethrough
                            sb.append("~~").append(inner).append("~~");
                        } else {
                            // Italic is default
                            sb.append("*").append(inner).append("*");
                        }


                        // Handle DocBook <link>
                    } else if (DOCBOOK_NS.equals(nameSpace) && "link".equals(localName)) {
                        // Get URL using href
                        String href = childElem.getAttributeNS(XLINK_NS, "href");
                        String text = renderInlineNodes(childElem).trim();
                        if (text.isBlank()) text = href;

                        String role = childElem.getAttribute("role");
                        boolean angleBrackets = role.equalsIgnoreCase("uri") || !href.isBlank() && href.equals(text);

                        if (!href.isBlank()) {
                            if (angleBrackets) {
                                sb.append("<").append(href).append(">");
                            } else {
                                String title = childElem.getAttribute("title");
                                if (!title.isBlank()) {
                                    sb.append("[").append(text).append("](").append(href).append(" \"").append(title).append("\")");
                                } else {
                                    sb.append("[").append(text).append("](").append(href).append(")");
                                }
                            }
                        } else {
                            sb.append(text);
                        }
                    } else {
                        sb.append(renderInlineNodes(childElem));
                    }
            }
        }
        return sb.toString();
    }

    /**
     * Render and orderedlist element as a numbered list
     *
     * @param orderedList the element order list
     * @param out a String Builder to output the special list too
     */
    private void renderOrderedList(Element orderedList, StringBuilder out) {
        // Get all elements in ordered list
        NodeList items = orderedList.getElementsByTagNameNS(DOCBOOK_NS, "listitem");

        for (int i = 0; i < items.getLength(); i++) {
            Element level = (Element) items.item(i);

            // Retrieves text from each item in list
            String text = getFirstChildText(level, "para");
            if (text == null) text = renderInlineNodes(level);

            // Prints numbers: 1)
            out.append((i + 1)).append(") ").append(text.trim()).append("\n");
        }
    }
}
