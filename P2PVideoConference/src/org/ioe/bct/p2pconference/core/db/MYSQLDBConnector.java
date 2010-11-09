/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core.db;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Kusum Adhikari
 * This class uses a Singelton pattern for a database connection
 */
public final class MYSQLDBConnector extends DBConnector {
    
    private static MYSQLDBConnector connector=new MYSQLDBConnector();

  
    private MYSQLDBConnector() {
        initialize();
    }

    //default connector for username 'root' and password ''
    public static MYSQLDBConnector getInstance(String dbName) {
        connector.dbName=dbName;
        return connector;
    }

    //for specific username and password for database connection
    public static MYSQLDBConnector getInstance(String dbName,String username, String password) {
        connector.dbName=dbName;
        connector.username=username;
        connector.password=password;
        return connector;
    }

    @Override
    public void connect() throws SQLException, ClassNotFoundException{
        Class.forName(driverName);
        conn=DriverManager.getConnection(URI);
        conn.setAutoCommit(false);
        stm=conn.createStatement();
        
    }

   

    @Override
    public void initDriver() {
       driverName="com.mysql.jdbc.driver";
       URI="jdbc:mysql://localhost:3306/"+dbName;
    }
    
    
}
