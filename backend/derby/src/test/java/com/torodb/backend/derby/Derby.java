package com.torodb.backend.derby;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.torodb.backend.SqlInterface;
import com.torodb.backend.driver.derby.DerbyDbBackendConfiguration;
import com.torodb.backend.meta.TorodbSchema;

public class Derby {

	public static Module getConfigurationModule() {
	    return new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(DerbyDbBackendConfiguration.class)
                    .toInstance(new DerbyDbBackendConfiguration() {
                        @Override
                        public String getUsername() {
                            return null;
                        }
                        
                        @Override
                        public int getReservedReadPoolSize() {
                            return 4;
                        }
                        
                        @Override
                        public String getPassword() {
                            return null;
                        }
                        
                        @Override
                        public int getDbPort() {
                            return 0;
                        }
                        
                        @Override
                        public String getDbName() {
                            return "torodb";
                        }
                        
                        @Override
                        public String getDbHost() {
                            return null;
                        }
                        
                        @Override
                        public long getCursorTimeout() {
                            return 8000;
                        }
                        
                        @Override
                        public long getConnectionPoolTimeout() {
                            return 10000;
                        }
                        
                        @Override
                        public int getConnectionPoolSize() {
                            return 8;
                        }

                        @Override
                        public boolean inMemory() {
                            return false;
                        }

                        @Override
                        public boolean embedded() {
                            return true;
                        }
                    });
            }
        };
	}
	
	public static void cleanDatabase(SqlInterface sqlInterface) throws SQLException {
		try (Connection connection = sqlInterface.createSystemConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables("%", "%", "%", null);
            while (tables.next()) {
                String schemaName = tables.getString("TABLE_SCHEM");
                String tableName = tables.getString("TABLE_NAME");
                if (sqlInterface.isAllowedSchemaIdentifier(schemaName) || schemaName.equals(TorodbSchema.TORODB_SCHEMA)) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE \"" + schemaName + "\".\"" + tableName + "\"")) {
                        preparedStatement.executeUpdate();
                    }
                }
            }
            ResultSet schemas = metaData.getSchemas();
            while (schemas.next()) {
                String schemaName = schemas.getString("TABLE_SCHEM");
                if (sqlInterface.isAllowedSchemaIdentifier(schemaName) || schemaName.equals(TorodbSchema.TORODB_SCHEMA)) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement("DROP SCHEMA \"" + schemaName + "\" RESTRICT")) {
                        preparedStatement.executeUpdate();
                    }
                }
            }
            connection.commit();
        }
	}
}
