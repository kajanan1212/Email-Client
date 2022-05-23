package finalOne;

/**
 * It's an abstract class of recipient objects.
 * It has two sub classes also.
 * 
 * @author Kajanan Selvanesan
 * @see PersonalRecipient
 * @see OfficialRecipient
 */

public abstract class Recipient {
	
	/**
	 * Total count of the Recipients in this application.
	 */
	private static int countOfRecipients = 0;
	
	/** Name of the recipient object */
	private String name;
	
	/** Mail address of the recipient object */
	private String mailAddress;
	
	/**
	 * Constructs a recipient object with name and mail address, 
	 * and increment the count of the recipients in this application by one.
	 * 
	 * @param name
	 * 		  Name of the new recipient.
	 * @param mailAddress
	 * 		  Mail address of the new recipient.s
	 */
	public Recipient(String name, String mailAddress){
		this.name = name;
		this.mailAddress = mailAddress;
		countOfRecipients++;
	}
	
	/**
	 * Getter
	 * 
	 * @return name 
	 * 		   The name of the specific recipient.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter
	 * 
	 * @return mailAddress
	 * 		   The mail address of the specific recipient. 
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	
	/**
	 * Setter for mail address of the object which might be changed in future.
	 * 
	 * @param mailAddress
	 * 		  the new mail address of the specific recipient. 
	 */
	public void setMailAddress(String newMailAddress) {
		this.mailAddress = newMailAddress;
	}

	/**
	 * Getter
	 * 
	 * @return countOfRecipients
	 * 		   Total count of the recipient objects which are in this application.
	 */
	public static int getCountOfRecipients() {
		return countOfRecipients;
	}
}
