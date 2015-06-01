package hello;
 
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

@Deprecated
public class JDBCDataBaseHandling {
	
  public static void main(String[] Args) throws Exception {
	  String PlacesSelected = SelectPlacesByTheme(connect(),"amusement");
	  
	  System.out.println(PlacesSelected);
  }
  
  @Deprecated
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
		String url = "jdbc:mysql://localhost/my.tour.guide";
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
  
  @Deprecated
  public static String SelectPlacesByTheme(Connection connection, String nomTheme) throws Exception{
	  
	  if(connection !=null){
		  System.out.println("SelectPlacesByTheme : Connection to DB is OK !");
		  
		  Statement stmt = null;
		  ResultSet rs = null;
		  List<String[]> SelectedPlaces;
		  String placesJson = null;
		  //String Query="SELECT description, longitude, latitude FROM Lieu WHERE nom_theme = '"+nomTheme+"'";
		  String Query = "SELECT Lieu.description, Lieu.longitude, Lieu.latitude "
	    			+ "FROM Lieu, Theme, Lieux_Themes "
	    			+ "WHERE Lieu.id_lieu = Lieux_Themes.id_lieu "
	    			+ "AND Lieux_Themes.id_theme = Theme.id_theme "
	    			+ "AND Theme.nom_theme= '"+nomTheme+"'";
		  try {
		      stmt = connection.createStatement();
		      rs = stmt.executeQuery(Query);  
		      SelectedPlaces = new ArrayList<String[]>();
		      
		      JSONArray jsonArraySample = Convertor.convertToJSON(rs);
		      
		      /*
		       * Older method to parse Array to JSON.
		       * As Convertor.convertToJSON() seems to be ok, we comment it.
		       */
		      // Now do something with the ResultSet ....
		      /*
		      while (rs.next()) {
		            double placeLatitude = rs.getDouble("latitude");
		            double placeLongitude = rs.getDouble("longitude");
		            String placeDescription = rs.getString("description");
		            
		            
		            SelectedPlaces.add(new String[]{
		            		"latitude"+'"'+':'+'"'+String.valueOf(placeLatitude),
		            		"longitude"+'"'+':'+'"'+String.valueOf(placeLongitude), 
		            		"description"+'"'+':'+'"'+placeDescription.toString()
		            		});
		            
		            
		            
		            //System.out.println(placeLatitude+" "+placeLongitude+" "+placeDescription);
		                  
		        }
		      
		      placesJson = new Gson().toJson(SelectedPlaces);
		      */
		      System.out.println(jsonArraySample.toString());
		      
		      
		      //return placesJson;
		      return jsonArraySample.toString();
		     
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
