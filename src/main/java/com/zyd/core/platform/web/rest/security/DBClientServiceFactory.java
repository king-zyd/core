package com.zyd.core.platform.web.rest.security;

import com.zyd.core.database.JDBCAccess;
import com.zyd.core.platform.SpringObjectFactory;
import com.zyd.core.util.AssertUtils;

import javax.sql.DataSource;

/**
 * @author neo
 */
public class DBClientServiceFactory implements ClientServiceFactory {
    private DataSource authDataSource;

    //TODO(neo): create interface for ClientService for future multiple impl, by db, nosql or service
    @Override
    public ClientService create(SpringObjectFactory springObjectFactory) {
        AssertUtils.assertNotNull(authDataSource, "auth data source is required");
        JDBCAccess jdbcAccess = springObjectFactory.createBean(JDBCAccess.class);
        jdbcAccess.setDataSource(authDataSource);
        ClientRepository clientRepository = springObjectFactory.createBean(ClientRepository.class);
        clientRepository.setJDBCAccess(jdbcAccess);
        ClientService clientService = springObjectFactory.createBean(ClientService.class);
        clientService.setClientRepository(clientRepository);
        return clientService;
    }

    public void setAuthDataSource(DataSource authDataSource) {
        this.authDataSource = authDataSource;
    }
}
