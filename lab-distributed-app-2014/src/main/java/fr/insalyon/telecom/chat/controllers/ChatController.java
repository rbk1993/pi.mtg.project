package fr.insalyon.telecom.chat.controllers;

import java.io.File;
import com.hazelcast.core.IList;
import java.util.List;

import fr.insalyon.telecom.chat.model.Post;
import fr.insalyon.telecom.chat.services.AuthenticationService;
import fr.insalyon.telecom.chat.services.GistService;
import fr.insalyon.telecom.chat.services.MessageBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ChatController {

  @RequestMapping("/")
  public ModelAndView index(HttpSession session) {  
    if (session.getAttribute("login") == null) {
      return new ModelAndView("login");
    }
    return new ModelAndView("main")
      .addObject("posts", messageBoard.getPosts())
      .addObject("user", session.getAttribute("login"));  
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String login(
      HttpSession session,
      @RequestParam("login") String login,
      @RequestParam("password") String password) {
		  
    // (...) 
    boolean success = authenticationService.authenticate(login, password);

    
    if(success == true) {
		session.setAttribute("login", login);
	}
	
	return "redirect:/";
	 
	// (...)
  }
  
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
	/* ... */
	
	session.setAttribute("login",null);
	return "redirect:/";
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String post(HttpSession session, @RequestParam("message") String message) {
    /* ... */
    messageBoard.post(new Post(session.getAttribute("login").toString(),message));
    return "redirect:/";
	}
	
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public String publish(HttpSession session) {
	
	String newLine = System.getProperty("line.separator");
	
	String content = "Log content :"+newLine;

	List<Post> listOfPosts = messageBoard.getPosts();
	
	for(int i=0;i<listOfPosts.size();i++) {
		content += listOfPosts.get(i).getAuthor() + " : " + listOfPosts.get(i).getMessage() + newLine;
	}
	
	String logMessage = "published the log to ";
	String url = gistService.publish(content);
	logMessage+=url;
	messageBoard.post(new Post(session.getAttribute("login").toString(),logMessage));
	return "redirect:/";
	
	}
  
  @Autowired
  private AuthenticationService authenticationService;
  
  @Autowired
  private MessageBoard messageBoard;
  
  @Autowired
  private GistService gistService;
}
