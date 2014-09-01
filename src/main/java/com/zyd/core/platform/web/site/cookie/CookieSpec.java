package com.zyd.core.platform.web.site.cookie;

import com.zyd.core.collection.Key;
import com.zyd.core.util.ReadOnly;
import com.zyd.core.util.TimeLength;

/**
 * @author neo
 */
public final class CookieSpec<T> {
    private static final TimeLength MAX_AGE_SESSION_SCOPE = TimeLength.seconds(-1);

    public static <T> CookieSpec<T> spec(Key<T> nameKey) {
        return new CookieSpec<>(nameKey);
    }

    private final Key<T> nameKey;
    private final ReadOnly<String> path = new ReadOnly<>();
    private final ReadOnly<Boolean> httpOnly = new ReadOnly<>();
    private final ReadOnly<Boolean> secure = new ReadOnly<>();
    private final ReadOnly<TimeLength> maxAge = new ReadOnly<>();

    private CookieSpec(Key<T> nameKey) {
        this.nameKey = nameKey;
    }

    public Key<T> nameKey() {
        return nameKey;
    }

    public String path() {
        return path.value();
    }

    public Boolean isHTTPOnly() {
        return httpOnly.value();
    }

    public Boolean isSecure() {
        return secure.value();
    }

    public TimeLength maxAge() {
        return maxAge.value();
    }

    public boolean pathAssigned() {
        return path.assigned();
    }

    public boolean httpOnlyAssigned() {
        return httpOnly.assigned();
    }

    public boolean secureAssigned() {
        return secure.assigned();
    }

    public boolean maxAgeAssigned() {
        return maxAge.assigned();
    }

    public CookieSpec<T> httpOnly() {
        httpOnly.set(true);
        return this;
    }

    public CookieSpec<T> path(String path) {
        this.path.set(path);
        return this;
    }

    public CookieSpec<T> secure() {
        secure.set(true);
        return this;
    }

    public CookieSpec<T> maxAge(TimeLength maxAge) {
        this.maxAge.set(maxAge);
        return this;
    }

    public CookieSpec<T> sessionScope() {
        maxAge.set(MAX_AGE_SESSION_SCOPE);
        return this;
    }
}
