package finalOne;

/**
 * It's a class of office-friend recipient objects.
 * It extends OfficialRecipient and 
 * implements Wishable
 * 
 * @author Kajanan Selvanesan
 * @see OfficialRecipient
 * @see Wishable
 */

public class OfficeFriend extends OfficialRecipient 
		implements Wishable{
	
	/**
	 * It is a greeting message for each of the office-friend recipient on his/her birthday.
	 */
	private static String greetingMessage = "Wish you a Happy Birthday...\n"+"Kajanan Selvanesan";
	
	/**
	 * Date of birth of the personal recipient object.
	 * It is used to send a greeting mail on his/her birthday. 
	 */
	private String dateOfBirth;
	
	/**
	 * Constructs a office-friend recipient object with name, mail address, designation and date of birth.
	 * Passes the values of name and mail address to its super class.
	 * 
	 * @param name
	 * @param mailAddress
	 * @param designation
	 * @param dateOfBirth
	 */
	public OfficeFriend(String name,String mailAddress,String designation,String dateOfBirth) {
		super(name,mailAddress,designation);
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * Getter
	 * Implements the method which is declared in the Wishable interface.
	 * 
	 * @return dateOfBirth
	 * 		   Date of birth of the recipient object.
	 * @see Wishable
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	
	/**
	 * Implements the method which is declared in the Wishable interface.
	 * Creates a greeting mail object using Mail class reference.
	 * Sends that greeting mail on his/her birthday by using sendMail method which is in Mail class.
	 * 
	 * @see Mail
	 * @see Wishable
	 */
	public void sendGreeting() {
		String bodyOfMail = "Dear "+this.getName()+",\n"+greetingMessage;
		Mail greetingMail = new Mail(this.getMailAddress(),"Birthday wish",bodyOfMail,true);
		greetingMail.sendMail();
	}
	
	

}
