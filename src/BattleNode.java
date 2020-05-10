/**
 * Contains necessary data for Battle Nodes, this is fed to a different system than for the basic Dialog Nodes.
 * 
 * @author Prokop Svaèina
 */
public class BattleNode extends Character {

	
	//Enemy Stats
	BattleProgress[] BP;
	
	
	int Damage;
	int Defense;
	
	Boolean flee = false;
	
	String Description;
	
	String NodeID;
	//Where to go after battle
	String gotoID;
	
	public BattleNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BattleNode(BattleProgress bP, int damage, int defense, Boolean flee, String description, String nodeID, String gotoID) {
		super();
		BP = new BattleProgress[] {bP};
		Damage = damage;
		Defense = defense;
		this.flee = flee;
		Description = description;
		NodeID = nodeID;
		this.gotoID = gotoID;
	}


	
	
}
