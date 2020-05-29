/**
 * Contains necessary data for Dialog Options.
 * 
 * @author Prokop Svaèina
 */
public class DialogOption {	
	
	String write;
	String gotoID;
	boolean isBattle = false;
	
	// New stuff
	String[] commands;
	
	// CONDITIONS: COMMANDS WHICH RETRIEVE TRUE / FALSE, ALL MUST BE RIGHT IN ORDER TO PASS
	String[] conditions;
	
	public DialogOption(String Message, String gotoOptionID, boolean isbattle, String[] commands, String[] conditions){	
		write = Message;
		this.isBattle = isbattle;
		gotoID = gotoOptionID;
		this.commands = commands;
		this.conditions = conditions;
	}
	
}
