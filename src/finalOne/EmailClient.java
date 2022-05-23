package finalOne;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailClient {
	
	/**
	 *  It is a file which contains client details in text file format
	 */
	private static File file = new File("clientList.txt");
	
	/**
	 * ArrayList of clients who are in clientList.txt file
	 */
	private static ArrayList<String[]> clientList = new ArrayList<>();
	
	/**
	 * ArrayList of Recipient objects
	 * 
	 * @see Recipient
	 */
	private static ArrayList<Recipient> recipientList = new ArrayList<>();
	
	/**
	 * ArrayList of Wishable objects
	 * 
	 * @see Wishable
	 */
	private static ArrayList<Wishable> wishableClientList = new ArrayList<>();
	
	/**
	 * It is date format which is used is this application 
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	
	/* main method */
	public static void main(String[] args) {
		
		// create Threads for the email receiving part of the EmailClient application
		MyBlockingQueue<Message> receivedMailbuffer = new MyBlockingQueue<>();
		
		MailReceiver mailReceiver = new MailReceiver(Mail.getUsername(),Mail.getPassword(),receivedMailbuffer);
		InboxDatabase inboxDatabase = new InboxDatabase(Mail.getUsername(),receivedMailbuffer);
				
		mailReceiver.attachObserver(new EmailStatRecorder());
		mailReceiver.attachObserver(new EmailStatPrinter(new File("emailStates.txt")));
		
		Thread mailReceiverThread = new Thread(mailReceiver, "Mail receiver");
		Thread mailStorerThread = new Thread(inboxDatabase, "Mail storer");
		
		createRecipientObj(); // Creates the Recipient objects who are in clientList.txt file 
		
		sendBirthdayGreeting(); // Sends greeting mails to Recipient objects who have birthday today which weren't sent yet.
		
		Scanner scanner = new Scanner(System.in);
		
        System.out.println("Enter option type:\n"
              + "1 - Adding a new recipient\n"
              + "2 - Sending an email\n"
              + "3 - Printing out all the recipients who have birthdays\n"
              + "4 - Printing out details of all the emails sent\n"
              + "5 - Printing out the number of recipient objects in the application\n"
              + "6 - Exit");
        
        mailReceiverThread.start();
        mailStorerThread.start();
        
        try {
        int option;
        while((option = Integer.parseInt(scanner.nextLine())) != 6) {
        	/* If user will input 6 then application will stop */
	        switch(option){
	              case 1:
	            	  /* Adding a new recipient */
	            	  try {
		            	  System.out.println("Enter the new recipient details in CORRECT format :\n"
		            			  +"(TypeOfClient: data1,data2...,dataN)");
		            	  String recipient = scanner.nextLine();
		            	  if(isAcceptable(recipient)) {
		            		  writeOnFile(recipient);
		            		  System.out.println("New recipient has been added to the list.");
		            		  sendBirthdayGreeting();
		            	  }
		            	  else {System.err.println("Input format is not Acceptable.");}
	            	  }
	            	  catch(IOException e) {
	            		  System.err.println("We could not complete this process.");
	            	  }
	                  break; 
	              case 2:
	            	  /* Sending an email */
	            	  System.out.println("Insert the details to send mail in the following format :\n"
	            			  +"recipientAddress,subjectOfMail,contentOfMail");
	            	  try {
		            	  String[] mailDetails = scanner.nextLine().split(",");
		            	  String recipientAddress = mailDetails[0];
		            	  String subjectOfMail = mailDetails[1];
		            	  String contentOfMail = mailDetails[2];
		            	  Mail mail = new Mail(recipientAddress,subjectOfMail,contentOfMail,false);
		                  mail.sendMail();
	            	  }
	            	  catch(ArrayIndexOutOfBoundsException e){
	            		  System.err.println("Invalid input.");
	            	  }
	                  break;  
	              case 3:
	            	  /* Printing out all the recipients who have birthdays */
	            	  System.out.println("Insert a date in following format:\n"
	            			  +"(yyyy/MM/dd)");
	            	  String birthdayDate = editDateFormat(scanner.nextLine());
	            	  if(birthdayDate != null) {
		            	  int countOfBdayClient = 0;
		            	  System.out.printf("Following clients have birthday on %s:\n", birthdayDate);
		            	  for(Wishable client:wishableClientList) {
		            		  if(isBirthday(birthdayDate,client.getDateOfBirth())) {
		            			  System.out.println(++countOfBdayClient+") "+client.getName());
		            		  }	  
		            	  }
		            	  if(countOfBdayClient == 0) {
		            		  System.out.printf("No-one has birthday on %s\n", birthdayDate);
		            	  }
	            	  }
	                  break;
	              case 4:
	            	  /* Printing out details of all the emails sent */
	            	  try {
		            	  System.out.println("Insert a date in following format:\n"
		            			  +"(yyyy/MM/dd)");
		            	  String mailDate = editDateFormat(scanner.nextLine());
		            	  if(mailDate != null) {
		            		  Mail.readStoredMail(mailDate);
		            	  }
	            	  }
	            	  catch(IOException e) {
	            		  System.err.println("Somthing is wrong.");
	            	  }
	                  break;
	              case 5:
	            	  /* Printing out the number of recipient objects in the application */
	                  System.out.println("Number of recipient objects in this application :\n"
	                		  +Recipient.getCountOfRecipients());
	                  break;
	        }
	        System.out.println("SELECT ONE OPTION TO CONTINUE :");
        }
        }
        catch(NumberFormatException e) {System.err.println("Invalid input.\nRestart the application.");}
        finally {
	        scanner.close();
	        mailReceiver.stop();
	        try{mailReceiverThread.join();} catch(InterruptedException e) {}
	        while(!receivedMailbuffer.isEmpty());
	        try{Thread.sleep(1000);} catch(InterruptedException e) {}
	        inboxDatabase.stop();
	        try {
	        	Mail terminateMail = new Mail(Mail.getUsername(),"terminateMail019237364ydnsowhzhz<?!*)","",false);
				receivedMailbuffer.enqueue(terminateMail.createMail());
			} catch (MessagingException e) {
				System.out.println(e.getMessage());
			}
	        System.out.println("...END...");
        }
	}
	
	/**
	 * It creates new Recipient objects which are in clientList.txt file
	 * and add each of them into the recipientList or wishableClientList.
	 */
	private static void createRecipientObj() {
		String name, nickname, mailAddress, dateOfBirth, designation;
		try {
			readClientFile();
			for(String[] clientDetail : clientList) {
				String typeOfObj = clientDetail[0];
				if(typeOfObj.equalsIgnoreCase("Personal")) {
					String[] personalClientDetail = clientDetail[1].split(",");
					name = personalClientDetail[0];
					nickname = personalClientDetail[1]; 
					mailAddress = personalClientDetail[2];
					dateOfBirth = editDateFormat(personalClientDetail[3]);
					PersonalRecipient PR = new PersonalRecipient(name,nickname,mailAddress,dateOfBirth);
					recipientList.add(PR);
					wishableClientList.add(PR);
				}
				else if(typeOfObj.equalsIgnoreCase("Office_friend")) {
					String[] officeFriendDetail = clientDetail[1].split(",");
					name = officeFriendDetail[0];
					mailAddress = officeFriendDetail[1]; 
					designation = officeFriendDetail[2];
					dateOfBirth = editDateFormat(officeFriendDetail[3]);
					OfficeFriend OF = new OfficeFriend(name,mailAddress,designation,dateOfBirth);
					recipientList.add(OF);
					wishableClientList.add(OF);
				}
				else if(typeOfObj.equalsIgnoreCase("Official")) {
					String[] officialClientDetail = clientDetail[1].split(",");
					name = officialClientDetail[0];
					mailAddress = officialClientDetail[1]; 
					designation = officialClientDetail[2];
					OfficialRecipient OR = new OfficialRecipient(name,mailAddress,designation);
					recipientList.add(OR);
				}
			}
			System.out.println("Client objects have been created successfully.");	
		}
		catch(IOException e) {System.err.println("Somthing is wrong.");}
	}
	
	/**
	 * It creates a new Recipient object and add it into the recipientList or wishableClientList.
	 * 
	 * @param typeOfRecipient
	 * @param RecipientDetails
	 */
	private static void createRecipientObj(String typeOfRecipient,String[] RecipientDetails) {
		if(typeOfRecipient.equalsIgnoreCase("Official")) {
			OfficialRecipient OR = new OfficialRecipient(RecipientDetails[0],RecipientDetails[1],RecipientDetails[2]);
			recipientList.add(OR);
		}
		else if(typeOfRecipient.equalsIgnoreCase("Office_friend")) {
			OfficeFriend OF = new OfficeFriend(RecipientDetails[0],RecipientDetails[1],RecipientDetails[2],editDateFormat(RecipientDetails[3]));
			recipientList.add(OF);
			wishableClientList.add(OF);
		}
		else if(typeOfRecipient.equalsIgnoreCase("Personal")) {
			PersonalRecipient PR = new PersonalRecipient(RecipientDetails[0],RecipientDetails[1],RecipientDetails[2],editDateFormat(RecipientDetails[3]));
			recipientList.add(PR);
			wishableClientList.add(PR);
		}
		System.out.println("A new object has been created.");	
	}
	
	/**
	 * It reads clientList.txt file and add the clients which are in that file
	 * into the ArrayList of clientsList. If clientList.txt file is empty then
	 * it says EMPTY.
	 * <br>FileNotFoundException is handled.
	 * 
	 * @throws IOException
	 */
	private static void readClientFile() throws IOException{
		
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(file));
			String clientDetails;
			boolean isEmpty = true;
			while((clientDetails = bReader.readLine()) != null) {
				clientList.add(clientDetails.split(": "));
				if(isEmpty) {isEmpty = false;}
			}
			if(isEmpty) {System.err.println("Client list is EMPTY...");}
			else {System.out.println("Client list has been read successfully.");}
		}
		catch(FileNotFoundException e) {System.err.println("Client list is missing...");}
		finally {
			if(bReader != null) {
				bReader.close();
			}
		}
	}
	
	/**
	 * It goes through the ArrayList of the wishableClientList and 
	 * searches the recipients who have birthday today.
	 * If any-one has birthday today in that list and a greeting wasn't sent yet
	 * then it calls sendGreeting method to that object.
	 * 
	 * @see Wishable
	 */
	private static void sendBirthdayGreeting() {
		String today = dateFormat.format(System.currentTimeMillis());
		for(Wishable client:wishableClientList) {
			if(isBirthday(today,client.getDateOfBirth())
					&& !Mail.isGreetingSent(client.getMailAddress())){
				System.out.println(client.getName()+"'s birthday today.");
				System.out.print("A greeting is ");
				client.sendGreeting();
			}	
		}
	}
	
	/**
	 * It helps to find whether a client has a birthday on user inputed date
	 * or not.
	 * <br>Month and date should be equal in both of the dates and 
	 * year of the user inputed date should be larger than or equal to the
	 * year of that client date of birth. 
	 * 
	 * @param DateOfComparison
	 * 		  - User inputed date
	 * @param ClientDOB
	 * 		  - Client date of birth
	 * @return true/false
	 */
	private static boolean isBirthday(String DateOfComparison,String ClientDOB) {
		Calendar dateOfComparison = Calendar.getInstance();
		Calendar clientDOB = Calendar.getInstance();
		try {
			dateOfComparison.setTime(dateFormat.parse(DateOfComparison));
			clientDOB.setTime(dateFormat.parse(ClientDOB));
			
			return clientDOB.get(Calendar.YEAR) <= dateOfComparison.get(Calendar.YEAR)
					&& clientDOB.get(Calendar.MONTH) == dateOfComparison.get(Calendar.MONTH)
					&& clientDOB.get(Calendar.DATE) == dateOfComparison.get(Calendar.DATE);
			
		} catch (ParseException e) {
			System.err.println("Invalid date format.");
			return false;
		}
	}
	
	/**
	 * It checks whether the user inputed recipient format is acceptable or not.
	 * <br>Acceptable formats : (Both lower case and upper case are acceptable)
	 * <br>Official: name,mail address,designation
	 * <br>Office_friend: name,mail address,designation,date of birth
	 * <br>Personal: name,nick name,mail address,date of birth 
	 * (Date of birth should be in format of yyyy/MM/dd)
	 * 
	 * @param recipient
	 * @return true/false
	 */
	private static boolean isAcceptable(String recipient) {
		int indexOfEndOfType = recipient.indexOf(": ");
		if (indexOfEndOfType == -1) {return false;}
		else {
			String typeOfRecipient = recipient.substring(0, indexOfEndOfType);
			String[] RecipientDetails = recipient.substring(indexOfEndOfType+2).split(",");
			if(typeOfRecipient.equalsIgnoreCase("Official")
					&& RecipientDetails.length == 3) {
				createRecipientObj(typeOfRecipient,RecipientDetails);
				return true;
			}
			else if((typeOfRecipient.equalsIgnoreCase("Office_friend")
					|| typeOfRecipient.equalsIgnoreCase("Personal"))
					&& RecipientDetails.length == 4
					&& isDateAcceptable(RecipientDetails[3])) {
				createRecipientObj(typeOfRecipient,RecipientDetails);
					return true;
			}
			else {return false;}
		}
	}
	
	/**
	 * It checks whether the date which is gotten from parameter is in yyyy/MM/dd format or not
	 * 
	 * @param date
	 * @return true/false
	 */
	private static boolean isDateAcceptable(String date) {
		try {
			dateFormat.parse(date);
			return true;
		}
		catch(ParseException e){return false;}
	}
	
	/**
	 * It writes details of the new recipient in clientList.txt file,
	 * and add the new line at the end of the details.
	 * 
	 * @param newRecipient
	 * @throws IOException
	 */
	private static void writeOnFile(String newRecipient) throws IOException{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file,true));
		bWriter.write(newRecipient);
		bWriter.newLine();
		bWriter.close();	
	}
	
	/**
	 * It edits the format of the date which is gotten from the parameter
	 * into yyyy/MM/dd format. If it throws ParseException then it says Invalid date format.
	 * 
	 * @param date
	 * @return edited date
	 */
	private static String editDateFormat(String date) {
		try {
			return dateFormat.format(dateFormat.parse(date));
		}
		catch(ParseException e) {
			System.err.println("Invalid date format.");
			return null;
		}
	}	
}