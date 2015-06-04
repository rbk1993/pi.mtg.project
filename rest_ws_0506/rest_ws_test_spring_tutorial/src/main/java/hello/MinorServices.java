package hello;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class MinorServices {

	public static int getRowsCount(ResultSet rs) throws SQLException{
		
		int incrCounter=0;
		while (rs.next()) {
			incrCounter++;
		}
		return incrCounter;
	}
	
	/**
     * Convert a result set into a JSON Array
     * @param resultSet
     * @return a JSONArray
     * @throws Exception
     */
    public static JSONArray resultSetToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        
        if(resultSet.first()) {
        	
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }
    
    public static JSONArray resultSetAndHashMapToJSON(ResultSet resultSet, HashMap<String, byte[]> hashMap)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        
        if(resultSet.first()) {
        	
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            System.out.println("DEBUG obj.get(nomtheme) ="+obj.get("nom_theme"));
            byte[] byteToPut = hashMap.get(obj.get("nom_theme")+".jpeg");
            obj.put("image", byteToPut.toString());
            obj.put("houhou", "kikou");
            jsonArray.put(obj);
        }
        
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            System.out.println("DEBUG obj.get(nomtheme) ="+obj.get("nom_theme"));
            byte[] byteToPut = hashMap.get(obj.get("nom_theme")+".jpeg");
            obj.put("image", byteToPut.toString());
            obj.put("houhou", "kikou");
            jsonArray.put(obj);   
        }
        return jsonArray;
    }

}
