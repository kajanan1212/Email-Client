package finalOne;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.mail.Message;
import javax.mail.MessagingException;

public class InboxDatabase extends MyThread implements Runnable{
	private static File inbox;
	private File userInbox;
	private MyBlockingQueue<Message> receivedMailbuffer;
	
	static {
		inbox = new File("INBOX");
		if(!inbox.exists()) {
			inbox.mkdir();
		}
	}
	
	public InboxDatabase(String username, MyBlockingQueue<Message> receivedMailbuffer) {
		this.receivedMailbuffer = receivedMailbuffer;
		userInbox = new File(inbox.getName()+"/"+username);
		if(!userInbox.exists()) {
			userInbox.mkdirs();
		}
	}
	
	public void createAndStoreMailObj(Message receivedMail) throws IOException {
		ObjectOutputStream OOS = null;
		try {
			if(!receivedMail.getSubject().equals("terminateMail019237364ydnsowhzhz<?!*)")) {
				HashMap<String,Object> mailDetails = new HashMap<>();
				mailDetails.put("username",userInbox.getName());
				mailDetails.put("from", receivedMail.getFrom()[0]);
				mailDetails.put("sentDate", receivedMail.getSentDate());
				mailDetails.put("subject", receivedMail.getSubject());
				ReceivedMail receivedMailObj = new ReceivedMail(mailDetails);
				
				OOS = new ObjectOutputStream(new FileOutputStream(userInbox.getPath()+"/"+System.currentTimeMillis()+".txt"));
				OOS.writeObject(receivedMailObj);
				System.out.println("Received mail was stored in HDD");
			}
			
		}
		catch(MessagingException e) {
			System.err.println(e.getMessage());
		}
		catch(FileNotFoundException e) {
			System.err.println("FileNotFound");
		}
		finally {
			if(OOS != null) {OOS.close();}
		}
	}
	
	@Override
	public void run() {
		System.out.println("I am ready to store mails");
		start();
		while(isAlive()) {
			try {
				createAndStoreMailObj(receivedMailbuffer.dequeue());
			} 
			catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		
	} 

}
