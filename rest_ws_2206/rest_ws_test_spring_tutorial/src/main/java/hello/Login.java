package hello;

public class Login {
	
	  private final int success;
	  private final String whoAmI;

	  public Login(int success, String whoAmI) {
	    this.success = success;
	    this.whoAmI = whoAmI;
	  }

	  public int getSuccess() {
	    return success;
	  }
	  
	  public String getWhoAmI() {
		 return whoAmI;
	  }
	  
}
