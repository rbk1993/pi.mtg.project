package hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;

public class Services {
	/* This main method intents only to provide some tests on the services*/
	public static void main (String[] Args) throws Exception {
		
		String theme = "amusement";
    	String Query = "SELECT Lieu.description, Lieu.longitude, Lieu.latitude "
    			+ "FROM Lieu, Theme, Lieux_Themes "
    			+ "WHERE Lieu.id_lieu = Lieux_Themes.id_lieu "
    			+ "AND Lieux_Themes.id_theme = Theme.id_theme "
    			+ "AND Theme.nom_theme= '"+theme+"'";
    	
    	String userID = "99";
    	String dureeHr = "3";
    	String dureeMn = "30";
    	String date = "2015-09-01";
    	String horaire = "19:22:00";
    	String nbMembresMax = "50";
    	String ptRencontre = "5";
    	
    	int successCreation = Services.sendSQLData(
    			"INSERT INTO Groupe "
    			+"(id_administrateur, duree_parcours_heures, "
    			+ "duree_parcours_minutes, date, horaire, nombre_membres_max, "
    			+ "id_lieu_rencontre) "
    			+ "VALUES ('"+userID+"','"+dureeHr+"','"+dureeMn+"','"
    					+date+"','"+horaire+"','"+nbMembresMax+"','"+ptRencontre+"')"
    			,true,true);
    	
    	int successRegistration = Services.sendSQLData(
    			"INSERT INTO Membres_Groupes VALUES ('"+userID+"','"+successCreation+"')"
    			,true, false);
    	
		System.out.println(SQLQueryToJSON(Query));
		
		int auth1 = authenticateUser("rbk1993","12345");
		int auth2 = authenticateUser("rbk1993","22232");
		int auth3 = authenticateUser("pirateMan","12345");
		System.out.println("authenticateUser res 1 : "+auth1);
		System.out.println("authenticateUser res 2 : "+auth2);
		System.out.println("authenticateUser res 3 : "+auth3);
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
	
	public static String SQLQueryToJSON(String Query) throws Exception {
		
		  Connection connection = connect();
		  
		  if(connection !=null){
			  System.out.println("SQLQueryToJSON : Connection to DB is OK !");
			  
			  Statement stmt = null;
			  ResultSet rs = null;

			  try {
			      stmt = connection.createStatement();
			      rs = stmt.executeQuery(Query);  
			  
			      JSONArray jsonArraySample = MinorServices.convertToJSON(rs);
			      
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
	
	public static int sendSQLData(String Query, boolean update, boolean autogen) throws Exception {
		Connection connection = connect();
		  
		  if(connection !=null){
			  System.out.println("SQLQueryToJSON : Connection to DB is OK !");
			  
			  Statement stmt = null;
			  ResultSet rs = null;
			  int aiKey = 0;

			  try {
			      stmt = connection.createStatement();
			      if (update) {
				      if (autogen) {
				    	  stmt.executeUpdate(Query,Statement.RETURN_GENERATED_KEYS);
				    	  rs = stmt.getGeneratedKeys();
				    	  
				            if (rs.first()) {
				            	aiKey = rs.getInt(1);
				                System.out.println("Key returned from getGeneratedKeys():"
				                        + rs.getInt(1));
				            }
				            rs.close();
				    	  return aiKey;
				      } else {
				    	  stmt.executeUpdate(Query);
				    	  return 1;
				      }
			      } else {
				      stmt = connection.createStatement();
				      rs = stmt.executeQuery(Query);  
				      return 1;
			      }		     
			  }
			  catch (SQLException ex){
			      // handle any errors
			      System.out.println("SQLException: " + ex.getMessage());
			      System.out.println("SQLState: " + ex.getSQLState());
			      System.out.println("VendorError: " + ex.getErrorCode());
			      return 0;
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
			  return 0;
		  }
	}
	
	public static int authenticateUser (String username, String password) throws Exception{
		
		Connection connection = connect();
		  
		  if(connection !=null){
			  System.out.println("SQLQueryToJSON : Connection to DB is OK !");
			  
			  Statement stmt = null;
			  ResultSet rs = null;
			  
		    	String Query = "SELECT user_id "
		    			+ "FROM Utilisateur "
		    			+ "WHERE (login = '"+username+"' "
		    			+ "OR mail = '"+username+"') "
		    			+ " AND password = '"+password+"' ";

			  try {
			      stmt = connection.createStatement();
			      rs = stmt.executeQuery(Query);  
			      int userID = 0;
			      
			      //int total_rows = MinorServices.getRowsCount(rs);
			      boolean total_rows = rs.first();
			      if(total_rows == true) {
			    	  userID = rs.getInt("user_id");
			      } 
			      JSONArray jsonArraySample = MinorServices.convertToJSON(rs);

			      System.out.println(jsonArraySample);
			      
			      if(total_rows == true){
			    	  return userID;
			      } else return 0;
			     
			  }
			  catch (SQLException ex){
			      // handle any errors
			      System.out.println("SQLException: " + ex.getMessage());
			      System.out.println("SQLState: " + ex.getSQLState());
			      System.out.println("VendorError: " + ex.getErrorCode());
			      return 0;
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
			  return 0;
		  }
	}	
}

