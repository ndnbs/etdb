package com.github.ndnbs.etdb;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement;  

public class MainETDB { 
   // JDBC driver name and database URL 
   static final String JDBC_DRIVER = "org.h2.Driver";   
   static final String DB_URL      = "jdbc:h2:./support/db/etdb";  
   
   //  Database credentials 
   static final String USER = "sa"; 
   static final String PASS = ""; 
 
   /**************************************************************************/
   public static void main(String[] args) { 

      String logMsg;

      // Ensure that exactly one(1) mode is passed in of the allowable modes. 
      // Because of "short-circuit" behavior, the following works out nicely.
      if ((args.length != 1) || (!(args[0].equals("create") || args[0].equals("dump") || 
                                   args[0].equals("populate") || 
                                   args[0].equals("start")))) 
      {

         logMsg = String.format("Valid Mode NOT specified. Valid Modes are: [%s]", 
                                 "create | dump | populate | start");
         System.out.println(logMsg);
         System.exit(1);
      }

      logMsg = String.format("Mode is: [%s]", args[0]);
      System.out.println(logMsg);

      switch(args[0]) {

        case "create":
          CreateETDB();
          break;

        case "dump":
          DumpETDB();
          break;

        case "populate":
          PopulateETDB();
          break;

        case "start":
         logMsg = String.format("Mode: [%s] not implemented yet.", args[0]);
         System.out.println(logMsg);
          break;

        default:
         logMsg = String.format("Should never happen, but here for code maintainability");
         System.out.println(logMsg);
         System.exit(1);
      }

      System.exit(0);
   } 
   /**************************************************************************/
   private static void CreateETDB() {

      String logMsg;

      logMsg = String.format("In CreateETDB");
      System.out.println(logMsg);

      Connection conn = null; 
      Statement stmt  = null; 
      try { 
         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER); 
             
         //STEP 2: Open a connection 
         System.out.println("Connecting to database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS);  
         
         //STEP 3: Execute a query 
         System.out.println("Creating [os] table in given database..."); 
         stmt = conn.createStatement(); 
         String sql =  
            "CREATE TABLE os (" + 
            "id IDENTITY, " + 
            "c_dt TIMESTAMP NOT NULL DEFAULT NOW(0), " +  
            "d_dt TIMESTAMP NOT NULL DEFAULT NOW(0), " +  
            "port INTEGER NOT NULL, " +  
            "symb VARCHAR(255) NOT NULL, " +  
            "exch VARCHAR(255) NOT NULL, " +  
            "stat CHAR(1) NOT NULL DEFAULT 'H', " +  
            "pvom DECIMAL(20,2) NOT NULL DEFAULT 0.00, " +  
            "o_d DATE NOT NULL DEFAULT TODAY, " +  
            "o_a DECIMAL(20,2) NOT NULL, " +  
            "o_p DECIMAL(20,2) NOT NULL, " +  
            "s_d DATE NOT NULL DEFAULT TODAY, " +  
            "s_a DECIMAL(20,2) NOT NULL DEFAULT 0.00, " +  
            "s_p DECIMAL(20,2) NOT NULL DEFAULT 0.00, " +  
            "l_p DECIMAL(20,2) NOT NULL DEFAULT 0.00, " +  
            "l_p_dt TIMESTAMP NOT NULL DEFAULT NOW(0), " +  
            "l_p_mech CHAR(6) NOT NULL DEFAULT 'MANUAL', " +  
            "r0001_v CHAR(1) NOT NULL DEFAULT 'N', " +  
            "r0001_dt TIMESTAMP NOT NULL DEFAULT NOW(0), " +  
            "r0002_v CHAR(1) NOT NULL DEFAULT 'N', " +  
            "r0002_dt TIMESTAMP NOT NULL DEFAULT NOW(0)" +  
            ")";  

         stmt.executeUpdate(sql);
         System.out.println("Created [os] table in given database..."); 
         
         // STEP 4: Clean-up environment 
         stmt.close(); 
         conn.close(); 
      } catch(SQLException se) { 
         //Handle errors for JDBC 
         se.printStackTrace(); 
      } catch(Exception e) { 
         //Handle errors for Class.forName 
         e.printStackTrace(); 
      } finally { 
         //finally block used to close resources 
         try{ 
            if(stmt!=null) stmt.close(); 
         } catch(SQLException se2) { 
         } // nothing we can do 
         try { 
            if(conn!=null) conn.close(); 
         } catch(SQLException se){ 
            se.printStackTrace(); 
         } //end finally try 
      } //end try 
   } 
   /**************************************************************************/
   private static void DumpETDB() {

      String logMsg;
      String sql;

      logMsg = String.format("In DumpETDB");
      System.out.println(logMsg);

      Connection conn = null; 
      Statement stmt  = null; 
      try { 
         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER); 
         
         // STEP 2: Open a connection 
         System.out.println("Connecting to database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS);  
         
         // STEP 3: Execute a query 
         System.out.println("Connected database successfully..."); 
         stmt = conn.createStatement(); 

         String header = "ID  PORT  SYMB       EXCH       S    O_A      O_P   O_D        C_DT";
         System.out.println(header); 

         // Below, we are going to SELECT all fields even if we are not displaying 
         // them further down. We want to keep this SELECT complete and in synch
         // with the CREATION.
         sql  = "SELECT "; 
         sql += "id, c_dt, port, symb, exch, stat, pvom, o_d, o_a, o_p, ";
         sql += "s_d, s_a, s_p, l_p, l_p_dt, l_p_mech, r0001_v, r0001_dt, "; 
         sql += "r0002_v, r0002_dt "; 
         sql += "FROM os"; 

         ResultSet rs = stmt.executeQuery(sql); 
         
         // STEP 4: Extract data from result set 
         while(rs.next()) { 
            // Retrieve by column name 
            int id      = rs.getInt("id"); 
            String c_dt = rs.getString("c_dt"); 
            int port    = rs.getInt("port"); 
            String symb = rs.getString("symb"); 
            String exch = rs.getString("exch"); 
            String stat = rs.getString("stat"); 
            String o_d  = rs.getString("o_d"); 
            String o_a  = rs.getString("o_a"); 
            String o_p  = rs.getString("o_p"); 

            String out_str = String.format("%03d %04d  %-10s %-10s %s %8s %8s %s %s", 
                                           id, port, symb, exch, stat, o_a, o_p, o_d, c_dt); 
            // Display values 
            System.out.println(out_str); 
         } 
         // STEP 5: Clean-up environment 
         rs.close(); 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } catch(Exception e) { 
         // Handle errors for Class.forName 
         e.printStackTrace(); 
      } finally { 
         // finally block used to close resources 
         try { 
            if(stmt!=null) stmt.close();  
         } catch(SQLException se2) { 
         } // nothing we can do 
         try { 
            if(conn!=null) conn.close(); 
         } catch(SQLException se) { 
            se.printStackTrace(); 
         } // end finally try 
      } // end try 
   } 
   /**************************************************************************/
   private static void PopulateETDB() { 

      String logMsg;
      String sql;

      logMsg = String.format("In PopulateETDB");
      System.out.println(logMsg);

      Connection conn = null; 
      Statement stmt  = null; 

      try{
         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER);  
         
         // STEP 2: Open a connection 
         System.out.println("Connecting to a selected database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS); 
         System.out.println("Connected database successfully..."); 
         
         // STEP 3: Execute a query 
         stmt = conn.createStatement();  

         sql = "INSERT INTO os " + 
                "VALUES (" +
                "default, default, default, 13, 'AMZN', 'NASDAQ', default, default, default, 10.00, 13.13, " +
                "default, default, default, default, default, default, default, default, default, default" +
                ")"; 
         stmt.executeUpdate(sql); 

         sql = "INSERT INTO os " + 
                "VALUES (" +
                "default, default, default, 13, 'SPOK', 'NASDAQ', default, default, default, 8.00, 14.14, " +
                "default, default, default, default, default, default, default, default, default, default" +
                ")"; 
         stmt.executeUpdate(sql); 

         sql = "INSERT INTO os " + 
                "VALUES (" +
                "default, default, default, 13, 'LMT', 'NYSE', default, default, default, 99.00, 15.15, " +
                "default, default, default, default, default, default, default, default, default, default" +
                ")"; 
         stmt.executeUpdate(sql); 

         System.out.println("Inserted records into the table..."); 
         
         // STEP 4: Clean-up environment 
         stmt.close(); 
         conn.close(); 
      } catch(SQLException se) { 
         // Handle errors for JDBC 
         se.printStackTrace(); 
      } catch(Exception e) { 
         // Handle errors for Class.forName 
         e.printStackTrace(); 
      } finally { 
         // finally block used to close resources 
         try {
            if(stmt!=null) stmt.close();  
         } catch(SQLException se2) { 
         } // nothing we can do 
         try { 
            if(conn!=null) conn.close(); 
         } catch(SQLException se) { 
            se.printStackTrace(); 
         } // end finally try 
      } // end try 
   }
   /**************************************************************************/
}

