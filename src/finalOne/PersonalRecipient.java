package finalOne;

/**
 * It's a class of personal recipient objects.
 * It extends Recipient and 
 * implements Wishable
 * 
 * @author Kajanan Selvanesan
 * @see Recipient
 * @see Wishable
 */

public class PersonalRecipient extends Recipient 
		implements Wishable{
	
	/**
	 * It is a greeting message for each of the personal recipient on his/her birthday.
	 */
	private static String greetingMessage = "Hugs and love on your birthday...\n"+"Kajanan Selvanesan";
	
	/** Nick name of the personal recipient object */
	private String nickName;
	
	/**
	 * Date of birth of the personal recipient object.
	 * It is used to send a greeting mail on his/her birthday. 
	 */
	private String dateOfBirth;
	
	/**
	 * Constructs a personal recipient object with name, nick name, mail address and date of birth.
	 * Passes the values of name and mail address to its super class.
	 * 
	 * @param name
	 * @param nickName
	 * @param mailAddress
	 * @param dateOfBirth
	 */
	public PersonalRecipient(String name, String nickName, String mailAddress,String dateOfBirth) {
		super(name, mailAddress);
		this.nickName = nickName;
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
		String bodyOfMail = "Dear "+nickName+",\n"+greetingMessage;
		Mail greetingMail = new Mail(this.getMailAddress(),"Birthday wish",bodyOfMail,true);
		greetingMail.sendMail();
	}
}
