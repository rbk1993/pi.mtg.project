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

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final AtomicLong badRequestCounter = new AtomicLong();
    
    @RequestMapping("/placesselection")
    public PlacesSelection placesSelection(HttpSession session, @RequestParam(value="theme", defaultValue="...") String theme) throws Exception {
    	
    	String Query = "SELECT Lieu.description, Lieu.longitude, Lieu.latitude "
    			+ "FROM Lieu, Theme, Lieux_Themes "
    			+ "WHERE Lieu.id_lieu = Lieux_Themes.id_lieu "
    			+ "AND Lieux_Themes.id_theme = Theme.id_theme "
    			+ "AND Theme.nom_theme= '"+theme+"'";
    	
		return new PlacesSelection(counter.incrementAndGet(), Services.SQLQueryToJSON(Query));
	 }
    
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
    
    
    @RequestMapping(value = "/creategroup", method = RequestMethod.GET)
    public String createGroup(
    		HttpSession session,
    		@RequestParam("duree_parcours_heures") Integer dureeHr,
    		@RequestParam("duree_parcours_minutes") Integer dureeMn,
    		@RequestParam("date") String date,
    		@RequestParam("horaire") String horaire,
    		@RequestParam("nombre_membres_max") Integer nbMembresMax,
    		@RequestParam("point_rencontre") Integer ptRencontre) throws Exception {
    	
    	Integer userID = (Integer) session.getAttribute("user_id");
    	
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
    	
    	return "[{\"creation_success\":\""+successCreation+"\","
    		  +"  \"registration_success\":\""+successRegistration+"\"}]";
    
    }
    
    /* TODO : Services below */
    
    /* Traveler interface */
    
    //@RequestMapping(value="/subscribegeneral", method = RequestMethod.GET)
    
    //@RequestMapping(value="/mainpage_themes", method = RequestMethod.GET)
    
    @RequestMapping(value = "/mainpagethemes", method = RequestMethod.POST)
    public JSONUpdateResponse loadMainPage(HttpSession session,
    		@RequestParam("jsonarray") String deviceStockedPictures) throws Exception {

    	return Services.CheckForUpdates(deviceStockedPictures);
    }
    
    
    //@RequestMapping(value="/watchprofile", method = RequestMethod.GET)
    
    //@RequestMapping(value="/searchgroups", method = RequestMethod.GET)
    
    //@RequestMapping(value="/subscribetogroup", method = RequestMethod.GET)
  
    //@RequestMapping(value="/deletegroup", method = RequestMethod.GET)
    
    //@RequestMapping(value="/createtheme", method = RequestMethod.GET)
    
    /* Administration Interface */
    
    //@RequestMapping(value="/deletetheme", method = RequestMethod.GET)
    
    //@RequestMapping(value="/createevent", method = RequestMethod.GET)
    
    
    
}
