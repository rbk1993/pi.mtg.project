package hello;

public class SearchMyGroups {

    private final String info_registeredgroups;
    private final String info_managedgroups;

    public SearchMyGroups(String info_registeredgroups, String info_managedgroups) {
        this.info_registeredgroups = info_registeredgroups;
        this.info_managedgroups = info_managedgroups;
    }

    public String getInfo_registeredgroups() {
        return info_registeredgroups;
    }
    
    public String getInfo_managedgroups() {
    	return info_managedgroups;
    }
	
}
