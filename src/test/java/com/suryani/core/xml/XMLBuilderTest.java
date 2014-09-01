package com.zyd.core.xml;

import com.zyd.core.util.StringUtils;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class XMLBuilderTest {
    @Test
    public void writeElements() {
        String xml = XMLBuilder.simpleBuilder()
                .startElement("parent")
                .startElement("child")
                .text("value")
                .endElement()
                .endElement().toXML();

        assertThat(xml, containsString("<parent>"));
        assertThat(xml, containsString("<child>value</child>"));
        assertThat(xml, containsString("</parent>"));
    }

    @Test
    public void writeAttributes() {
        XMLBuilder builder = XMLBuilder.simpleBuilder()
                .emptyElement("node")
                .attribute("id", "1")
                .attribute("name", "nodeName");

        String xml = builder.toXML();

        assertEquals("<node id=\"1\" name=\"nodeName\"/>", xml);
    }

    @Test
    public void formatOutput() {
        XMLBuilder builder = XMLBuilder.indentedXMLBuilder();
        builder.startElement("parent");

        builder.startElement("child1")
                .text("value1")
                .endElement();

        builder.startElement("child2")
                .startElement("child3")
                .text("value3")
                .endElement()
                .endElement();

        builder.textElement("child4", "value4");

        builder.endElement();   // end parent
        String xml = builder.toXML();

        assertEquals("<parent>\n"
                + "    <child1>value1</child1>\n"
                + "    <child2>\n"
                + "        <child3>value3</child3>\n"
                + "    </child2>\n"
                + "    <child4>value4</child4>\n"
                + "</parent>", xml);
    }

    @Test
    public void formatOutputWithEmptyElement() {
        XMLBuilder builder = XMLBuilder.indentedXMLBuilder();
        String xml = builder.startElement("parent")
                .emptyElement("child")
                .attribute("id", "1")
                .endElement()
                .toXML();

        assertEquals("<parent>\n"
                + "    <child id=\"1\"/>\n"
                + "</parent>", xml);
    }

    @Test
    public void formatOutputWithCData() {
        XMLBuilder builder = XMLBuilder.indentedXMLBuilder();
        String xml = builder.startElement("parent")
                .startElement("child")
                .attribute("id", "1")
                .cdata("value")
                .endElement()
                .endElement()
                .toXML();

        assertEquals("<parent>\n"
                + "    <child id=\"1\"><![CDATA[value]]></child>\n"
                + "</parent>", xml);
    }

    @Test
    public void allowNullTextValue() {
        String xml = XMLBuilder.simpleBuilder()
                .startElement("parent")
                .textElement("child-text", null)
                .startElement("child-cdata").cdata(null).endElement()
                .endElement()
                .toXML();

        Document doc = new XMLParser().parse(xml);
        assertFalse(StringUtils.hasText(XPathUtils.selectText(doc, "/parent/child-text")));
        assertFalse(StringUtils.hasText(XPathUtils.selectText(doc, "/parent/child-cdata")));
    }

    @Test
    public void allowNullAttributeValue() {
        String xml = XMLBuilder.simpleBuilder()
                .startElement("parent")
                .attribute("attr", null)
                .endElement()
                .toXML();

        Document doc = new XMLParser().parse(xml);
        assertFalse(StringUtils.hasText(XPathUtils.selectText(doc, "/parent/@attr")));
    }

    @Test
    public void writeRawXML() {
        String xml = XMLBuilder.indentedXMLBuilder()
                .startElement("parent")
                .rawXML("<child xmlns=\"http://test.org\">text</child>")
                .textElement("child-2", "text2")
                .endElement()
                .toXML();

        Document doc = new XMLParser().parse(xml);
        assertEquals("text", XPathUtils.selectText(doc, "/parent/child"));
    }
}
