package hello;

public class SearchGroup {

    private final String info_mygroups;
    private final String info_allgroups;

    public SearchGroup(String info_mygroups, String info_allgroups) {
        this.info_mygroups = info_mygroups;
        this.info_allgroups = info_allgroups;
    }

    public String getInfo_mygroups() {
        return info_mygroups;
    }
    
    public String getInfo_allgroups() {
    	return info_allgroups;
    }
    
}
