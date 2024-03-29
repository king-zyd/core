package com.zyd.core.xml;

import com.zyd.core.util.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author neo
 */
public class XMLBinderTest {
    @XmlRootElement(name = "mock-bean")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MockBean {
        String field1;
        Integer field2;
        MockChildBean child;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MockChildBean {
        Boolean field1;
    }

    @Test
    public void convertXMLToBean() {
        XMLBinder<MockBean> binder = XMLBinder.binder(MockBean.class);
        MockBean bean = binder.fromXML("<mock-bean><field1>field1Value</field1><field2>1</field2><child><field1>true</field1></child></mock-bean>");

        assertEquals("field1Value", bean.field1);
        assertTrue(bean.child.field1);
        assertEquals(1, bean.field2, 0);
    }

    @Test
    public void convertXMLToBeanWithUnmatchedFields() {
        XMLBinder<MockBean> binder = XMLBinder.binder(MockBean.class);
        MockBean bean = binder.fromXML("<mock-bean><field1>field1Value</field1><not-existing-field>field1Value</not-existing-field></mock-bean>");

        assertEquals("field1Value", bean.field1);
        assertNull("field2 should be in default value", bean.field2);
    }

    @Test
    public void convertXMLToBeanWithDuplicatedFields() {
        XMLBinder<MockBean> binder = XMLBinder.binder(MockBean.class);
        MockBean bean = binder.fromXML("<mock-bean><field1>field1Value</field1><field1>field2Value</field1></mock-bean>");

        assertEquals("JAXB uses last field", "field2Value", bean.field1);
    }

    @Test
    public void ignoreFieldsIfConvertingFails() {
        XMLBinder<MockBean> binder = XMLBinder.binder(MockBean.class);
        MockBean bean = binder.fromXML("<mock-bean><field2>invalidInt</field2></mock-bean>");
        assertNull(bean.field2);
    }

    @Test
    public void convertBeanToXML() {
        MockBean bean = new MockBean();
        bean.field1 = "field1Value";
        bean.field2 = 1;
        bean.child = new MockChildBean();
        bean.child.field1 = true;

        XMLBinder<MockBean> binder = XMLBinder.binder(MockBean.class);
        String xml = binder.toXML(bean);

        assertThat(xml, containsString("<mock-bean><field1>field1Value</field1><field2>1</field2><child><field1>true</field1></child></mock-bean>"));
    }

    @Test
    public void convertBeanToXMLShouldNotContainXMLDeclaration() {
        MockBean bean = new MockBean();
        XMLBinder<MockBean> binder = XMLBinder.binder(MockBean.class);
        String xml = binder.toXML(bean);
        assertThat(xml, not(containsString("<?xml")));
    }

    public static class BeanWithoutJAXBAnnotation {
        String field1;
        ChildBeanWithoutJAXBAnnotation child;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public ChildBeanWithoutJAXBAnnotation getChild() {
            return child;
        }

        public void setChild(ChildBeanWithoutJAXBAnnotation child) {
            this.child = child;
        }
    }

    public static class ChildBeanWithoutJAXBAnnotation {
        int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    @Test
    public void convertBeanWithoutJAXBAnnotationToXML() {
        BeanWithoutJAXBAnnotation bean = new BeanWithoutJAXBAnnotation();
        bean.setField1("field-value");
        ChildBeanWithoutJAXBAnnotation child = new ChildBeanWithoutJAXBAnnotation();
        child.setValue(100);
        bean.setChild(child);

        XMLBinder<BeanWithoutJAXBAnnotation> binder = XMLBinder.binder(BeanWithoutJAXBAnnotation.class);

        String xml = binder.toXML(bean);

        assertThat(xml, containsString("<BeanWithoutJAXBAnnotation><child><value>100</value></child><field1>field-value</field1></BeanWithoutJAXBAnnotation>"));
    }

    @Test
    public void convertXMLToBeanWithoutJAXBAnnotation() {
        String xml = "<BeanWithoutJAXBAnnotation><child><value>100</value></child><field1>field-value</field1></BeanWithoutJAXBAnnotation>";
        XMLBinder<BeanWithoutJAXBAnnotation> binder = XMLBinder.binder(BeanWithoutJAXBAnnotation.class);
        BeanWithoutJAXBAnnotation bean = binder.fromXML(xml);

        assertEquals("field-value", bean.getField1());
        assertEquals(100, bean.getChild().getValue());
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BeanWithDateField {
        Date date;
    }

    @Test
    public void convertDateFieldBeanToXML() {
        BeanWithDateField bean = new BeanWithDateField();
        bean.date = DateUtils.date(2009, Calendar.DECEMBER, 29, 12, 0, 0);

        XMLBinder<BeanWithDateField> binder = XMLBinder.binder(BeanWithDateField.class);
        String xml = binder.toXML(bean);

        Assert.assertThat(xml, JUnitMatchers.containsString("<BeanWithDateField><date>2009-12-29T12:00:00"));
        Assert.assertThat(xml, JUnitMatchers.containsString("</date></BeanWithDateField>"));
    }

    @Test
    public void convertXMLToDateFieldBean() {
        String xml = "<BeanWithDateField><date>2009-12-29</date></BeanWithDateField>";

        XMLBinder<BeanWithDateField> binder = XMLBinder.binder(BeanWithDateField.class);
        BeanWithDateField bean = binder.fromXML(xml);

        assertEquals(DateUtils.date(2009, Calendar.DECEMBER, 29), bean.date);
    }
}
