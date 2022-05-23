package finalOne;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * It is the Mail class which maintains all the mail objects.
 * Gmail user can create and send mails using this class.
 * Mail objects are stored to hardware after they were sent,
 * and we can read any stored mails on specific date by using <b>readStoredMail</b> static method of this class.
 * We can find whether the greeting mail has been sent to specific mail address or not 
 * by using <b>isGreetingSent</b> static method of this class. 
 * 
 * @author Kajanan Selvanesan
 */
public class Mail implements Serializable{
	
	/**
	 * Serial Version UID.
	 * Helps during deserialization to find the correct class.
	 */
	@java.io.Serial
    private static final long serialVersionUID = 1L;
	
	/**
	 * User's valid mail address
	 * 
	 */
	private static String userName = "USERNAME";
	
	/**
	 * User's correct password to the specific {@link userName}
	 */
	private static String password = "PASSWORD";
	
	/**
	 * It is the date format which is used in this Mail class for all of the mail objects.
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	/**
	 * It contains the details regarding the host which is used in this Mail class for all of the mail objects.
	 */
	private static Properties propertiesOfHost;
	
	static {
		propertiesOfHost = new Properties();
		propertiesOfHost.put("mail.smtp.host", "smtp.gmail.com");
		propertiesOfHost.put("mail.smtp.port", "587");
		propertiesOfHost.put("mail.smtp.auth", "true");
		propertiesOfHost.put("mail.smtp.starttls.enable", "true");
	}
	
