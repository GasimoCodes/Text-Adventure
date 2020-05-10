/**
 * Contains necessary data for Dialog Options.
 * 
 * @author Prokop Svaèina
 */
public class DialogOption {	
	
	String write;
	String gotoID;
	boolean isBattle = false;
	
	public DialogOption(String Message, String gotoOptionID, boolean isbattle){	
		write = Message;
		isBattle = isbattle;
		gotoID = gotoOptionID;
	}
	
}
