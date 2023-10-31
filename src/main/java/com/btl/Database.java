package com.btl;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    
    /**
     * developing
     * @return 
     */
    public Connection connectDb() {
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/account", "root", "");
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
}