	/**
	 * It contains the propertiesOfHost as Properties data structure
	 * and a object of anonymous class where the getPasswordAuthentication method 
	 * of Authenticator is overridden.
	 */
	private static Session session = Session.getDefaultInstance(propertiesOfHost, new Authenticator() {
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName,password);
		}	
	});
	
	/**
	 * It is a folder where all of the sent Mail objects are going to be stored in computer hardware.
	 */
	private static File storedMail_Folder;
	
	static {
		storedMail_Folder = new File("storedMail/storedGreetingMail");
		if(!storedMail_Folder.exists()) {
			/* If there are not folders such as storedMail and storedGreetingMail then they will be created */
			storedMail_Folder.mkdirs();
		}
	}
	
	/**
	 * It contains the date when the mail was sent.
	 */
	private String mailSentDate;
	
	/**
	 * Recipient's address of the mail object
	 */
	private String recipientAddress;
	
	/**
	 * Subject of the mail object
	 */
	private String subject;
	
	/**
	 * Body of the mail object
	 */
	private String body;
	
	/**
	 * It says that it is either a greeting mail which was created automatically by application  
	 * or normal mail which was created by user by using command line.
	 */
	private boolean isGreeting;
	
	/**
	 * Constructs a mail object with address of recipient, subject of the mail, 
	 * the boolean value of isGreeting and the date when the mail was sent.
	 * 
	 * @param recipientAddress
	 * @param subject
	 * @param body
	 * @param isGreeting
	 */
	public Mail(String recipientAddress,String subject,String body,boolean isGreeting) {
		this.recipientAddress = recipientAddress;
		this.subject = subject;
		this.body = body;
		this.isGreeting = isGreeting;
		mailSentDate = "Not sent yet...";
		
	}
	
	/**
	 * Setter
	 * <br>
	 * Set the new mail address of user
	 * and set the correct password of that specific mail address.
	 * 
	 * @param newUserName
	 * @param newPassword
	 */
	public static void changeUser(String newUserName, String newPassword) {
		userName = newUserName;
		password = newPassword;
	}
	
	/**
	 * Getter
	 * 
	 * @return userName
	 * 		   - Mail address of the current user
	 */
	public static String getUsername() {
		return userName;
	}
	
	/**
	 * Getter
	 * 
	 * @return password
	 * 		   - Mail password of the current user
	 */
	public static String getPassword() {
		return password;
	}
	
	/**
	 * Getter
	 * 
	 * @return recipientAddress
	 * 		   - Mail address of the recipient
	 */
	public String getRecipientAddress() {
		return recipientAddress;
	}
	
	/**
	 * Getter
	 * 
	 * @return subject
	 * 		   - Subject of the mail
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * Getter
	 * 
	 * @return body
	 * 		   - Body of the mail
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Getter
	 * 
	 * @return mailSentDate
	 * 		   - The date when the mail was sent
	 */
	public String getMailSentDate() {
		return mailSentDate;
	}
	
	/**
	 * Creates and returns a Message object of the mail which is going to be sent.
	 * 
	 * @return message
	 * @throws MessagingException
	 */
	public Message createMail() throws MessagingException{
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
		message.setSubject(subject);
		message.setText(body);
		return message;
	
	}
	
	/**
	 * Sends the prepared mail,
	 * sets the value to <b>mailSentDate</b>
	 * and calls the <b>storeMail</b> method to store the mail object in <b>storedMail</b> folder.
	 * It handles the Exceptions as well such as SendFailedException, MessagingException
	 * and IOException.
	 */
	public void sendMail() {
		
		try {
			System.out.println("Sending...");
			Message message = createMail();
			Transport.send(message);
			mailSentDate = dateFormat.format(System.currentTimeMillis());
			System.out.println((this.isGreeting?"Greeting mail":"Mail")
							+" was sent to "+this.getRecipientAddress()+" successfully.");
			storeMail();
		}
		catch(SendFailedException e) {
			System.err.println("Invalid mail address.\nCheck it out and change it.");
		}
		catch(MessagingException e) {
			System.err.println("Sorry something is wrong.\nCheck your connection.\nWe could not send the mail.");
		}
		catch(IOException e) {
			System.err.println("We can not store this mail details on hardware.");
		}
		
	}
	
	/**
	 * Stores sent mail object in <b>storedMail</b> folder.
	 * All the mails which were sent on the same date are stored in same file.
	 * If it is greeting mail then in addition to store in <b>storedMail</b> folder 
	 * it is stored as separate file in <b>storedGreetingMail</b> folder which is in inside the <b>storedMail</b> folder.
	 * <br>FileNotFoundException is handled.
	 * 
	 * @throws IOException
	 */
	private void storeMail() throws IOException{
		
		ObjectOutputStream OOStreamMail = null;
		ObjectOutputStream OOStreamGreeting = null;
		File storedMail_sentDateFile = new File(storedMail_Folder.getParent()
				+"/"+this.getMailSentDate().replace('/', '_')+".txt");
		/* Name of the file looks like yyyy_MM_dd */
		try {
			OOStreamMail = new ObjectOutputStream(new FileOutputStream(storedMail_sentDateFile,true));
			OOStreamMail.writeObject(this);
			System.out.println((this.isGreeting?"Greeting mail":"Mail")
							+" details have been stored in storedMail Folder successfully.");
			if(isGreeting) {
				File storedGreeting_recipientFile = new File(storedMail_Folder.getPath()
						+"/"+this.getRecipientAddress()+".txt");
				OOStreamGreeting = new ObjectOutputStream(new FileOutputStream(storedGreeting_recipientFile,true));
				OOStreamGreeting.writeObject(this);
				System.out.println("Greeting mail details have been stored in storedGreetingMail Folder successfully.");
			}
		}
		catch(FileNotFoundException e) {
			System.err.println("File is not found.\nBecause of that this mail details were not stored in hardware.");
		}
		finally {
			if(OOStreamMail != null) {OOStreamMail.close();}
			if(OOStreamGreeting != null) {OOStreamGreeting.close();}
		}
	}
	
	/**
	 * Searches file name which is similar to the date which is gotten from the parameter.
	 * If it doesn't exist then says any mail was not sent on that date,
	 * else prints recipient address and subject of each mail object which is in that specific file.
	 * ClassNotFoundException and EOFException are handled.
	 * 
	 * @param date
	 * @throws IOException
	 */
	public static void readStoredMail(String date) throws IOException{
		
		FileInputStream FIStream = null;
		ObjectInputStream OIStream = null;
		File storedMail_sentDateFile = new File(storedMail_Folder.getParent()
				+"/"+date.replace('/', '_')+".txt");
		if(!storedMail_sentDateFile.exists()) {
			System.out.printf("Any mail was not sent on %s.\n",date);
		}
		else {
			try {
				FIStream = new FileInputStream(storedMail_sentDateFile);
				while(true) {
					OIStream = new ObjectInputStream(FIStream);
					Mail storedMail = (Mail)OIStream.readObject();
					System.out.println("Recipient's Address : "+storedMail.getRecipientAddress()+"\n"
							+"Subject of the mail : "+storedMail.getSubject()+"\n");
				}
			}
			catch(ClassNotFoundException e) {
				System.err.println("Class couldn't be found.");
			}
			catch(StreamCorruptedException e) {
				System.err.println("The specific file has been edited/corrupted .\n"
							+"That file should be deleted.(Some stored mails will be lost)");
			}
			catch(EOFException e) {
				System.out.println("Finished...");
			}
			finally {
				if(OIStream != null) {OIStream.close();};
				if(FIStream != null) {FIStream.close();};
			}
		}
	}
	
	/**
	 * Helps to find whether the greeting mail has been sent to RecipientAddress
	 * which is gotten from the parameter or not.
	 * It searches file which has name with specific mail address in 
	 * <b>storedGreetingMail</b> folder which is in inside the <b>storedMail</b> folder.
	 * RecipientAddress.txt file in <b>storedGreetingMail</b> should be clear 
	 * before his/her next birthday.
	 * 
	 * @param RecipientAddress
	 * @return true or false
	 */
	public static boolean isGreetingSent(String RecipientAddress) {
		
		File storedGreeting_recipientFile = new File(storedMail_Folder.getPath()
				+"/"+RecipientAddress+".txt"); 
		return storedGreeting_recipientFile.exists();
			
	}
}
