package hello;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();
    //private final AtomicLong badRequestCounter = new AtomicLong();
    
    //TODO : OK
    @RequestMapping("/placesselection")
    public PlacesSelection placesSelection(HttpSession session, @RequestParam(value="theme", defaultValue="...") String theme) throws Exception {
    	
    	//TODO : A faire normalement pour tous les services : Check s'il y a une session ou pas 
    	
    		System.out.println("Places selection !");
        	
        	String Query = "SELECT poi.id, poi.typedetail, poi.longitude, poi.latitude, poi.name, poi.opening, poi.type "
        			+ "FROM poi WHERE ( poi.type = '"+theme+"' ) OR ( poi.typedetail = '"+theme+"' )  ORDER BY poi.name";
        	
        	
        	//Statistics
        	String Query2 = 
        	    	"UPDATE statistics_themes SET compter = compter + 1 "
        	    	+ "WHERE theme = '"+theme+"'";      	          	
        
        	    	int successUpdate = Services.sendSQLData(Query2, true, false);	
        	    	
        	
    		return new PlacesSelection(counter.incrementAndGet(), Services.SQLQueryToJSON(Query));
 
	 }
    
    //TODO : OK
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Login login(
    	      HttpSession session,
    	      @RequestParam("login") String login,
    	      @RequestParam("password") String password) throws Exception {
    	
    	System.out.println("A user is logging in !");
    	
    		int successfulAuth = Services.authenticateUser(login, password);
    	
    	    if(successfulAuth != 0) {
    	    	//Ouvrir une session pour "login"
    			session.setAttribute("login", login);
    			session.setAttribute("user_id", successfulAuth);
    			System.out.println("Session opened with login ="+session.getAttribute("login")+" "
    					+"and user_id="+session.getAttribute("user_id"));
    		}
    	    
    	    String identity = whoAmI(session);
    	    
    	return new Login(successfulAuth,identity);
    }
    
    //TODO : OK
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String createUser(
    		HttpSession session,
    		@RequestParam("prenom") String prenom,
    		@RequestParam("nom") String nom,
    		@RequestParam("date_naissance") String date_naissance,
    		@RequestParam("login") String login,
    		@RequestParam("mail") String mail,
    		@RequestParam("password") String password,
    		@RequestParam("telephone") String telephone) throws Exception {
    	
    	System.out.println("Creation of a new user in progress.... !");
    	
    	    String Query = "INSERT INTO utilisateur "
        			+"(prenom, nom, "
        			+ "date_naissance, login, mail, password, "
        			+ "telephone) "
        			+ "VALUES ( '"+prenom+"', '"+nom+"', '"+date_naissance+"', '" 
        					+login+"', '"+mail+"', '"+password+"' ,"+telephone+ ")";
    	    
    	int successCreation = Services.sendSQLData(Query, true, true);
    
    	return "{\"inscription_success\":\""+successCreation+"\"}";
    
    }   
    
    //TODO : OK
    @RequestMapping(value = "/creategroup", method = RequestMethod.GET)
    public String createGroup(
    		HttpSession session,
    		@RequestParam("url") String url,
    		@RequestParam("duree_parcours_heures") String dureeHr,
    		@RequestParam("date") String date,
    		@RequestParam("horaire") String horaire,
    		@RequestParam("nombre_membres_max") String nbMembresMax,
    		@RequestParam("nom_groupe") String name,
    		@RequestParam("user_id") Integer userID,
    		@RequestParam("jsonobject") String id_selectedPlaces) throws Exception {
    	
    	System.out.println("User "+userID+" attempting to create a group !");

    	int successCreation = 0;
    	int successRegistration = 0;
    	int successAddLieux = 0;
    	String queryCreate = "INSERT INTO groupe "
    			+"(id_administrateur, duree_parcours_heures, "
    			+ "date, horaire, nombre_membres_max, nombre_membres_inscrits, url, "
    			+ "name) "
    			+ "VALUES ('"+userID+"','"+dureeHr+"','"+date+"','"+horaire+"','"+nbMembresMax+"', "+1+",'"+url+"','"+name+"')";
    	
    	successCreation = Services.sendSQLData(queryCreate,true,true);
    	
    	if (successCreation != 0) {	
    		
        	String queryRegister = "INSERT INTO membres_groupes VALUES ('"+userID+"', '"+successCreation+"', '0')";
        	
    		successRegistration = Services.sendSQLData(queryRegister,true, false);
    		
    		if(successRegistration != 0) {
    			
    			//Ajout des lieux au groupe
    			
    	    	JSONArray jsonPlaces = new JSONArray(id_selectedPlaces);
    	    	String[] queryAddPlacesToGroup = new String[jsonPlaces.length()];
    	    	for(int i = 0;i<jsonPlaces.length();i++) {
    	    		
    	    		JSONObject d = jsonPlaces.getJSONObject(i);
    	    		String id_lieu = d.getString("id_lieu");
    	    		System.out.println("Success creation before adding places : "+successCreation);
    	    		queryAddPlacesToGroup[i] = "INSERT INTO lieux_groupes VALUES ('"+id_lieu+"', '"+successCreation+"')";
    	    	}
    			
    			for(int i=0;i<queryAddPlacesToGroup.length;i++)
    				Services.sendSQLData(queryAddPlacesToGroup[i], true, false);
    			
    			//Ecriture d'un premier message de bienvenue dans le mur
    			
    			String writeNew = writeGroupWall(session,String.valueOf(userID),String.valueOf(successCreation),"Welcome !");
    			
    	    	//count stats heures
    			  String Query_stat1 = 
    			    	    	"UPDATE statistics_heures SET compter = compter + 1 "
    			        	+ "WHERE heure_deb <='"+horaire+"' AND heure_fin > '"+horaire+"'";      	          	
    			    
    			   int successUpdate = Services.sendSQLData(Query_stat1, true, false);
    			   
       	    	//count stats duree
     			  String Query_stat_duree = 
     			    	    	"UPDATE statistics_duree SET compter = compter + 1 "
     			        	+ "WHERE duree_deb <='"+dureeHr+"' AND duree_fin > '"+dureeHr+"'";      	          	
     			    
     			   int successUpdate2 = Services.sendSQLData(Query_stat_duree, true, false);
    			   
    			//count stats lieux
    			   
    			   String Query_stat2;
    			   
    	    	for(int i = 0;i<jsonPlaces.length();i++) {
    	    		
    	    		JSONObject d = jsonPlaces.getJSONObject(i);
    	    		String id_lieu = d.getString("id_lieu");
    			    Query_stat2 =
    					   "UPDATE statistics_lieux SET compter = compter + 1 "
    			    		+ " WHERE id_lieu = '"+id_lieu+"'";
    			    Services.sendSQLData(Query_stat2, true, false);
    			}
    		
    			
    		}
    	}
    	
    	return "{\"creation_success\":\""+successCreation+"\","
    		  +"  \"registration_success\":\""+successRegistration+"\"}";
    
    }
   
    //TODO : OK
    @RequestMapping(value = "/mainpagethemes", method = RequestMethod.GET)
    public String loadMainPage(HttpSession session,
    		@RequestParam("jsonarray") String deviceStockedPictures) throws Exception {

    	return Services.CheckForUpdates(deviceStockedPictures).toString();
    } 
    
    //TODO : OK
    @RequestMapping(value="/searchgroup", method = RequestMethod.GET)
    public SearchGroup searchGroup(
    		HttpSession session,    	    
    		
    		@RequestParam(value = "duree_parcours_heures_min", defaultValue = "00:00:00") String dureeHrMin,
    		@RequestParam(value = "duree_parcours_heures_max", defaultValue = "23:59:59") String dureeHrMax,
    	    @RequestParam(value = "date_deb", defaultValue = "1900-01-01") String date_deb,
    		@RequestParam(value = "date_fin", defaultValue = "2100-01-01") String date_fin,
    	    @RequestParam(value = "horaire_min", defaultValue = "00:00:00") String horaireMin,
    	    @RequestParam(value = "horaire_max", defaultValue = "23:59:59") String horaireMax,
    	    @RequestParam(value = "nombre_membres_min", defaultValue = "0") String nbMembresMin,
    		@RequestParam(value = "nombre_membres_max", defaultValue = "999") String nbMembresMax,
    		@RequestParam(value = "show_complete_groups", defaultValue = "0") String show_compl,
    		@RequestParam(value = "user_id") int userID
    		)throws Exception {
    	
    	System.out.println("User "+userID+" attempting to search for groups !");
    	
    	/* Search for group infos */
    	String Query; //My groups
    	String Query2; //Other groups
    	
    	/* Groups for which user is a member */
    	if (show_compl == "0") {

    	Query = "SELECT DISTINCT * FROM groupe, membres_groupes "
    			+ "WHERE ( duree_parcours_heures BETWEEN "+"'"+dureeHrMin+"'"+" AND "+"'"+dureeHrMax+"'"+" ) "
    			+ "AND ( date BETWEEN "+"'"+date_deb+"'"+" AND "+"'"+date_fin+"'"+" ) "
    			+ "AND ( nombre_membres_max BETWEEN "+Integer.parseInt(nbMembresMin)+" AND "+Integer.parseInt(nbMembresMax)+" ) "
    			+ "AND ( horaire BETWEEN "+"'"+horaireMin+"'"+" AND "+"'"+horaireMax+"'"+" ) "
    			+ "AND groupe.group_id = membres_groupes.group_id AND membres_groupes.user_id = "+userID+" "
    			+ "AND id_administrateur <> "+userID+" "
    			+ "HAVING nombre_membres_inscrits < nombre_membres_max "
    			+ "ORDER BY date, horaire";
    	} else {
    		
        	Query = "SELECT DISTINCT * FROM groupe, membres_groupes "
        			+ "WHERE ( duree_parcours_heures BETWEEN "+"'"+dureeHrMin+"'"+" AND "+"'"+dureeHrMax+"'"+" ) "
        			+ "AND ( date BETWEEN "+"'"+date_deb+"'"+" AND "+"'"+date_fin+"'"+" ) "
        			+ "AND ( nombre_membres_max BETWEEN "+Integer.parseInt(nbMembresMin)+" AND "+Integer.parseInt(nbMembresMax)+" ) "
        			+ "AND ( horaire BETWEEN "+"'"+horaireMin+"'"+" AND "+"'"+horaireMax+"'"+" ) "
        			+ "AND groupe.group_id = membres_groupes.group_id AND membres_groupes.user_id = "+userID+" "
        			+ "AND id_administrateur <> "+userID+" "
        			+ "ORDER BY date, horaire";
        	
    	}
    	
    	/* All groups */
    	if (show_compl == "0") {

    	Query2 = "SELECT * FROM groupe "
    			+ "WHERE ( duree_parcours_heures BETWEEN "+"'"+dureeHrMin+"'"+" AND "+"'"+dureeHrMax+"'"+" ) "
    			+ "AND ( date BETWEEN "+"'"+date_deb+"'"+" AND "+"'"+date_fin+"'"+" ) "
    			+ "AND ( nombre_membres_max BETWEEN "+Integer.parseInt(nbMembresMin)+" AND "+Integer.parseInt(nbMembresMax)+" ) "
    			+ "AND ( horaire BETWEEN "+"'"+horaireMin+"'"+" AND "+"'"+horaireMax+"'"+" ) "
    			+ "HAVING nombre_membres_inscrits < nombre_membres_max "
    			+ "ORDER BY date, horaire";
    	} else {
    		
        	Query2 = "SELECT * FROM groupe "
        			+ "WHERE ( duree_parcours_heures BETWEEN "+"'"+dureeHrMin+"'"+" AND "+"'"+dureeHrMax+"'"+" ) "
        			+ "AND ( date BETWEEN "+"'"+date_deb+"'"+" AND "+"'"+date_fin+"'"+" ) "
        			+ "AND ( nombre_membres_max BETWEEN "+Integer.parseInt(nbMembresMin)+" AND "+Integer.parseInt(nbMembresMax)+" ) "
        			+ "AND ( horaire BETWEEN "+"'"+horaireMin+"'"+" AND "+"'"+horaireMax+"'"+" ) "
        			+ "ORDER BY date, horaire";
        	
    	}
    	
    	String info_mygroups = Services.SQLQueryToJSON(Query);
    	
    	String info_allgroups = Services.SQLQueryToJSON(Query2);
    	
    	return new SearchGroup(info_mygroups,info_allgroups);
    	}
    
    //TODO : OK
    @RequestMapping(value="/showgroup", method = RequestMethod.GET)
    public ShowGroup showGroup(
    		HttpSession session,  
    		@RequestParam("group_id") String group_id) throws Exception {
    
    	System.out.println("Show group !");
    	
	/* Search for group members */
	
	String Query = "SELECT DISTINCT utilisateur.Nom, utilisateur.Prenom, utilisateur.user_id, utilisateur.date_naissance "
			+ "FROM utilisateur, membres_groupes WHERE utilisateur.user_id = membres_groupes.user_id "
			+ "AND membres_groupes.group_id = '"+group_id+"'";
	
	String groupMembers = Services.SQLQueryToJSON(Query);
	
	/* Search for group wall */
	
	String Query2 = "SELECT utilisateur.login, group_wall.message, group_wall.timestamp FROM group_wall, utilisateur "
			+ "WHERE utilisateur.user_id = group_wall.user_id "
			+ "AND group_id = '"+group_id+"'";
	
	String groupWall = Services.SQLQueryToJSON(Query2);
	
	/* Search for group places */
	
	String Query3 = "SELECT poi.id, poi.type, poi.typedetail, poi.longitude, poi.latitude, poi.name, poi.opening "
			+ "FROM poi, lieux_groupes "
			+ "WHERE lieux_groupes.id_lieu = poi.id AND group_id = '"+group_id+"'";
	
	String groupPlaces = Services.SQLQueryToJSON(Query3);
	
	return new ShowGroup(groupMembers,groupWall, groupPlaces);
    }
    
    //TODO : OK
    @RequestMapping(value="/writegroupwall", method = RequestMethod.GET)
    public String writeGroupWall(
    		HttpSession session,  
    		@RequestParam("user_id") String userID,
    		@RequestParam("group_id") String group_id,
    		@RequestParam("message") String message) throws Exception {
    	
    	System.out.println("Write group wall !");
    	
    	if(Services.checkIfExists(group_id, "group_id", "groupe")) {
    		
	    	String Query = "INSERT INTO group_wall ( group_id, user_id, message ) "
	    			+ "VALUES ( '"+group_id+"', '"+userID+"', '"+message+"')";
	    	
	    	int successWrite = Services.sendSQLData(Query, true, false);
	    	return "{\"write_success\":\""+successWrite+"\"}";
    	} else return "{\"write_success\":\""+0+"\"}";
    }
 
    //TODO : OK
    @RequestMapping(value="/subscribegroup", method = RequestMethod.GET)
    public String inscriptionGroup(
    		HttpSession session,  
    		@RequestParam("group_id") String group_id,
    		@RequestParam("user_id") String userID) throws Exception {
    	
    	System.out.println("Register to group !");
    	
    	//Integer userID = (Integer) session.getAttribute("user_id");
    	
    	//Attention, dans l'appli, n'afficher le bouton s'inscrire que si le groupe
    	//n'est pas complet. Cet aspect n'est pas géré ici pour l'instant...
    	
    	if(Services.checkIfExists(group_id, "group_id", "groupe")) {
    	
	    	String Query = 		
			"INSERT INTO membres_groupes " // TODO: membres_groupes_a_approuver
			+"(user_id, group_id) "
			+ "VALUES ('"+userID+"','"+group_id+"')";
	    	
	    	// Cette fonctionnalité n'a de sens que si on assume que les membres sont automatiquement
	    	// acceptés dans le groupe. Si ajout de fct de confirmation, ne pas mettre
	    	// cette fct ici, mais dans le service de confirmation.
	    	String Query2 = 
	    	"UPDATE groupe SET nombre_membres_inscrits = nombre_membres_inscrits + 1 "
	    	+ "WHERE group_id = '"+group_id+"'";
	          	
	    	int successInsert = Services.sendSQLData(Query,true,false);
	    	int successUpdate = Services.sendSQLData(Query2, true, false);
	    	
	    	//Stats : Count +1 for each place belonging to group
		    String Query_stat2 =
					   "UPDATE statistics_lieux SET compter = compter + 1 "
			    		+ "WHERE id_lieu IN ( "
			    		+ "SELECT id_lieu FROM lieux_groupes WHERE group_id = '"+group_id+"'";
			    Services.sendSQLData(Query_stat2, true, false);
	    	
	    	return "{\"procedure\":\""+"inscription"+"\", \"success\":\""+successInsert*successUpdate+"\"}";
	    	
	    	
    	} else {
    		return "{\"procedure\":\""+"inscription"+"\", \"success\":\""+0+"\"}";
    	}
    }
    
    //TODO : OK
    @RequestMapping(value = "/unsubscribegroup", method = RequestMethod.GET)
    public String desinscriptionGroup(
    		HttpSession session,
    		@RequestParam("group_id") String group_id,
    		@RequestParam("user_id") String userID) throws Exception {
    	
    	System.out.println("Unsubscribe from group !");
    	
        //Integer userID = (Integer) session.getAttribute("user_id");
      	
        if(Services.checkIfExists(group_id, "group_id", "groupe")) {
        	 
			String Query = "DELETE FROM membres_groupes WHERE group_id = '"+group_id+"' AND user_id = '"+userID+"'";
			//TODO : membres_groupes_a_approuver
			
	      	String Query2 = 
	      	    	"UPDATE groupe SET nombre_membres_inscrits = nombre_membres_inscrits - 1 "
	      	    	+ "WHERE group_id = '"+group_id+"'";
	          
	    	int successDelete = Services.sendSQLData(Query, true, false);
	    	int successUpdate = Services.sendSQLData(Query2, true, false);
        	
	    	return "{\"procedure\":\""+"desinscription"+"\", \"success\":\""+successDelete*successUpdate+"\"}";
	    	
          } else return "{\"procedure\":\""+"desinscription"+"\", \"success\":\""+0+"\"}";
    }
 
    //TODO : OK
    //TODO ! : Vérifier que c'est bien l'administrateur qui formule la requête de suppression.
    @RequestMapping(value = "/deletegroup", method = RequestMethod.GET)
    public String annulationgroup(
    		HttpSession session,
    		@RequestParam("group_id") String group_id,
    		@RequestParam("user_id") String userID) throws Exception {
    	
    	System.out.println("Delete group !");
    	
    	int successSuppressionMembres = 0;
    	int successSuppressionWall = 0;
    	int successSuppressionLieux = 0;
    	int successAnnulation = 0;

        if(Services.checkIfExists(group_id, "group_id", "groupe")) {
        	  
	        String Query = "DELETE FROM groupe WHERE group_id = '"+group_id+"' AND id_administrateur = '"+userID+"' ";
	        
	        String Query2 = "DELETE FROM membres_groupes WHERE group_id = '"+group_id+"'";
	        
	        String Query3 = "DELETE FROM lieux_groupes WHERE group_id = '"+group_id+"'";
	        
	        String Query4 = "DELETE FROM group_wall WHERE group_id = '"+group_id+"'";
	          
	    	successAnnulation = Services.sendSQLData(Query,true,false);
	    	
	    	if (successAnnulation == 1){
	    		successSuppressionMembres = Services.sendSQLData(Query2, true,false);
	    		successSuppressionLieux = Services.sendSQLData(Query3, true,false);
	    		successSuppressionWall = Services.sendSQLData(Query4, true,false);
	    	}
	    	return "{\"procedure\":\""
	    	+"annulation"+"\", "
	    	+ "\"success\":\""+successSuppressionMembres*successAnnulation*successSuppressionWall*successSuppressionLieux+"\"}";
          } else return "{\"procedure\":\""+"annulation"+"\", \"success\":\""+0+"\"}";
    }
    
    //TODO : OK
    @RequestMapping(value="/writeuserwall", method = RequestMethod.GET)
    public String writeUserWall(
    		HttpSession session,  
    		@RequestParam("user_id") String user_id,
    		@RequestParam("message") String message,
    		@RequestParam("note") int note) throws Exception {
    	
    	System.out.println("Write user wall !");
    	
    	int user_id_int = Integer.parseInt(user_id);
    	Integer userID = (Integer) session.getAttribute("user_id");
    	
    	if(Services.checkIfExists(user_id, "user_id", "utilisateur") && userID != user_id_int && note>=0 && note<=5) {
    		
	    	String Query = "INSERT INTO user_wall ( user_id, writer_id, message, note ) "
	    			+ "VALUES ( '"+user_id+"', '"+userID+"', '"+message+"', '"+note+"')";
	    	
	    	int successWrite = Services.sendSQLData(Query, true, false);
	    	return "{\"write_success\":\""+successWrite+"\"}";
    	} else return "{\"write_success\":\""+0+"\"}";
    }
    
    //TODO : OK
    @RequestMapping(value="/showprofile", method = RequestMethod.GET)
    public ShowProfile showProfile(
    		HttpSession session,  
    		@RequestParam("user_id") String user_id) throws Exception {
    
    	System.out.println("Show Profile function !");
    	
    if(Services.checkIfExists(user_id, "user_id", "utilisateur")) {
	    Integer userID = (Integer) session.getAttribute("user_id");
	    
		/* Search for user info */
		
		String Query = "SELECT prenom, nom, date_naissance, login, mail, telephone FROM "
				+ "utilisateur WHERE user_id = "+user_id;
		
		String userInfo = Services.SQLQueryToJSON(Query);
		
		/* Search for user wall */
		
		String Query2 = "SELECT * FROM user_wall WHERE user_id = '"+user_id+"'";
		
		String userWall = Services.SQLQueryToJSON(Query2);
		
		/* Search for profile average mark */
	
		String Query3 = "SELECT AVG(note) AS average FROM user_wall WHERE user_id = '"+user_id+"'";
		
		String userAvgMark = Services.SQLQueryToJSON(Query3);
	    
		
		/*return*/
		return new ShowProfile(userInfo, userWall, userAvgMark);
    } else return new ShowProfile("","","");
    }
    
    //TODO : OK
    @RequestMapping(value="/searchmygroups", method = RequestMethod.GET)
    public SearchMyGroups searchMyGroups(HttpSession session,
    		@RequestParam("user_id") Integer userID) throws Exception {

    	System.out.println("User "+userID+" attempting to search for HIS groups !");
    	
    	/* Search for group infos */
    	String Query; //My groups (where I'm registered)
    	String Query2; //My managed groups (where I'm the admin!)
    	
    	//My groups
    	
    	Query = "SELECT DISTINCT * FROM groupe, membres_groupes "
    			+ "WHERE groupe.group_id = membres_groupes.group_id AND membres_groupes.user_id = "+userID+" "
    			+ "AND id_administrateur <> "+userID+" "
    			+ "ORDER BY date, horaire";

    	//My managed groups
    	
        Query2 = "SELECT DISTINCT * FROM groupe "
        		+ "WHERE id_administrateur = "+userID+" "
        		+ "ORDER BY date, horaire";
        	
    	String infos_mygroups = Services.SQLQueryToJSON(Query);
    	String infos_managedgroups = Services.SQLQueryToJSON(Query2);
    	
    	return new SearchMyGroups(infos_mygroups,infos_managedgroups);
    	}
    
    //TODO : OK
    @RequestMapping(value="/whoami", method = RequestMethod.GET)
    public String whoAmI(HttpSession session) throws Exception {
    	Integer userID = (Integer) session.getAttribute("user_id");
    	
    	System.out.println("Who Am I ???");
    	
    	String Query = "SELECT nom, prenom, user_id FROM utilisateur where user_id = "+userID;
    	
    	return Services.SQLQueryToJSON(Query);
    }
    
    //TODO : to do
    @RequestMapping(value="/showUsers", method = RequestMethod.GET)
    public String showUsers(HttpSession session) throws Exception {
    	
    	System.out.println("Show Users !");
    	
    	String Query = "SELECT nom, prenom, user_id FROM utilisateur";
    	
    	return Services.SQLQueryToJSON(Query);
    }
    
    /* TODO : Administration Interface */
    //@RequestMapping(value="/createtheme", method = RequestMethod.GET)
    
    //@RequestMapping(value="/deletetheme", method = RequestMethod.GET)
    
    //@RequestMapping(value="/createevent", method = RequestMethod.GET)
    
    
    
}
