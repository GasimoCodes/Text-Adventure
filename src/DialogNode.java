
public class DialogNode {

	// Node ID
	/*
	 * "dead" 	- node called when health reaches 0	
	 * "0" 		- the initial node called on launch
	 */
	String NodeID;
	
	// Node Modifiers with special functions
	/*
	 * 	Read the SpecialCommands() in FrontEnd for list
	 * */
	String[] Args = null;
	
	// Node Text to show
	String[] WriteText;
	
	// Player Input Options
	DialogOption[] Options;

	
	
	
	public DialogNode(String[] TextToWrite, DialogOption[] DOptions, String ID){
		this.Options = DOptions;
		NodeID = ID;
		WriteText = TextToWrite;
	}
	
	public DialogNode(String TextToWrite, DialogOption DOption, String ID){
		this.Options = new DialogOption[]{DOption};
		this.WriteText = new String[]{TextToWrite};
		NodeID = ID;
	}
	
	
	
}
