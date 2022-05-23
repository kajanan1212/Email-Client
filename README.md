# EMAIL CLIENT
In this project, I have implemented a command-line based email client.
<br>
The email client has two types of recipients, official and personal. Some official recipients are close friends.
Details of the recipient list should be stored in a text file. 
<br>
An official recipient’s record in the text file has the following format:
<br>
official: <name>, <email>,<designation>.
<br>
A sample record for official recipients in the text file looks as follows:
<br>
Official: nimal,nimal@gmail.com,ceo
<br>
<br>
A sample record for official friends in the text file looks as follows (last value is the recipient's birthday):
<br>
Office_friend: kamal,kamal@gmail.com,clerk,2000/12/12
<br>
<br>
A sample record for personal recipients in the text file looks as follows (last value is the recipient's birthday):
<br>
Personal: sunil,<nick-name>,sunil@gmail.com,2000/10/10
<br>
<br>
The user should be given the option to update this text file, i.e. the user should be able to add a new recipient through command-line, and these details should be added to the text file.

When the email client is running, an object for each email recipient should be maintained in the application. For this, you will have to load the recipient details from the text file into the application. For each recipient having a birthday, a birthday greeting should be sent on the correct day. Official friends and personal recipients should be sent different messages (e.g. Wish you a Happy Birthday. <your name> for an office friend, and hugs and love on your birthday. <your name> for personal recipients). But all personal recipients receive the same message, and all office friends should receive the same message.  A list of recipients to whom a birthday greeting should be sent is maintained in the application, when it is running. When the email client is started, it should traverse this list, and send a greeting email to anyone having their birthday on that day.

The system should be able to keep a count of the recipient objects. Use static members to keep this count.

All the emails sent out by the email client should be saved into the hard disk, in the form of objects – object serialization can be used for this. The user should be able to retrieve information of all the mails sent on a particular day by using a command-line option.

You only have to send out messages. No need to implement the logic to receive messages.
<br>
Command-line options should be available for:
<br>
·         Adding a new recipient
<br>
·         Sending an email
<br>
·         Printing out all the names of recipients who have their birthday set to current date
<br>
·         Printing out details (subject and recipient) of all the emails sent on a date specified by user input
<br>
·         Printing out the number of recipient objects in the application
<br>
In the given code, note that it imports the javax.mail package. This package is included in the javax.mail.jar.

Tip: With the help of the TAs, first try to get the mail sending functionality, file I/O functionality, and object serialization functionality  implemented. Later you can implement the system to cover all the requirements described above, while adhering to OOP principles.

You are given marks for the

·         Correct implementation of the mail sending functions (i.e. sending a birthday greeting, sending an email based on the instructions given through command-line, ability to serialize email objects,  etc).

·         Correct use of OOP principles

·         Use of coding best practices

Use the given Email_Client.java file as the starting point.

Following email addresses can be used for testing:

Personal 

·  nimalpereracs@gmail.com 

·  cse@1234

Office_friend 

·  kamalfernandocs@gmail.com

·  cse@1234

Save the recipient data into clientList.txt.

Submission information: put all the code into a single pdf file and submit.

