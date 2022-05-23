package finalOne;

/**
 * It's an interface to send and manage the greeting mails.
 * 
 * @author Kajanan Selvanesan
 */
public interface Wishable {
	
	/**
	 * Sends a greeting mail on his/her birthday.
	 */
	public void sendGreeting();
	
	/**
	 * Getter
	 * Helps to know greeting mail on birthday.
	 * 
	 * @return dateOfBirth	
	 */
	public String getDateOfBirth();
	
	/**
	 * Getter
	 * Helps to know the name of the person who has a birthday on that day.
	 * 
	 * @return name
	 */
	public String getName();
	
	/**
	 * Getter
	 * Helps to know the mail address of the person who has a birthday on that day.
	 * 
	 * @return mailAddress
	 */
	public String getMailAddress();
}
