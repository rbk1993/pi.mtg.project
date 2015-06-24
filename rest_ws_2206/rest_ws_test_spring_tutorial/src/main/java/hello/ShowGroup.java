package hello;

public class ShowGroup {

    private final String members;
    private final String wall;
    private final String places;
    //private final int userIsMember;

    public ShowGroup(String members, String wall, String places/*,int userIsMember*/) {
        this.members = members;
        this.wall = wall;
        this.places = places;
        //this.userIsMember = userIsMember;       
    }

    public String getMembers() {
        return members;
    }
    
    public String getWall() {
    	return wall;
    }
    
    public String getPlaces() {
    	return places;
    }
    /*
    public int getUserIsMember() {
    	return userIsMember;
    }
    */
}
