package hello;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
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
    	//if (!session.isNew()) {
        	
        	String Query = "SELECT poi.id, poi.typedetail, poi.longitude, poi.latitude, poi.name, poi.opening, poi.type "
        			+ "FROM poi WHERE poi.type = '"+theme+"' ORDER BY poi.name";
        	
    		return new PlacesSelection(counter.incrementAndGet(), Services.SQLQueryToJSON(Query));
    	//} else {
    		//return new PlacesSelection(counter.incrementAndGet(), "connecte toi al hmar");
    	//}

	 }
    
    //TODO : OK
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Login login(
    	      HttpSession session,
    	      @RequestParam("login") String login,
    	      @RequestParam("password") String password) throws Exception {
    	
    		int successfulAuth = Services.authenticateUser(login, password);
    	
    	    if(successfulAuth != 0) {
    	    	//Ouvrir une session pour "login"
    			session.setAttribute("login", login);
    			session.setAttribute("user_id", successfulAuth);
    		}
    	    
    	return new Login(Services.authenticateUser(login, password));
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
    		@RequestParam("duree_parcours_heures") Integer dureeHr,
    		@RequestParam("duree_parcours_minutes") Integer dureeMn,
    		@RequestParam("date") String date,
    		@RequestParam("horaire") String horaire,
    		@RequestParam("nombre_membres_max") Integer nbMembresMax,
    		@RequestParam("point_rencontre") Integer ptRencontre,
    		@RequestParam("nom_groupe") String name) throws Exception {
    	
    	Integer userID = (Integer) session.getAttribute("user_id");
    	 
    	int successCreation = 0;
    	int successRegistration = 0;
    	
    	String queryCreate = "INSERT INTO groupe "
    			+"(id_administrateur, duree_parcours_heures, "
    			+ "duree_parcours_minutes, date, horaire, nombre_membres_max, nombre_membres_inscrits, url, "
    			+ "id_lieu_rencontre, nom_groupe) "
    			+ "VALUES ('"+userID+"','"+dureeHr+"','"+dureeMn+"','"
    					+date+"','"+horaire+"','"+nbMembresMax+"', "+1+",'"+url+"','"+ptRencontre+"','"+name+"')";
    	
    	String queryRegister = "INSERT INTO membres_groupes VALUES ('"+userID+"','"+successCreation+"')";
    	successCreation = Services.sendSQLData(queryCreate,true,true);
    	
    	if (successCreation != 0) {
    	successRegistration = Services.sendSQLData(queryRegister,true, false);
    	}
    	
    	return "[{\"creation_success\":\""+successCreation+"\","
    		  +"  \"registration_success\":\""+successRegistration+"\"}]";
    
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
    		
    		@RequestParam(value = "duree_parcours_heures_min", defaultValue = "0") String dureeHrMin,
    		@RequestParam(value = "duree_parcours_heures_max", defaultValue = "24") String dureeHrMax,
    	    @RequestParam(value = "date_deb", defaultValue = "1900-01-01") String date_deb,
    		@RequestParam(value = "date_fin", defaultValue = "2100-01-01") String date_fin,
    	    @RequestParam(value = "horaire_min", defaultValue = "00:00:00") String horaireMin,
    	    @RequestParam(value = "horaire_max", defaultValue = "23:59:59") String horaireMax,
    	    @RequestParam(value = "nombre_membres_min", defaultValue = "0") String nbMembresMin,
    		@RequestParam(value = "nombre_membres_max", defaultValue = "999") String nbMembresMax,
    		@RequestParam(value = "show_complete_groups", defaultValue = "0") String show_compl
    		)throws Exception {
    	
    	Integer userID = (Integer) session.getAttribute("user_id");
    	System.out.println("User "+userID+" attempting to search for groups !");
    	
    	/* Search for group infos */
    	String Query; //My groups
    	String Query2; //Other groups
    	
    	/* Groups for which user is a member */
    	if (show_compl == "0") {

    	Query = "SELECT DISTINCT * FROM groupe, membres_groupes "
    			+ "WHERE ( duree_parcours_heures BETWEEN "+Integer.parseInt(dureeHrMin)+" AND "+Integer.parseInt(dureeHrMax)+" ) "
    			+ "AND ( date BETWEEN "+"'"+date_deb+"'"+" AND "+"'"+date_fin+"'"+" ) "
    			+ "AND ( nombre_membres_max BETWEEN "+Integer.parseInt(nbMembresMin)+" AND "+Integer.parseInt(nbMembresMax)+" ) "
    			+ "AND ( horaire BETWEEN "+"'"+horaireMin+"'"+" AND "+"'"+horaireMax+"'"+" ) "
    			+ "AND groupe.group_id = membres_groupes.group_id AND membres_groupes.user_id = "+userID+" "
    			+ "AND id_administrateur <> "+userID+" "
    			+ "HAVING nombre_membres_inscrits < nombre_membres_max "
    			+ "ORDER BY date, horaire";
    	} else {
    		
        	Query = "SELECT DISTINCT * FROM groupe, membres_groupes "
        			+ "WHERE ( duree_parcours_heures BETWEEN "+Integer.parseInt(dureeHrMin)+" AND "+Integer.parseInt(dureeHrMax)+" ) "
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
    			+ "WHERE ( duree_parcours_heures BETWEEN "+Integer.parseInt(dureeHrMin)+" AND "+Integer.parseInt(dureeHrMax)+" ) "
    			+ "AND ( date BETWEEN "+"'"+date_deb+"'"+" AND "+"'"+date_fin+"'"+" ) "
    			+ "AND ( nombre_membres_max BETWEEN "+Integer.parseInt(nbMembresMin)+" AND "+Integer.parseInt(nbMembresMax)+" ) "
    			+ "AND ( horaire BETWEEN "+"'"+horaireMin+"'"+" AND "+"'"+horaireMax+"'"+" ) "
    			+ "HAVING nombre_membres_inscrits < nombre_membres_max "
    			+ "ORDER BY date, horaire";
    	} else {
    		
        	Query2 = "SELECT * FROM groupe "
        			+ "WHERE ( duree_parcours_heures BETWEEN "+Integer.parseInt(dureeHrMin)+" AND "+Integer.parseInt(dureeHrMax)+" ) "
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
    
	/* Search for group members */
	
	String Query = "SELECT DISTINCT utilisateur.Nom, utilisateur.Prenom, utilisateur.user_id "
			+ "FROM utilisateur, membres_groupes WHERE utilisateur.user_id = membres_groupes.user_id "
			+ "AND membres_groupes.group_id = '"+group_id+"'";
	
	String groupMembers = Services.SQLQueryToJSON(Query);
	
	/* Search for group wall */
	
	String Query2 = "SELECT * FROM group_wall WHERE group_id = '"+group_id+"'";
	
	String groupWall = Services.SQLQueryToJSON(Query2);
	
	return new ShowGroup(groupMembers,groupWall);
    }
    
    //TODO : OK
    @RequestMapping(value="/writegroupwall", method = RequestMethod.GET)
    public String writeGroupWall(
    		HttpSession session,  
    		@RequestParam("group_id") String group_id,
    		@RequestParam("message") String message) throws Exception {
    	
    	Integer userID = (Integer) session.getAttribute("user_id");
    	
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
    		@RequestParam("group_id") String group_id) throws Exception {
    	
    	Integer userID = (Integer) session.getAttribute("user_id");
    	
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
	    	
	    	return "{\"inscription_success\":\""+successInsert*successUpdate+"\"}";
    	} else {
    		return "{\"inscription_success\":\""+0+"\"}";
    	}
    }
    
    //TODO : OK
    @RequestMapping(value = "/unsubscribegroup", method = RequestMethod.GET)
    public String desinscriptionGroup(
    		HttpSession session,
    		@RequestParam("group_id") String group_id) throws Exception {
    	
        Integer userID = (Integer) session.getAttribute("user_id");
      	
        if(Services.checkIfExists(group_id, "group_id", "groupe")) {
        	 
			String Query = "DELETE FROM membres_groupes WHERE group_id = '"+group_id+"' AND user_id = '"+userID+"'";
			//TODO : membres_groupes_a_approuver
			
	      	String Query2 = 
	      	    	"UPDATE groupe SET nombre_membres_inscrits = nombre_membres_inscrits - 1 "
	      	    	+ "WHERE group_id = '"+group_id+"'";
	          
	    	int successDelete = Services.sendSQLData(Query, true, false);
	    	int successUpdate = Services.sendSQLData(Query2, true, false);
        	
	    	return "{\"desinscription_success\":\""+successDelete*successUpdate+"\"}";
          } else return "{\"desinscription_success\":\""+0+"\"}";
    }
 
    //TODO : OK
    //TODO ! : Vérifier que c'est bien l'administrateur qui formule la requête de suppression.
    @RequestMapping(value = "/deletegroup", method = RequestMethod.GET)
    public String annulationgroup(
    		HttpSession session,
    		@RequestParam("group_id") String group_id) throws Exception {
    	
    	int successSuppressionMembres = 0;
    	int successAnnulation = 0;
        Integer userID = (Integer) session.getAttribute("user_id");
        //Integer userID=10;
        
        if(Services.checkIfExists(group_id, "group_id", "groupe")) {
        	  
	        String Query = "DELETE FROM groupe WHERE group_id = '"+group_id+"' AND id_administrateur = '"+userID+"' ";
	        
	        String Query2 = "DELETE FROM membres_groupes WHERE group_id = '"+group_id+"'";
	          
	    	successAnnulation = Services.sendSQLData(Query,true,false);
	    	
	    	if (successAnnulation == 1){
	    		successSuppressionMembres = Services.sendSQLData(Query2, true,false);
	    	}
	    	return "{\"delete_group_success\":\""+successAnnulation*successSuppressionMembres+"\"}";
          } else return "{\"delete_group_success\":\""+0+"\"}";
    }
    
    //TODO : OK
    @RequestMapping(value="/writeuserwall", method = RequestMethod.GET)
    public String writeUserWall(
    		HttpSession session,  
    		@RequestParam("user_id") String user_id,
    		@RequestParam("message") String message,
    		@RequestParam("note") int note) throws Exception {
    	
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
    public SearchMyGroups searchGroup(HttpSession session) throws Exception {
    	
    	Integer userID = (Integer) session.getAttribute("user_id");
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
    	String infos_allgroups = Services.SQLQueryToJSON(Query2);
    	
    	return new SearchMyGroups(infos_mygroups,infos_allgroups);
    	}
    
    //TODO : OK
    @RequestMapping(value="/whoami", method = RequestMethod.GET)
    public String whoAmI(HttpSession session) throws Exception {
    	Integer userID = (Integer) session.getAttribute("user_id");
    	
    	String Query = "SELECT nom, prenom, user_id FROM utilisateur where user_id ="+userID;
    	
    	return Services.SQLQueryToJSON(Query);
    }
    
    //TODO : to do
    //@RequestMapping(value="/show ... ?
    
    /* TODO : Administration Interface */
    //@RequestMapping(value="/createtheme", method = RequestMethod.GET)
    
    //@RequestMapping(value="/deletetheme", method = RequestMethod.GET)
    
    //@RequestMapping(value="/createevent", method = RequestMethod.GET)
    
    
    
}
