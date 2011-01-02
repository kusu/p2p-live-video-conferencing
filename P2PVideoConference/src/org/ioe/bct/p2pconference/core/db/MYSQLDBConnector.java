/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ioe.bct.p2pconference.core.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Kusum Adhikari
 * This class uses a Singelton pattern for a database connection
 */
public final class MYSQLDBConnector extends DBConnector {
    
    private static MYSQLDBConnector connector;

  
    private MYSQLDBConnector(){
       
     
    
    }

    //default connector for username 'root' and password ''
    synchronized  public static MYSQLDBConnector getInstance(String dbName){
        if(connector==null) {
            connector=new MYSQLDBConnector();
            connector.dbName=dbName;
        }
        
         
        return connector;
    }

    //for specific username and password for database connection
   synchronized  public static MYSQLDBConnector getInstance(String dbName,String username, String password) {
       
        if(connector==null) {
            
            connector=new MYSQLDBConnector();
           
        }
       
       
        connector.username=username;
        connector.password=password;
        return connector;
    }

    @Override
    public void connect() throws SQLException, ClassNotFoundException{
        Class.forName(driverName);
        conn=DriverManager.getConnection(URI,connector.username,connector.password);
        conn.setAutoCommit(false);
       
        
    }

    public PreparedStatement getStatement(String sql) throws SQLException{
        stm=conn.prepareStatement(sql);
        return stm;
    }

   

    @Override
    public void initDriver() {
       driverName="com.mysql.jdbc.Driver";
       URI="jdbc:mysql://localhost:3306/"+dbName;
    }


    
}
