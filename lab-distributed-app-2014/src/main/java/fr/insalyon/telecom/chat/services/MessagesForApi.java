package fr.insalyon.telecom.chat.services;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import fr.insalyon.telecom.chat.model.Post;
import org.springframework.stereotype.Component;
import java.util.List;

public class MessagesForApi {

    private final String author;
    private final String message;
	private final long id;
	
    public MessagesForApi(long id, String author, String message) {
        this.id = id;
        this.author = author;
        this.message = message;
    }

    public long getId() {
        return id;
    }
    
}
