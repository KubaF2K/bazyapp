package org.example.bazyapp;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import java.sql.SQLException;

public class DBConn {
    private final String DB_URL="jdbc:oracle:thin:@db202203141259_medium?TNS_ADMIN=Wallet_DB202203141259/";
    private final String DB_USER = "ADMIN";
    private final String CONN_FACTORY_CLASS_NAME = "oracle.jdbc.pool.OracleDataSource";
    private final String DB_PASSWORD;

    private PoolDataSource pds;

    public PoolDataSource getDataSource() {
        if (pds == null){
            pds = PoolDataSourceFactory.getPoolDataSource();
            try {
                pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
                pds.setURL(DB_URL);
                pds.setUser(DB_USER);
                pds.setPassword(DB_PASSWORD);
                pds.setConnectionPoolName("JDBC_UCP_POOL");
                pds.setInitialPoolSize(5);
                pds.setMinPoolSize(5);
                pds.setMaxPoolSize(20);
            }
            catch (SQLException e) {
                System.out.println("SQLException:\n" + e.getMessage());
                System.exit(1);
            }
        }
        return pds;
    }

    public DBConn(String db_password) {
        DB_PASSWORD = db_password;
    }
}
