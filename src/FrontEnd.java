import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import com.google.gson.*;

@Deprecated
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
	String nodeID = "0";
	//Additional Properties
	Scanner sc;
	boolean Await = false;
	
	Window GUI = new Window();
	Thread GUI_T = new Thread(GUI);

	
	
	public void Init()
	{
		GUI_T.start();
		stats.Init();
		File file = new File(System.getProperty("user.dir")+"/"+"DIALOG.nodes");
		
		SwingUtilities.invokeLater(new Runnable(){

		    public void run(){
		        GUI.print("sometext1");
		    }
		});

		
// Disabled Manager for compatibility reasons as this is now OBSOLETE		
// Mgr.End = this;
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
		
		stats.activeWeapon = Mgr.getWeapon("0");
		
		//READ BATTLE DATABASE
		File file2 = new File(System.getProperty("user.dir")+"/"+"BATTLE.nodes");
		
				try {
					Scanner sce = new Scanner(file2, "UTF-8");
					sce.useDelimiter("\\Z");
					TempString = sce.next();
					sce.close();
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
		
		isBattleTime = false;
	
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
			
		Utils.print("->\t(Vaše životy: " + stats.Health+")" + "\t(" + node.name + " životy" + ": " + node.health + ")");
		
			// Ask what to do
		if(stats.activeWeapon.id.compareTo("0") != 0)
		{
		Utils.print("->\tZaútoèit pomocí " + stats.activeWeapon.name + "[1]");
		} else {
		Utils.print("->\tZaútoèit. " + "[1]");
		}
		Utils.print("->\tBránit se. [2]");
			
		if(stats.healthPacks >= 1)
		{
		Utils.print("->\tPoužít Lékárnièku. (" + stats.healthPacks + ") [3]");
		}
		
		Utils.print(Utils.div());	
		
		// Get input
		boolean inpwait = true;
		int inp = 0;
		while(inpwait)	{
			try {
				
				Utils.printCol("-> \t");
					
				
				Integer Input = Integer.parseInt(sc.nextLine());
				inp = Input;
				
			if(Input > 0 && Input <= 3)
			{
					
				// IF ELSE
				if(Input != 3)
				{
					
					inpwait = false;
						
						
				} else {
				
					
					// IF HEAL
					if(Input == 3 && stats.healthPacks <= 0)
					{
						
						Utils.print("Nelze použít Lékárnièku, protože žádná nezbývá v inventáøi.");
						inpwait = true;
					
					} else {
						
						if(inp == 3)
						{
							Utils.print("->\tPoužili jste Lékárnièku.");
							stats.useHealthPack(Utils.Math.randInt(50, 100));	
							inpwait = true;
						}
						
					}
					
				}

					
				} else {
				
					Utils.print("Akce " + Input + " neexistuje. Zkuste to znovu nebo použijte /help.");
				}	
			
			} catch(Exception E) {
				Utils.print("Zadejte prosím èíslo pro zvolení akce nebo /help pro pomoc");
				inpwait = true;
			}
		}
		
// APPLY BATTLE
		
		double DefenceFactor = 1d - (stats.Defense/100d);
		
		if(inp == 1)
		{
			DefenceFactor += DefenceFactor * 0.2;
		
			if(DefenceFactor > 1)
				DefenceFactor = 1;
			
			Utils.print(DefenceFactor);
			
			Utils.print("->\tZaútoèil jste pomocí " + stats.activeWeapon.name);
			node.health -= Utils.Math.randInt(stats.activeWeapon.damage,stats.activeWeapon.damage + 10) * (1 - (node.Defense/100));
		}
		
		if(inp != 2 && node.health > 0)
			stats.damage(Utils.Math.randInt(node.Damage,node.Damage + 10) * DefenceFactor);
		
		if(inp == 2 && node.health > 0) {
			DefenceFactor = DefenceFactor /2 ;
			
			Utils.print(DefenceFactor);
			
			if(DefenceFactor > 1)
				DefenceFactor = 1;
			
			stats.damage(Utils.Math.randInt(node.Damage,node.Damage + 10) * DefenceFactor);
		}
		
		//Utils.print(node.Damage + "//" + stats.Defense);
		//Utils.print(stats.activeWeapon.damage + "//" + node.Defense);
		
		// Attack

			//stats.health = stats.health - 20;//Utils.Math.randInt(node.Damage,node.Damage + 5);// * (1 - (stats.Defense/100));
		
		
		// BATTLE ENDED, WIN
			if(node.health <= 0 && stats.Health > 0)
			{
				nodeID = node.gotoID;
				Await = false;
				IsBattling = false;
			} 
		// BATTLE ENDED, FAILED.		
			else if(stats.Health <= 0d)
			{
				nodeID = "dead";
				Await = false;
				IsBattling = false;
			}
			
			
			
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
				
				stats.healthPacks += 1;
				
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
