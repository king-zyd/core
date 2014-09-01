package com.zyd.core.platform.web.rest.security;

import com.zyd.core.database.JDBCAccess;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author neo
 */
public class ClientRepository {
    public static final String CLIENT_STATUS_ACTIVE = "A";
    public static final String CLIENT_STATUS_INACTIVE = "I";

    private static final String FIND_SECRET_KEY_SQL = "select SecretKey from Client where ClientName = ? and Status = ?";

    private static final String FIND_PERMISSION_SQL = "select COUNT(*) from ClientPermission cp \n"
            + "inner join Client c on cp.ClientId = c.id\n"
            + "inner join Operation o on cp.OperationId = o.id\n"
            + "where c.ClientName = ? and o.System = ? and o.Operation = ?";

    private JDBCAccess jdbcAccess;

    @Cacheable(ClientAuthCacheConstants.CACHE_CLIENT_SECRET_KEY)
    public String findClientSecretKey(String clientId) {
        return jdbcAccess.findString(FIND_SECRET_KEY_SQL, clientId, CLIENT_STATUS_ACTIVE);
    }

    @Cacheable(ClientAuthCacheConstants.CACHE_CLIENT_PERMISSION)
    public boolean hasPermission(String clientId, String system, String operation) {
        return jdbcAccess.findInteger(FIND_PERMISSION_SQL, clientId, system, operation) > 0;
    }

    public void setJDBCAccess(JDBCAccess jdbcAccess) {
        this.jdbcAccess = jdbcAccess;
    }
}
