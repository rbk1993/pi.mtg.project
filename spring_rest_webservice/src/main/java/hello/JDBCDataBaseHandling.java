package hello;
 
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

 
public class JDBCDataBaseHandling {
 
  public static void main(String[] argv) {
 
	System.out.println("-------- MySQL JDBC Connection Testing ------------");
 
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();
		return;
	}
 
	System.out.println("MySQL JDBC Driver Registered!");
	Connection connection = null;
 
	try {
		String url = "jdbc:mysql://localhost/mytourguide";
		String user = "root";
		String password = "root";
		connection = DriverManager.getConnection(url,user,password);
 
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
	}
 
	if (connection != null) {
		System.out.println("You made it, take the control of your database now!");
		
		
	} else {
		System.out.println("Failed to make connection!");
	}
  }
  
  public static String SelectPlacesByTheme(Connection connection, String nomTheme){
	  
	  if(connection !=null){
		  System.out.println("SelectPlacesByTheme : Connection to DB is OK !");
		  
		  Statement stmt = null;
		  ResultSet rs = null;
		  
		  String Query="SELECT * FROM Lieu WHERE nom_theme="+nomTheme;

		  try {
		      stmt = connection.createStatement();
		      rs = stmt.executeQuery(Query);
		      
		      // Now do something with the ResultSet ....
		      while (rs.next()) {
		            double coffeeName = rs.getDouble("latitude");
		            double supplierID = rs.getDouble("longitude");
		            String price = rs.getString("description");
		            
		  
		        }
		  }
		  catch (SQLException ex){
		      // handle any errors
		      System.out.println("SQLException: " + ex.getMessage());
		      System.out.println("SQLState: " + ex.getSQLState());
		      System.out.println("VendorError: " + ex.getErrorCode());
		  }
		  finally {
		      // it is a good idea to release
		      // resources in a finally{} block
		      // in reverse-order of their creation
		      // if they are no-longer needed

		      if (rs != null) {
		          try {
		              rs.close();
		          } catch (SQLException sqlEx) { } // ignore

		          rs = null;
		      }

		      if (stmt != null) {
		          try {
		              stmt.close();
		          } catch (SQLException sqlEx) { } // ignore

		          stmt = null;
		      }
		  }
		  
		  return null;
	  } else {
		  System.out.println("SelectPlacesByTheme : Given DB Connection is null !");
		  return null;
	  }
  }
}
