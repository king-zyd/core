package com.zyd.core.platform.management.web;

import com.zyd.core.json.JSONBinder;
import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.platform.cache.CacheGroup;
import com.zyd.core.platform.cache.DefaultCacheKeyGenerator;
import com.zyd.core.platform.exception.InvalidRequestException;
import com.zyd.core.platform.exception.ResourceNotFoundException;
import com.zyd.core.platform.management.web.view.CacheGroupView;
import com.zyd.core.platform.management.web.view.CacheGroupsView;
import com.zyd.core.platform.management.web.view.CacheItemView;
import com.zyd.core.platform.web.request.RequestContext;
import com.zyd.core.platform.web.rest.RESTController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * @author neo
 */
@Controller
public class CacheManagementController extends RESTController {
    private final Logger logger = LoggerFactory.getLogger(CacheManagementController.class);
    private SpringObjectFactory springObjectFactory;
    private RequestContext requestContext;

    @RequestMapping(value = "/management/cache/groups", method = RequestMethod.GET)
    @ResponseBody
    public CacheGroupsView getCacheGroups() {
        CacheManager cacheManager = getBean(CacheManager.class);
        CacheGroupsView result = new CacheGroupsView();
        for (String group : cacheManager.getCacheNames()) {
            CacheGroup cacheGroup = (CacheGroup) cacheManager.getCache(group);
            CacheGroupView view = createCacheGroupView(cacheGroup);
            result.getGroups().add(view);
        }
        return result;
    }

    @RequestMapping(value = "/management/cache/group/{group}", method = RequestMethod.PUT)
    @ResponseBody
    public CacheGroupView refreshCacheGroup(@PathVariable("group") String group) {
        Cache cache = getCacheGroup(group);
        cache.clear();
        logger.info("refresh cache group, group={}, updatedBy={}", group, requestContext.getRemoteAddress());
        return createCacheGroupView((CacheGroup) cache);
    }

    // only support json because XMLBinder doesn't support dynamic Object field type
    @RequestMapping(value = "/management/cache/item/{group}/{key}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public String getCacheItem(@PathVariable("group") String group, @PathVariable("key") String key) {
        String encodedKey = encodeKey(key);
        Cache cache = getCacheGroup(group);
        Cache.ValueWrapper wrapper = cache.get(encodedKey);
        if (wrapper == null)
            throw new ResourceNotFoundException("cache item does not exist, group=" + group + ", key=" + key);
        Object object = wrapper.get();
        CacheItemView item = new CacheItemView();
        item.setGroup(group);
        item.setKey(key);
        item.setValue(object);
        return JSONBinder.binder(CacheItemView.class).indentOutput().toJSON(item);
    }

    @RequestMapping(value = "/management/cache/item/{group}/{key}", method = RequestMethod.DELETE)
    @ResponseBody
    public String clearCacheItem(@PathVariable("group") String group, @PathVariable("key") String key) {
        String encodedKey = encodeKey(key);
        Cache cache = getCacheGroup(group);
        Cache.ValueWrapper wrapper = cache.get(encodedKey);
        if (wrapper == null)
            throw new ResourceNotFoundException("cache item does not exist, group=" + group + ", key=" + key);
        logger.info("clear cache, group={}, key={}, updatedBy={}", group, encodedKey, requestContext.getRemoteAddress());
        cache.evict(encodedKey);
        return String.format("cache item removed, group=%s, key=%s", group, encodedKey);
    }

    private CacheGroupView createCacheGroupView(CacheGroup cacheGroup) {
        CacheGroupView view = new CacheGroupView();
        view.setName(cacheGroup.getName());
        view.setRevision(cacheGroup.getRevision());
        view.setLastRefreshed(cacheGroup.getLastRefreshed());
        return view;
    }

    private Cache getCacheGroup(String group) {
        CacheManager cacheManager = getBean(CacheManager.class);
        Cache cache = cacheManager.getCache(group);
        if (cache == null) throw new ResourceNotFoundException("cache group does not exist, group=" + group);
        return cache;
    }

    private String encodeKey(String key) {
        DefaultCacheKeyGenerator keyGenerator = getBean(DefaultCacheKeyGenerator.class);
        return keyGenerator.encodeKey(key);
    }

    private <T> T getBean(Class<T> beanClass) {
        try {
            return springObjectFactory.getBean(beanClass);
        } catch (NoSuchBeanDefinitionException e) {
            throw new InvalidRequestException("cache is not used in this application", e);
        }
    }

    @Inject
    public void setSpringObjectFactory(SpringObjectFactory springObjectFactory) {
        this.springObjectFactory = springObjectFactory;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }
}
