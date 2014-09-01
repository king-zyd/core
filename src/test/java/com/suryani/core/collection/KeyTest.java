package com.zyd.core.collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

/**
 * @author neo
 */
public class KeyTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void targetClassCannotBeNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(Key.ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_NULL);

        new Key<List>("key", null);
    }

    @Test
    public void targetClassCannotBePrimitive() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(Key.ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_PRIMITIVE);

        new Key<>("key", int.class);
    }

    @Test
    public void targetClassCannotBeInterface() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(Key.ERROR_MESSAGE_TARGET_CLASS_CANNOT_BE_INTERFACE);

        new Key<>("key", List.class);
    }
}
