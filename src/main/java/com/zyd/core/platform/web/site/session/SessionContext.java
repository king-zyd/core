package com.zyd.core.platform.web.site.session;

import com.zyd.core.collection.Key;
import com.zyd.core.collection.KeyMap;
import com.zyd.core.util.ReadOnly;

/**
 * @author neo
 */
public class SessionContext {
    private final ReadOnly<String> id = new ReadOnly<>();
    private final KeyMap session = new KeyMap();
    private boolean changed;
    private boolean invalidated;

    public <T> T get(Key<T> key) {
        return session.get(key);
    }

    public <T> void set(Key<T> key, T value) {
        session.put(key, value);
        changed = true;
    }

    public void invalidate() {
        session.clear();
        changed = true;
        invalidated = true;
    }

    boolean changed() {
        return changed;
    }

    boolean invalidated() {
        return invalidated;
    }

    String getId() {
        return id.value();
    }

    void setId(String id) {
        this.id.set(id);
    }

    void loadSessionData(String sessionData) {
        session.deserialize(sessionData);
    }

    String getSessionData() {
        return session.serialize();
    }

    void saved() {
        changed = false;
    }

    void requireNewSessionId() {
        changed = true;
    }
}
