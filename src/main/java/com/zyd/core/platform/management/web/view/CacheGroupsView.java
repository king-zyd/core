package com.zyd.core.platform.management.web.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
@XmlRootElement(name = "cache-groups")
@XmlAccessorType(XmlAccessType.FIELD)
public class CacheGroupsView {
    @XmlElement(name = "group")
    private final List<CacheGroupView> groups = new ArrayList<>();

    public List<CacheGroupView> getGroups() {
        return groups;
    }
}
