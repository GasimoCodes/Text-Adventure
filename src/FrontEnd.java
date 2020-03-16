import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.google.gson.*;

public class FrontEnd {
		
	
	Gson gson = new Gson();
	
	boolean end = false;
	//STORES ALL DIALOG DATA
	DialogNode[] D_Nodes;
	BattleNode[] B_Nodes;
	
	//Player Statistics
	PlayerStats stats = new PlayerStats();
	ItemManager Mgr = new ItemManager();
	NodeManager NMgr = new NodeManager();
	//Temporary String to Read/Write
	public String TempString;
	
	//Additional Properties
	Scanner sc;
	boolean Await = false;
	

	public void Init()
	{
		
		File file = new File(System.getProperty("user.dir")+"/"+"DIALOG.nodes");
		Mgr.Init();
		
		//READ THE DIALOG
		try {
			sc = new Scanner(file, "UTF-8");
			sc.useDelimiter("\\Z"); 
			TempString = sc.next();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		D_Nodes = null;
		D_Nodes = gson.fromJson(TempString, DialogNode[].class);
		
		if (D_Nodes == null) {
			Utils.print("An Error occured while reading: " + file + ", which cannot be correctly parsed.\n The operation will now retry.");
			Init();
		}
		
		
		//READ BATTLE DATABASE
		File file2 = new File(System.getProperty("user.dir")+"/"+"BATTLE.nodes");
		
				try {
					Scanner sce = new Scanner(file2, "UTF-8");
					sce.useDelimiter("\\Z");
					TempString = sce.next();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Utils.print(e);
				}
				
				B_Nodes = null;
				B_Nodes = gson.fromJson(TempString, BattleNode[].class);
				
				if (B_Nodes == null) {
					Utils.print("An Error occured while reading: " + file2 + ", which cannot be correctly parsed.\n The operation will now retry.");
					Init();
				}
		
		
		
		
		
		Run();
		
		
	}
	
	
	public void Run()
	{
		boolean isBattleTime = false;
		sc = new Scanner(System.in);
		end = false;
		Integer ID = 0;
		String nodeID = "0";
		
		
		Utils.print("PRAGUE 2033");
		Utils.print(Utils.div());
		Utils.print("Vždy mùžete napsat /help pro zobrazení pøíkazù");
		Utils.print(Utils.div());
		
		while(end != true)
		{
			
			
	// Find node with ID		
	//		-> Check if not Battle
			
			
	if(!isBattleTime)
	{		
			
		if(stats.isAlive)
			ID = NMgr.getNodeID(nodeID, D_Nodes);
		else
			ID = NMgr.getNodeID("dead", D_Nodes);
			
			
	// Write Node Start
			
			for (String s : D_Nodes[ID].WriteText) 
			{ 
			    Utils.print(s);
			    try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
	// Execute Commands		
			
			if(D_Nodes[ID].Args != null)
			{
				//Utils.print("ARGS DETECTED");
				SpecialCommand(D_Nodes[ID].Args);
			}
	
	// Check if the game hasnt ended already, if so we dont want any input from the nodes to be displayed.
		if(end != true)
		{
				
	// Write Options
			int i = 0;
			for (DialogOption s : D_Nodes[ID].Options) 
			{ 
			    i++;
			    Utils.print("-> \t" + s.write + "\t[" + i + "]");
			    //Utils.print(i);
			    
			    try {			    	
					Thread.sleep(500);
				
			    } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			Utils.print(Utils.div());

			
			boolean waitInput = true;
			String Input = "";
			
	//INPUT PHASE
			while(waitInput) {
			
				Utils.printCol("-> \t");
				Input = sc.nextLine();
				
				
				// IF HELP
				if(Input.toLowerCase().compareTo("/help") == 0)
				{
					WriteHelpDialog();
					Utils.print(Utils.div());
					
				// Write Options
					i = 0;
					for (DialogOption s : D_Nodes[ID].Options) 
					{ 
					    i++;
					    Utils.print("-> \t" + s.write + "\t[" + i + "]");
					    
					    try {			    	
							Thread.sleep(500);
					    } catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				// ELSE	
				} else {
					try {
					
						if(Integer.parseInt(Input) <= i && Integer.parseInt(Input) > 0)
						{
							waitInput = false;
							//Utils.print(i);
							
						} else {
						
						Utils.print("Èíslo " + Input + " není ani jedna z možností");
						WriteHelpDialog();
						
						}
						
						
					} catch(Exception e) {
						Utils.print("Napište /help pro pomoc pøi zadávání." + e.toString());
					}
				}
				
			} // END OF LOOP
			
			// SELECT FROM ARRAY
			
			
			
			//Utils.print(Input + " I2 " + " ID THIS: " + ID );
			String nextNodeID;
			
			//Select ID
			nextNodeID = D_Nodes[ID].Options[Integer.parseInt(Input) - 1].gotoID;
			
			//Pront
			Utils.print("->\t" + D_Nodes[ID].Options[Integer.parseInt(Input) - 1].write);
			
			//Check if its battle type ID
			isBattleTime = D_Nodes[ID].Options[Integer.parseInt(Input) - 1].isBattle;
			
			
			nodeID = nextNodeID;	
			//Utils.print(nodeID);
			
		//End of check if game already ended.	
		}
			
			Utils.print(Utils.div());
			
	} // END OF ISBATTLE = FALSE
	else
	{
		//IS BATTLE
		
		
		Integer en = 0;
		Integer ez = -1;
		for (BattleNode d : B_Nodes)
		{
			
			if(d.NodeID.compareTo(nodeID) == 0)
			{
				ez = en;
			} else
			{
				en++;
			}
			
		}
		
		if(ez == -1)
		{
			Utils.print("NO ID MATCH FOUND FOR " + nodeID + "(B_ID), PERHAPS THE DATABASE IS CORRUPTED?");
		}
		
		ID = ez;
					
		Await = true;

		Battle(B_Nodes[ID]);
		while(Await)
		{
		//SLEEEEEEEEEEEEEEEEEP?.	
		}
		
		
	
	}
			
		}
			
		
	}
	
	public void WriteHelpDialog()
	{	
		Utils.print("Napište èíslo aby jste zvolili výše zobrazenou možnost.");
		//Utils.print("/inv \t pro zobrazení inventáøe");
	}
	
	
	
	public void Battle(BattleNode node)
	{
		boolean IsBattling = true;
		
		//BATTLE LOGIC HERE
		while(IsBattling) //ADD IS ALIVE HERE :EYES:
		{
			
		
			// Ask what to do
		if(stats.ActiveWeaponID != 0)
		{
		Utils.print("->\tZaútoèit pomocí " + Mgr.getItem(stats.ActiveWeaponID.toString()).name + "[1]");
		} else {
		Utils.print("->\tZaútoèit pomocí " + Mgr.getItem(stats.ActiveWeaponID.toString()).name + "[1]");
		}
		
		
		
		
		Utils.print("->\tOchránit se");
		
		
		
			
		// Get input
		boolean inpwait = true;
		while(inpwait)	{
			try {
				
				Integer Input = Integer.parseInt(sc.nextLine());
				
				if(Input > 0 && Input <= 3)
				{
				inpwait = false;
				} else {
				Utils.print("Akce " + Input + " neexistuje. Zkuste to znovu nebo použijte /help.");
				}
			
			} catch(Exception E) {
				Utils.print("Zadejte prosím èíslo pro zvolení akce nebo /help pro pomoc");
				inpwait = true;
			}
		}
		
		//Battle Results
			
			
		}
	
	}
	
	
	
	
	public void SpecialCommand(String[] Commands) {
	
		for(String cmd : Commands)
		{
			
			String[] Args = Utils.inputSerialize(cmd, " ");
			
			
			//giveItem <ItemID>
			if (Args[0].toLowerCase().compareTo("giveitem") == 0 && Args.length == 2)
			{
				
				
			}
			
			//giveWeapon <WeaponID>
			if (Args[0].toLowerCase().compareTo("giveweapon") == 0 && Args.length == 2)
			{
				
				
			}
			
			
			//clearWeapon
			if (Args[0].toLowerCase().compareTo("clearweapon") == 0 && Args.length == 1)
			{
				
				
			}
			
			
			//giveMedkit
			if (Args[0].toLowerCase().compareTo("givemedkit") == 0 && Args.length == 1)
			{
				
			}
			
			
			//set health
			if (Args[0].toLowerCase().compareTo("sethealth") == 0 && Args.length == 2)
			{
				try
				{
					stats.setHealth(Integer.parseInt(Args[1]));
				} catch (Exception e) {
				Utils.print("Cannot execute setHealth: " + e.toString());
				}
			}
			
			
			//set damage
			if (Args[0].toLowerCase().compareTo("damage") == 0 && Args.length == 2)
			{
				try
				{
					stats.damage(Integer.parseInt(Args[1]));
				} catch (Exception e) {
				Utils.print("Cannot execute setHealth: " + e.toString());
				}
			}
			
			
			//set heal
			if (Args[0].toLowerCase().compareTo("heal") == 0 && Args.length == 2)
			{

				try
				{
					stats.heal(Integer.parseInt(Args[1]));
				} catch (Exception e) {
				Utils.print("Cannot execute setHealth: " + e.toString());
				}
			}
			
			//set heal
			if (Args[0].toLowerCase().compareTo("end") == 0 && Args.length == 1)
			{
					end = true;
					Utils.print("Game ended.");
					stats = new PlayerStats();
			}
			
			
		}
		
		
		//List of commands:
		/*
			giveItem <ItemID>		// Gives item by id 
			giveWeapon <WeaponID>	// Gives weapon by id
			clearWeapon				// Clears all weapons
			giveMedkit				// Gives x medkits 
			
			setHealth <amount>		// Forces health value
			damage <amount>			// Deals damage by amount
			heal <amount>			// Heals by amount
			
			end						// Ends the game, indicates the last node in branch.
			
		*/
		
	}
	
	
	
	
	
}
