package finalOne;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class MailReceiver extends MyThread implements Runnable{
	
	private static Properties properties;
	private static Session emailSession;
	private static FlagTerm unseenFlagTerm;
	private static DateFormat dateFormat;
	private String username;
	private String password;
	private String notification;
	private Store emailStore;
	private Folder INBOXFolder;
	private MyBlockingQueue<Message> receivedMailbuffer;
	private List<EmailStatObserver> observers;
	
	static {
		properties = new Properties();
		properties.put("mail.store.protocol", "imaps");
		
		emailSession = Session.getInstance(properties);
		
	    unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
	    
	    dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
	}
	
	public MailReceiver(String username, String password, MyBlockingQueue<Message> receivedMailbuffer) {
		this.username = username;
		this.password = password;
		notification = "";
		this.receivedMailbuffer = receivedMailbuffer;
		observers = new ArrayList<>();
		emailStore = null;
		INBOXFolder = null;
	}
	
	public void attachObserver(EmailStatObserver observer) {
		observers.add(observer);
	}
	
	public void notifyObserver() {
		for(EmailStatObserver observer: observers) {
			observer.update(notification);
		}
	}
	
	public void receiveMail() {
		try {
			INBOXFolder = emailStore.getFolder("INBOX");
			INBOXFolder.open(Folder.READ_WRITE);
	
		    Message[] messages = INBOXFolder.search(unseenFlagTerm);
			
			for(Message message:messages) {
				message.setFlags(new Flags(Flags.Flag.SEEN), true);
				receivedMailbuffer.enqueue(message);
				notification = "An email is received at "+dateFormat.format(System.currentTimeMillis());
				notifyObserver();
			}
		}  
		catch(MessagingException e) {
			System.err.println(e.getMessage());
			System.err.println("Trying to connect with your mail account again");
			try {
				emailStore.connect("imap.gmail.com",username,password);
			}
			catch (MessagingException e1) {
				System.err.println("We couldn't connect with your account");
			} 
		}		
	}
	
	@Override
	public void run() {
		System.out.println("I am ready to receive mails");
		start();
		try {
			emailStore = emailSession.getStore("imaps");
			emailStore.connect("imap.gmail.com",username,password);
			while(isAlive()) {
				receiveMail();
			}
		}
		catch (NoSuchProviderException e) {
			System.err.println(e.getMessage());
		}
		catch (MessagingException e) {
			System.err.println(e.getMessage());
		}
		finally {
			while(!receivedMailbuffer.isEmpty());
			try {
				if(INBOXFolder != null) {INBOXFolder.close(false);}
				if(emailStore != null) {emailStore.close();}
			} 
			catch (MessagingException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
