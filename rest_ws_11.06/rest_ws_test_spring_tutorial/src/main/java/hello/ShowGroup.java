package hello;

public class ShowGroup {

    private final String members;
    private final String wall;
    //private final int userIsMember;

    public ShowGroup(String members, String wall/*,int userIsMember*/) {
        this.members = members;
        this.wall = wall;
        //this.userIsMember = userIsMember;       
    }

    public String getMembers() {
        return members;
    }
    
    public String getWall() {
    	return wall;
    }
    /*
    public int getUserIsMember() {
    	return userIsMember;
    }
    */
}
