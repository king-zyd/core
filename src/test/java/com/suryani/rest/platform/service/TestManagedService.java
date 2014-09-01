package com.suryani.rest.platform.service;

import com.zyd.core.platform.service.ManagedService;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;

/**
 * @author neo
 */
@Service
@Singleton
public class TestManagedService extends ManagedService {
    @Override
    public void initialize() throws Throwable {
    }
}
