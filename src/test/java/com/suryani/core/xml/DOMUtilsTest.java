package com.zyd.core.xml;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

import static com.zyd.core.xml.DOMUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class DOMUtilsTest {
    @Test
    public void convertDocumentToText() {
        Document document = new XMLParser().parse("<xml>value</xml>");
        String xml = text(document);
        assertThat(xml, containsString("<xml>"));
        assertThat(xml, containsString("value"));
        assertThat(xml, containsString("</xml>"));
    }

    @Test
    public void getChildElements() {
        Document document = new XMLParser().parse("<xml><child1/>\n<child2>\t<inside/> </child2> \n\t <child3/>text</xml>");
        List<Element> elements = childElements(document.getDocumentElement());
        assertEquals(3, elements.size());
        assertEquals("child1", elements.get(0).getTagName());
        assertEquals("child2", elements.get(1).getTagName());
        assertEquals("child3", elements.get(2).getTagName());
    }

    @Test
    public void getChildElementsShouldReturnEmptyListIfNoChildElements() {
        Document document = new XMLParser().parse("<xml></xml>");
        List<Element> elements = childElements(document.getDocumentElement());
        Assert.assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void getChildren() {
        Document document = new XMLParser().parse("<xml><child1/><child2/></xml>");
        List<Node> nodes = children(document.getDocumentElement());
        assertEquals(2, nodes.size());
        assertEquals("child1", nodes.get(0).getNodeName());
        assertEquals("child2", nodes.get(1).getNodeName());
    }

    @Test
    public void getChildrenShouldIgnoreEmptyText() {
        Document document = new XMLParser().parse("<xml>        </xml>");
        List<Node> nodes = children(document.getDocumentElement());
        assertEquals(0, nodes.size());

        document = new XMLParser().parse("<xml>    <child/>    </xml>");
        nodes = children(document.getDocumentElement());
        assertEquals(1, nodes.size());
        assertEquals("child", nodes.get(0).getNodeName());
    }

    @Test
    public void toTextShouldKeepAllNamespaceDeclarations() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<envelope xmlns=\"http://example.org/envelope\"\n"
                + "    xmlns:data=\"http://example.org/data\"\n"
                + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                + "    <app:application/>"
                + "</envelope>";

        Document document = new XMLParser().parse(xml);

        String outputXML = text(document);

        assertThat(outputXML, containsString("<envelope xmlns=\"http://example.org/envelope\""));
    }

    @Test
    public void prettyFormatShouldSuppressXMLDeclaration() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<envelope xmlns=\"http://example.org/envelope\" xmlns:app=\"http://example.org/app\">   "
                + " <app:application1/>"
                + " <app:application2>someapp</app:application2>"
                + " <app:application3></app:application3>"
                + "</envelope>";

        String formattedXML = DOMUtils.prettyFormat(new XMLParser().parse(xml));

        assertEquals(false, formattedXML.contains("version=\"1.0\""));
        assertThat("application1 should ends with line break",
                formattedXML, containsString("<app:application1/>\n"));
    }
}
