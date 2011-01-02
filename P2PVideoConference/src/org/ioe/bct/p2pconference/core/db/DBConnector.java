/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 *
 * @author kusu
 */
public abstract class DBConnector {

 
    protected  PreparedStatement  stm;
    protected  Connection conn;
    protected String URI;
    protected String username;
    protected String password;
    protected String driverName;
    protected String dbName;

    public DBConnector() {
        
    }

    public final void initialize() throws  SQLException,ClassNotFoundException{
        inituser();
        initDriver();
        connect();
    }

    public abstract void initDriver();
    public abstract void connect() throws SQLException,ClassNotFoundException;

    public Connection getConnection() {
        return conn;
    }

    public abstract PreparedStatement getStatement(String cred) throws SQLException;
       
    

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() throws SQLException {
        conn.rollback();
    }

    //default username and password for database connection
    public void inituser() {
        username="root";
        password="";
    }

    public void close() throws SQLException{
        stm.close();
        conn.close();

    }

}
