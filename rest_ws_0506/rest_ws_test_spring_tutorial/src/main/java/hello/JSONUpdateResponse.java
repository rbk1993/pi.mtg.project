package hello;

import org.json.JSONArray;

public class JSONUpdateResponse {
	
	private final JSONArray imgToDelete;
	private final JSONArray imgToDownload;
	private final JSONArray imgOkay;
	
	public JSONUpdateResponse(	JSONArray imgToDelete, 
								JSONArray imgToDownload, 
								JSONArray imgOkay ) {
		
		this.imgToDelete = imgToDelete;
		this.imgToDownload = imgToDownload;
		this.imgOkay = imgOkay;
	}
	
	public JSONArray getImgToDelete(){
		return imgToDelete;
	}
	
	public JSONArray imgToDownload(){
		return imgToDownload;
	}
	
	public JSONArray imgOkay(){
		return imgOkay;
	}

}
