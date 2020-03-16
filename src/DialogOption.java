
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
