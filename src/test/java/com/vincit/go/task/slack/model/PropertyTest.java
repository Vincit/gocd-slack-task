package com.vincit.go.task.slack.model;

import com.vincit.go.task.slack.utils.MessageFormatter;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PropertyTest {

    @Test(expected = RuntimeException.class)
    public void testRequiredGetValue_Null() {
        Property property = new Property(null, false, true);
        property.getValue();
    }

    @Test(expected = RuntimeException.class)
    public void testRequiredGetValue_Empty() {
        Property property = new Property("", false, true);
        property.getValue();
    }

    @Test
    public void testRequiredGetValueOr_Null() {
        Property property = new Property(null, false, true);
        assertThat(property.getValueOr("Value"), is("Value"));
    }

    @Test
    public void testRequiredGetValueOr_Empty() {
        Property property = new Property("", false, true);
        assertThat(property.getValueOr("Value"), is("Value"));
    }

    @Test
    public void testSubstitute() {
        Property property = new Property("$value", false, false);
        MessageFormatter formatter = new MessageFormatter();
        formatter.add("value", "another");

        Property substitute = property.substitute(formatter);

        assertThat(substitute.getValue(), is("another"));
    }

}