package finalOne;

/**
 * It's a class of personal recipient objects.
 * It extends Recipient class.
 * It has a sub class which is OfficeFriend.
 * 
 * @author Kajanan Selvanesan
 * @see Recipient
 * @see OfficeFriend
 */

public class OfficialRecipient extends Recipient{
	
	/** Designation of the official recipient object*/
	private String designation;
	
	/**
	 * Constructs a official recipient object with name, mail address and designation.
	 * Passes the values of name and mail address to its super class.
	 * 
	 * @param name
	 * @param mailAddress
	 * @param designation
	 */
	public OfficialRecipient(String name, String mailAddress, String designation) {
		super(name, mailAddress);
		this.designation = designation;
	}
	
	/**
	 * Setter for designation of the official recipient which will be changed in future.
	 * 
	 * @param newDesignation
	 * 		  The new designation of the specific recipient. 
	 */
	public void setMailAddress(String newDesignation) {
		this.designation = newDesignation;
	}

	/**
	 * Getter
	 * 
	 * @return designation
	 * 		   Designation of the official recipient which might be changed in future.
	 */
	public String getDesignation() {
		return designation;
	}
}
