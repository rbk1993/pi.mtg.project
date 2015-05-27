package hello;
 
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

 
public class JDBCDataBaseHandling {
	
  public static void main(String[] Args){
	  String PlacesSelected = SelectPlacesByTheme(connect(),"culture");
	  System.out.println(PlacesSelected);
  }
 
  public static Connection connect() {
 
	System.out.println("-------- MySQL JDBC Connection Testing ------------");
 
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();
		return null;
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
		return null;
	}
 
	if (connection != null) {
		System.out.println("You made it, take the control of your database now!");
		return connection;
		
	} else {
		System.out.println("Failed to make connection!");
		return null;
	}
  }
  
  public static String SelectPlacesByTheme(Connection connection, String nomTheme){
	  
	  if(connection !=null){
		  System.out.println("SelectPlacesByTheme : Connection to DB is OK !");
		  
		  Statement stmt = null;
		  ResultSet rs = null;
		  List<String[]> SelectedPlaces;
		  String placesJson = null;
		  String Query="SELECT * FROM Lieu WHERE nom_theme = '"+nomTheme+"'";

		  try {
		      stmt = connection.createStatement();
		      rs = stmt.executeQuery(Query);  
		      SelectedPlaces = new ArrayList<String[]>();
		      
		      // Now do something with the ResultSet ....
		      while (rs.next()) {
		            double placeLatitude = rs.getDouble("latitude");
		            double placeLongitude = rs.getDouble("longitude");
		            String placeDescription = rs.getString("description");
		            
		            SelectedPlaces.add(new String[]{String.valueOf(placeLatitude),
		            		String.valueOf(placeLongitude), placeDescription.toString()});
		            
		            //System.out.println(placeLatitude+" "+placeLongitude+" "+placeDescription);
		                  
		        }
		      
		      placesJson = new Gson().toJson(SelectedPlaces);
		      
		      //System.out.println(placesJson);
		      
		      return placesJson;
		     
		  }
		  catch (SQLException ex){
		      // handle any errors
		      System.out.println("SQLException: " + ex.getMessage());
		      System.out.println("SQLState: " + ex.getSQLState());
		      System.out.println("VendorError: " + ex.getErrorCode());
		      return null;
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
	  } else {
		  System.out.println("SelectPlacesByTheme : Given DB Connection is null !");
		  return null;
	  }
  }
}
