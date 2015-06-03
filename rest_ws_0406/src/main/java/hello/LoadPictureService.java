package hello;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class LoadPictureService {
	
	public static void main (String[] Args) throws IOException, JSONException {
		
		LoadPicture();
		
	}
	public static String LoadPicture() throws IOException, JSONException{
		
		JSONArray jsonArray = new JSONArray();
		
		final File folder = new File("/home/invite/IProject/Images/");
		
		List<File> myFiles = listFilesForFolder(folder);
		
		List<DataInputStream> listIS = new ArrayList<DataInputStream>();
		
		JSONObject obj = new JSONObject();	
		
		for(int i=0;i<myFiles.size();i++){
			
			
			listIS.add(( new DataInputStream(new FileInputStream(myFiles.get(i))) ));
			
			byte[] bytes = new byte[(int) myFiles.get(i).length()];
			
			for(int j = 0;listIS.get(i).available() != 0;j++){
				
				bytes[j] = listIS.get(i).readByte();
				
			}
			
			obj.put(myFiles.get(i).getName(), bytes);
		
		//System.out.println(Arrays.toString(bytes));
		
		//FileOutputStream fileinput = new FileOutputStream(new File("/home/invite/IProject/Images/newpic"+i));
		
		//DataOutputStream stream = new DataOutputStream(fileinput);
		
		//stream.write(bytes);
		}
		
		jsonArray.put(obj);
		
		//System.out.println(jsonArray.toString());
		
		return jsonArray.toString();
	}
	
	public static List<File> listFilesForFolder(final File folder) {
		
		String folderPath = folder.getAbsolutePath();
		
		List<File> listOfFiles = new ArrayList<File>();
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	String filePath = folderPath+"/"+fileEntry.getName();
	            System.out.println(filePath);
	            listOfFiles.add(new File(filePath));
	        }
	    }
	    
	    return listOfFiles;
	}


}
