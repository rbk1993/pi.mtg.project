package fr.insalyon.telecom.chat.services;

public class GistRequest {

  private Files files;

  public GistRequest(Files files) {
    this.files = files;
  }

  public Object getFiles() {
    return files;
  }
  
}


