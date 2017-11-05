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
          PopulateVariableTable();
          break;

        case "dump":
          DumpCategoryTable();
          DumpExpenseTable();
          DumpVariableTable();
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
      String sql;

      logMsg = String.format("In CreateETDB");
      System.out.println(logMsg);

      Connection conn = null; 
      Statement stmt  = null; 
      try { 

         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER); 
             
         //STEP 2: Open a connection 
         //System.out.println("Connecting to database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS);  
         
         //STEP 3: Execute a query 
         stmt = conn.createStatement(); 

         sql =  
            "CREATE TABLE category (" + 
            "id IDENTITY, " + 
            "code VARCHAR(60) NOT NULL " +  
            ")";  
         stmt.executeUpdate(sql);
         System.out.println("Created: [category] table."); 

         sql =  
            "CREATE TABLE expense (" + 
            "id IDENTITY, " + 
            "date_incurred DATE NOT NULL DEFAULT TODAY, " +  
            "amount DECIMAL(20,2) NOT NULL, " +  
            "category_id INTEGER NOT NULL, " +  
            "memo VARCHAR(60) NOT NULL DEFAULT 'N' " +  
            ")";  
         stmt.executeUpdate(sql);
         System.out.println("Created: [expense] table."); 

         sql =  
            "CREATE TABLE variable (" + 
            "id IDENTITY, " + 
            "name VARCHAR(40) NOT NULL, " +  
            "value VARCHAR(80) NOT NULL " +  
            ")";  
         stmt.executeUpdate(sql);
         System.out.println("Created: [variable] table."); 

         sql =  
            "ALTER TABLE expense " + 
            "ADD FOREIGN KEY (category_id) " + 
            "REFERENCES category(id) ";
         stmt.executeUpdate(sql);
         System.out.println("Created: [FK from expense table to category table]."); 

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
   private static void DumpCategoryTable() {

      String logMsg;
      String sql;

      logMsg = String.format("In DumpCategoryTable");
      System.out.println(logMsg);

      Connection conn = null; 
      Statement stmt  = null; 
      try { 
         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER); 
         
         // STEP 2: Open a connection 
         //System.out.println("Connecting to database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS);  
         
         // STEP 3: Execute a query 
         // System.out.println("Connected database successfully..."); 
         stmt = conn.createStatement(); 

         String header = "ID       CODE";
         System.out.println(header); 

         // Below, we are going to SELECT all fields even if we are not displaying 
         // them further down. We want to keep this SELECT complete and in synch
         // with the CREATION.
         sql  = "SELECT "; 
         sql += "id, code ";
         sql += "FROM category "; 

         ResultSet rs = stmt.executeQuery(sql); 
         
         // STEP 4: Extract data from result set 
         while(rs.next()) { 
            // Retrieve by column name 
            int id       = rs.getInt("id"); 
            String code  = rs.getString("code"); 

            String out_str = String.format("%08d %s", id, code);
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
   private static void DumpExpenseTable() {

      String logMsg;
      String sql;

      logMsg = String.format("In DumpExpenseTable");
      System.out.println(logMsg);

      Connection conn = null; 
      Statement stmt  = null; 
      try { 
         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER); 
         
         // STEP 2: Open a connection 
         //System.out.println("Connecting to database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS);  
         
         // STEP 3: Execute a query 
         //System.out.println("Connected database successfully..."); 
         stmt = conn.createStatement(); 

         String header = "ID       CATEGORY    DATE        AMOUNT MEMO";
         System.out.println(header); 

         // Below, we are going to SELECT all fields even if we are not displaying 
         // them further down. We want to keep this SELECT complete and in synch
         // with the CREATION.
         sql  = "SELECT "; 
         sql += "id, date_incurred, amount, category_id, memo ";
         sql += "FROM expense"; 

         ResultSet rs = stmt.executeQuery(sql); 
         
         // STEP 4: Extract data from result set 
         while(rs.next()) { 
            // Retrieve by column name 
            int id               = rs.getInt("id"); 
            String dt_incurred   = rs.getString("date_incurred"); 
            String amount        = rs.getString("amount"); 
            int category_id      = rs.getInt("category_id"); 
            String memo          = rs.getString("memo"); 

            String out_str = String.format("%08d %08d %-10s %10s %s", id, category_id, dt_incurred, amount, memo);
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
   private static void DumpVariableTable() {

      String logMsg;
      String sql;

      logMsg = String.format("In DumpVariableTable");
      System.out.println(logMsg);

      Connection conn = null; 
      Statement stmt  = null; 
      try { 
         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER); 
         
         // STEP 2: Open a connection 
         //System.out.println("Connecting to database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS);  
         
         // STEP 3: Execute a query 
         //System.out.println("Connected database successfully..."); 
         stmt = conn.createStatement(); 

         String header = "ID       NAME                                     VALUE";
         System.out.println(header); 

         // Below, we are going to SELECT all fields even if we are not displaying 
         // them further down. We want to keep this SELECT complete and in synch
         // with the CREATION.
         sql  = "SELECT "; 
         sql += "id, name, value ";
         sql += "FROM variable"; 

         ResultSet rs = stmt.executeQuery(sql); 
         
         // STEP 4: Extract data from result set 
         while(rs.next()) { 
            // Retrieve by column name 
            int id       = rs.getInt("id"); 
            String name  = rs.getString("name"); 
            String value = rs.getString("value"); 

            String out_str = String.format("%08d %-40s %s", id, name, value);
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
         //System.out.println("Connecting to a selected database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS); 
         //System.out.println("Connected database successfully..."); 
         
         // STEP 3: Execute a query 
         stmt = conn.createStatement();  

         sql = "INSERT INTO category VALUES (default, 'FOOD')";
         stmt.executeUpdate(sql); 
         sql = "INSERT INTO category VALUES (default, 'ENTERTAINMENT')";
         stmt.executeUpdate(sql); 
         sql = "INSERT INTO category VALUES (default, 'TRANSPORTATION-FUEL')";
         stmt.executeUpdate(sql); 
         sql = "INSERT INTO category VALUES (default, 'TRANSPORTATION-MAINT')";
         stmt.executeUpdate(sql); 
         System.out.println("Inserted records into: [category]."); 

         sql = "INSERT INTO expense " + 
                "VALUES (" +
                "default, default, 10.10, 1, default" +
                ")"; 
         stmt.executeUpdate(sql); 

         sql = "INSERT INTO expense " + 
                "VALUES (" +
                "default, default, 20.20, 2, 'Lame Movie'" +
                ")"; 
         stmt.executeUpdate(sql); 

         sql = "INSERT INTO expense " + 
                "VALUES (" +
                "default, default, 30.30, 3, '012345678901234567890123456789012345678901234567890123456789'" +
                ")"; 
         stmt.executeUpdate(sql); 

         sql = "INSERT INTO expense " + 
                "VALUES (" +
                "default, default, 9999999.99, 4, 'Very big repair'" +
                ")"; 
         stmt.executeUpdate(sql); 

         System.out.println("Inserted records into: [expense]."); 
         
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
   private static void PopulateVariableTable() { 

      String logMsg;
      String sql;

      logMsg = String.format("In PopulateVariableTable");
      System.out.println(logMsg);

      Connection conn = null; 
      Statement stmt  = null; 

      try{
         // STEP 1: Register JDBC driver 
         Class.forName(JDBC_DRIVER);  
         
         // STEP 2: Open a connection 
         //System.out.println("Connecting to a selected database..."); 
         conn = DriverManager.getConnection(DB_URL,USER,PASS); 
         //System.out.println("Connected database successfully..."); 
         
         // STEP 3: Execute a query 
         stmt = conn.createStatement();  

         sql = "INSERT INTO variable VALUES (default, 'version', '1.0')";
         stmt.executeUpdate(sql); 
         System.out.println("Inserted records into: [version]."); 

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

