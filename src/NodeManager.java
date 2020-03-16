
public class NodeManager {

	public void Init()
	{
				
	}
	
	public int getNodeID(String ID, DialogNode[] D_Nodes)
	{
		Integer en = 0;	 //WILL OVERRIDE
		Integer ez = -1; //Will get overwritten
		
		for (DialogNode d : D_Nodes)
		{
			
			if(d.NodeID.compareTo(ID) == 0)
			{
				ez = en;
				//Utils.print("MATCH FOR NODE: " + ID + " WITH NEXT ID: " + en);
			} else
			{
				//Utils.print(" + 1 to the " + en);
				en++;
			}
			
		}
		
		if(ez == -1)
		{
			Utils.print("NO ID MATCH FOUND FOR " + ID + ", PERHAPS THE DATABASE IS CORRUPTED?");
		}
		return ez;
		
	}
	
	
	public int getNodeID(String ID, BattleNode[] B_Nodes)
	{
		Integer en = 0;	 //WILL OVERRIDE
		Integer ez = -1; //Will get overwritten
		
		for (BattleNode d : B_Nodes)
		{
			
			if(d.NodeID.compareTo(ID) == 0)
			{
				ez = en;
				//Utils.print("MATCH FOR NODE: " + ID + " WITH NEXT ID: " + en);
			} else
			{
				//Utils.print(" + 1 to the " + en);
				en++;
			}
			
		}
		
		if(ez == -1)
		{
			Utils.print("NO ID MATCH FOUND FOR " + ID + ", PERHAPS THE DATABASE IS CORRUPTED?");
		}
		return ez;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
