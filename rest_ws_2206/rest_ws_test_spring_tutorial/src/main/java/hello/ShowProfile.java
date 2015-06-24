package hello;

public class ShowProfile {

    private final String info;
    private final String wall;
    private final String avgMark;

    public ShowProfile(String info, String wall, String avgMark) {
        this.info = info;
        this.wall = wall;
        this.avgMark = avgMark;      
    }

    public String getInfo() {
        return info;
    }
    
    public String getWall() {
    	return wall;
    }
    
    public String getAvgMark() {
    	return avgMark;
    }
	
}
