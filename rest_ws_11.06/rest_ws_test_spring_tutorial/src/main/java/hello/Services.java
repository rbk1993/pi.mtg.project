package hello;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			  
			      JSONArray jsonArraySample = MinorServices.resultSetToJSON(rs);
			      
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
	
	public static boolean checkIfExists (String item, String column, String table) throws Exception {

		Connection connection = connect();
		  
		  if(connection !=null){
			  System.out.println("SQLQueryToJSON : Connection to DB is OK !");
			  
			  Statement stmt = null;
			  ResultSet rs = null;
			  
		    	String Query = "SELECT "+column+" FROM "+table+" WHERE "+column+" = "+item;

			  try {
			      stmt = connection.createStatement();
			      rs = stmt.executeQuery(Query);  

			      boolean total_rows = rs.first();
			      if(total_rows == true) {
			    	  return true;
			      } else {
			    	  return false;
			      }

			  }
			  catch (SQLException ex){
			      // handle any errors
			      System.out.println("SQLException: " + ex.getMessage());
			      System.out.println("SQLState: " + ex.getSQLState());
			      System.out.println("VendorError: " + ex.getErrorCode());
			      return false;
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
			  return false;
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
			      JSONArray jsonArraySample = MinorServices.resultSetToJSON(rs);

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
	
public static HashMap<String, byte[]> LoadPicture(List<String> picturesToDL) throws IOException, JSONException{
		
		HashMap<String, byte[]> picturesHashMap = new HashMap<String, byte[]>();
		
		final File folder = new File("C:/IProject/pi.mtg.project-master/Images");
		List<File> myFiles = MinorServices.listFilesForFolder(folder);
		//System.out.println("myFiles.size() ="+myFiles.size());
		List<DataInputStream> listIS = new ArrayList<DataInputStream>();	
		int listis_count=-1;
		for(int i=0;i<myFiles.size();i++){
			
			//System.out.println("myFiles.get("+i+").getName() = "+myFiles.get(i).getName());
			if(picturesToDL.contains(myFiles.get(i).getName())) {
				
				//System.out.println(myFiles.get(i).getName());
				
				listIS.add(new DataInputStream (new FileInputStream (myFiles.get(i))));
				listis_count++;
				//System.out.println((int)myFiles.get(i).length());
				//System.out.println(myFiles.get(i).length());
				
				byte[] bytes = new byte[(int) myFiles.get(i).length()];
				
				for(int j = 0; /*(j<myFiles.get(i).length()) && */
						(listIS.get(listis_count).available() != 0);j++){
					
					bytes[j] = listIS.get(listis_count).readByte();
					
				}
				//System.out.println("DEBUG LOADP :"+myFiles.get(i).getName());
				//System.out.println("DEBUG LOADP :"+Arrays.toString(bytes));
				//picturesHashMap.put(myFiles.get(i).getName(), Arrays.toString(bytes));
				picturesHashMap.put(myFiles.get(i).getName(), bytes);
			}
		}
		return picturesHashMap;
}
	
	public static JSONObject CheckForUpdates(String deviceStockedPictures) throws Exception{
		
	    //final String TAG_NOMTHEME = "nom_theme";
	    final String TAG_ID = "id_theme";
	    //HashMap<Integer, String> ThemeHashMap = new HashMap<Integer, String>();
	    String id_values_concat = "0, ";
		Connection connection = connect();
		List<String> picturesToDownload = new ArrayList<String>();
		//HashMap<String, String> PictureHashMap = new HashMap<String, String>();
		
		  if(connection !=null){
			  //System.out.println("SQLQueryToJSON : Connection to DB is OK !");
			  
			  Statement stmt1 = null;
			  Statement stmt2 = null;
			  Statement stmt3 = null;
			  ResultSet rs_del = null;
			  ResultSet rs_dload = null;
			  ResultSet rs_ok = null;
			  JSONArray deviceStockedPicturesJ = new JSONArray(deviceStockedPictures);
			  
			  for (int i = 0; i < deviceStockedPicturesJ.length(); i++) {
				  
				  JSONObject d = deviceStockedPicturesJ.getJSONObject(i);
				  
				  //System.out.println(d.toString());
				  int theme_id = d.getInt(TAG_ID);
				  //String theme_name = d.getString(TAG_NOMTHEME);
				  
				  //ThemeHashMap.put(theme_id, "'"+theme_name+"'");
				  
				  if (i != deviceStockedPicturesJ.length()-1) { 
				  	id_values_concat+=theme_id+", ";
				  } else {
					id_values_concat+=theme_id;
				  }
			  }
			  
			  //Pour savoir quelles sont les thèmes qui ne sont plus d'actualité
			  //(c à d qu'ils ont été supprimés par l'admin)
		    	String Query_isDeleted = "SELECT id_theme FROM Theme WHERE available = 0 AND "
		    			+ "id_theme IN ( "+id_values_concat+" )";
		      
		      //Pour savoir quels sont les thèmes pour lesquels il faut à nouveau 
		      //télécharger l'image (c à dire qu'on a pas déjà l'id du thème sur le tel
		    	String Query_toDownload = "SELECT id_theme, nom_theme FROM Theme WHERE available = 1 AND "
		    			+ "id_theme NOT IN ( "+id_values_concat+" )";
		    	
		    	//Pour savoir quelles sont les images qu'on possède déjà et dont les thèmes sont toujours
		    	//d'actualité (on récupère le nom de thème au cas ou il aurait changé).
		    	String Query_imgOkay = "SELECT id_theme, nom_theme FROM Theme WHERE available = 1 AND "
		    			+ "id_theme IN ( "+id_values_concat+" )";
		    
			  try {
			     stmt1 = connection.createStatement();
			     stmt2 = connection.createStatement();
			     stmt3 = connection.createStatement();
			     rs_del = stmt1.executeQuery(Query_isDeleted);
			     rs_dload = stmt2.executeQuery(Query_toDownload);
			     rs_ok = stmt3.executeQuery(Query_imgOkay);
			     
			     JSONArray JSONArray_del = MinorServices.resultSetToJSON(rs_del);
			     JSONArray JSONArray_ok = MinorServices.resultSetToJSON(rs_ok);
			    
			     //JSONObject JSONResponse = new JSONObject();
			     System.out.println(JSONArray_del.toString());
			     System.out.println(JSONArray_ok.toString());
			     
			     rs_dload.first();
			     picturesToDownload.add(rs_dload.getString("nom_theme")+".jpeg");
			     while(rs_dload.next()) {
			    	 picturesToDownload.add(rs_dload.getString("nom_theme")+".jpeg");
			     }
			     
			     HashMap <String, byte[]> picHashMap = LoadPicture(picturesToDownload);
			     
			     JSONArray JSONArray_dload = MinorServices.resultSetAndHashMapToJSON(rs_dload, picHashMap);
			     System.out.println(JSONArray_dload.toString());
			     
			     JSONObject finalResponse = new JSONObject();
			     finalResponse.put("imgToDownload", JSONArray_dload);
			     finalResponse.put("imgToDelete", JSONArray_del);
			     finalResponse.put("imgOkay",JSONArray_ok);  
			     return finalResponse;	     
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

			      if (rs_del != null) {
			          try {
			              rs_del.close();
			          } catch (SQLException sqlEx) { } // ignore

			          rs_del = null;
			      }
			      
			      if (rs_dload != null) {
			          try {
			              rs_dload.close();
			          } catch (SQLException sqlEx) { } // ignore

			          rs_dload = null;
			      }
			      
			      if (rs_ok != null) {
			          try {
			              rs_ok.close();
			          } catch (SQLException sqlEx) { } // ignore

			          rs_ok = null;
			      }

			      if (stmt1 != null) {
			          try {
			              stmt1.close();
			          } catch (SQLException sqlEx) { } // ignore

			          stmt1 = null;
			      }
			      
			      if (stmt2 != null) {
			          try {
			              stmt2.close();
			          } catch (SQLException sqlEx) { } // ignore

			          stmt2 = null;
			      }
			      
			      if (stmt3 != null) {
			          try {
			              stmt3.close();
			          } catch (SQLException sqlEx) { } // ignore

			          stmt3 = null;
			      }
			  }
		  } else {
			  System.out.println("SelectPlacesByTheme : Given DB Connection is null !");
			  return null;
		  }
	}
}

