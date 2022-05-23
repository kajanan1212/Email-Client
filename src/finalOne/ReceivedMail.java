package finalOne;

import java.io.Serializable;
import java.util.HashMap;

public class ReceivedMail implements Serializable{
	
	@java.io.Serial
    private static final long serialVersionUID = 987654321L;
	
	private HashMap<String,Object> mailDetails;	
	
	public ReceivedMail(HashMap<String,Object> mailDetails) {
		this.mailDetails = mailDetails;
	}

	public HashMap<String, Object> getMailDetails() {
		return mailDetails;
	}	
}
