package fr.insalyon.telecom.chat.controllers;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import com.hazelcast.core.IList;
import java.util.List;

import fr.insalyon.telecom.chat.model.Post;
import fr.insalyon.telecom.chat.services.AuthenticationService;
import fr.insalyon.telecom.chat.services.GistService;
import fr.insalyon.telecom.chat.services.MessageBoard;
import fr.insalyon.telecom.chat.services.MessagesForApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
public class RestApiController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping(value = "/api/messages", method = RequestMethod.POST)
    public MessagesForApi messagesForApi() {
		List<Post> listOfPosts = messageBoard.getPosts();
		int id = (int)counter.incrementAndGet();
        return new MessagesForApi(id, listOfPosts.get(id).getAuthor(), listOfPosts.get(id).getMessage() );
    }
    
     @Autowired
	private MessageBoard messageBoard;
	
}
