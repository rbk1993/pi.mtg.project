package fr.insalyon.telecom.chat.services;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GistService {

  public String publish(String content) {
    RestTemplate restTemplate = new RestTemplate();
    
    Files files = new Files(new Filename(content));
    
    GistRequest request = new GistRequest(files);
    
    GistResponse response = restTemplate.postForObject(
        "https://api.github.com/gists",
        request,
        /* ... */ GistResponse.class);
    return /* ... */ response.getUrl(); 
  }
}
