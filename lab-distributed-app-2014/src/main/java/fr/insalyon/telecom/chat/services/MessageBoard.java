package fr.insalyon.telecom.chat.services;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import fr.insalyon.telecom.chat.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageBoard {

  private final HazelcastInstance instance;

  public MessageBoard() {
    Config config = new Config();
    instance = Hazelcast.newHazelcastInstance(config);
  }

  public List<Post> getPosts() {
    return getList();
  }

  private IList<Post> getList() {
    return instance.getList("message-board");
  }

  public void post(Post post) {
    getList().add(0, post);
  }
}
