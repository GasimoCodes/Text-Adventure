import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.google.gson.Gson;


/**
 * Contains the main system loop for the Story Engine reading and playing.
 * 
 * @author Prokop Svaèina
 */
public class Window implements Runnable {
	
	
	// - - - Static References
	Gson gson = new Gson();
	public PlayerStats stats = new PlayerStats();
	Scanner sc;
	public ItemManager Mgr = new ItemManager();
	public NodeManager NMgr = new NodeManager();
	
	File Save = new File(System.getProperty("user.dir")+"/Story"+"/"+"USERDATA.sav");
	
	// - - - Temporary Primitives
	boolean end = false;
	String TempString;
	String nodeID = "0";
	boolean Await = false;
	boolean windowReady = false;
	boolean awaitGUI = false;
	Integer ID = 0;
	boolean isBattleTime = false;
	long waitTime = 200; //ms?
	
	ArrayList<DialogOption> tempOptions = new ArrayList<DialogOption>();
	
	// - - - Story Data Containers
	public DialogNode[] D_Nodes;
	public BattleNode[] B_Nodes;
	
	// - - - Other Data Containers
	public SettingsData settings = new SettingsData();
	public SaveData S_Data = new SaveData();
	
	// - - - GUI Containers
	Composite composite;
	SashForm sashForm;
	ScrolledComposite scrolledComposite;
	Composite composite_2;
	StyledText styledText;
	Composite composite_1;
	Combo combo;
	Button btnNewButton;
	
	protected Shell shell;
	
	
	/**
     * Launches the Engine Loop Threads in an instance.
     * 
     */
	public void run()
	{
		SwingUtilities.invokeLater(new Runnable(){
		    public void run(){
		        Init();
		    }
		});
		open();	
	}
	

	/**
     * Loads all files into objects. On finish checks if GUI Thread is loaded.
     * 
     */
	public void Init()
	{
		stats.Init();
		File file = new File(System.getProperty("user.dir")+"/Story"+"/"+"DIALOG.nodes");
		
		Mgr.End = this;
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
			print("An Error occured while reading: " + file + ", which cannot be correctly parsed.\n The operation will now retry.");
			Init();
		}
		
		stats.activeWeapon = Mgr.getWeapon("0");
		
		//READ BATTLE DATABASE
		File file2 = new File(System.getProperty("user.dir")+"/Story"+"/"+"BATTLE.nodes");
		
				try {
					Scanner sce = new Scanner(file2, "UTF-8");
					sce.useDelimiter("\\Z");
					TempString = sce.next();
					sce.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					print(e);
				}
				
				B_Nodes = null;
				B_Nodes = gson.fromJson(TempString, BattleNode[].class);
				
				if (B_Nodes == null) {
					print("An Error occured while reading: " + file2 + ", which cannot be correctly parsed.\n The operation will now retry.");
					Init();
				}
				
				

				
				
		//Read Save Files if present
		if(Save.exists())
		{
			try {
					Scanner sce = new Scanner(Save, "UTF-8");
					sce.useDelimiter("\\Z");
					TempString = sce.next();
					sce.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					print(e);
				}
				
				S_Data = null;
				S_Data = gson.fromJson(TempString, SaveData.class);
				LoadFromSave();
				
				
				if (S_Data == null) {
					print("An Error occured while reading: " + Save + ", which cannot be correctly parsed.");
					List<String> opt = new ArrayList(); 
					
					opt.add("Retry");
					opt.add("Create new save file");
					opt.add("Exit");
					
					input2id(opt);
					
					Init();
				}
				
		} else {
			
			Utils.print("SAVE FILE NOT FOUND, SKIPPING.");
			try {
				
				/*
				S_Data = new SaveData(new SaveValue[]{new SaveValue("Test", "True")}, stats, "");
				Utils.print(gson.toJson(S_Data));
				*/
				
				Save.createNewFile();
				Files.write(Save.toPath(), gson.toJson(S_Data).getBytes());
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
		while(windowReady)
		{
			// Wait for GUI Thread to catch up
		}
		
		buttonSetEnabled(false);
		print(Utils.div() + "\n" + "STORYNAME Loaded Successfuly");
		runStory();
		
		
	}
	
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		

		
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("TBSE | v " + settings.getVersionText());
		shell.setMinimumSize(new Point(430, 360));
		shell.setSize(581, 590);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		sashForm = new SashForm(composite, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblWreee = new Label(sashForm, SWT.NONE);
		lblWreee.setText("- TBSE v " + settings.getVersionText() + " -");
		sashForm.setWeights(new int[] {1});
		
		scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		composite_2 = new Composite(scrolledComposite, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		
		styledText = new StyledText(composite_2, SWT.BORDER | SWT.WRAP);
		styledText.setText("TEXT-BASED STORY ENGINE v " + settings.getVersionText());
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		styledText.setEditable(false);
		scrolledComposite.setContent(composite_2);
		scrolledComposite.setMinSize(composite_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_1.setLayout(new GridLayout(2, false));
		
		combo = new Combo(composite_1, SWT.NONE);
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 284;
		combo.setLayoutData(gd_combo);
		combo.setBounds(0, 0, 91, 23);
		
		btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setText("Confirm");
		
		// When finished loading, inform the other thread.
		windowReady = true;
		
		
        // Handling when users click the button.
		btnNewButton.addSelectionListener(new SelectionListener( ) {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Utils.print("- Input - " + combo.getText());
				awaitGUI = false;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
			
			
		});
		
		
	}

	/**
     * Sets the input button to either be enabled or disabled in order to prevent input.
     * @param enabled - Keep true to enable, or false to disable button input
     */
	public void buttonSetEnabled(boolean enabled) {
	Display.getDefault().asyncExec(new Runnable() {
        public void run() {
        	Utils.print("Buttonio Refreshio" + enabled);
        	btnNewButton.setEnabled(enabled);
        	btnNewButton.redraw();
        	btnNewButton.update();
        }
     });
	}
	
	
	/**
     * Prints a text into the GUI Text panel
     * @param obj - Object to be printed, convertible to string.
     */
	public void print(Object obj) {
	Display.getDefault().asyncExec(new Runnable() {
        public void run() {
        	styledText.setText(styledText.getText() + "\n" + obj);
        	updateScroll();

        }
     });
	}
	
	
	/**
     * Updates GUI Panels to adjust the Scroll Values to the text
     *
     */
	public void updateScroll() {
	Display.getDefault().asyncExec(new Runnable() {
        public void run() {

        	styledText.layout();
        	composite_2.update();
        	composite_2.layout();
        	scrolledComposite.update();
        	// Utils.print("UPDATED");
        	shell.layout();
        	scrolledComposite.layout();
        	scrolledComposite.setMinSize(composite_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        	scrolledComposite.setOrigin(0, 999999999);	// I don't want to know why
        												// I shouldn't need to wonder why, but for whatever reason this hack works the best around here.
        												// Anything else will break the limit anyway, this is the easiest and most reliable way to do this. 
        												// I hope this implementation is so bad that they wont let me ever write Java SWT Code again.
        	
        												// 17.05 23:00 | This is probably a hack I feel the worst for... I know there will be that one time someone is gonna break the limit... It can't work... yet it does...
        }
     });
	}
	
	
	
	/**
     * Prints a text into the GUI Text panel inLine
     * @param obj - Object to be printed, convertible to string.
     */
	public void printCol(Object obj) {
	Display.getDefault().asyncExec(new Runnable() {
        public void run() {
        	styledText.setText(styledText.getText() + obj);
        }
     });
	}
	
	/**
     * Adds Option to GUI Story Input
     * @param obj - Object to be printed, convertible to string.
     */
	public void printOption(Object obj) {
	Display.getDefault().asyncExec(new Runnable() {
        public void run() {
        	combo.add((String) obj, 0);        	
        	
        }
     });
	}
	
	
	/**
     * Sets the input button to either be enabled or disabled to prevent input while loading.
     * @param obj - Option to be printed
     * @param index - Index of the option to be printed
     */
	public void printOption(Object obj, int index) {
	Display.getDefault().asyncExec(new Runnable() {
        public void run() {
        	combo.add(obj.toString(), index);
        	combo.select(0);
        }
     });
	}
	
	/**
     * Resets GUI Dialog 
     */
	public void resetOptions() {
	Display.getDefault().asyncExec(new Runnable() {
        public void run() {
        	combo.clearSelection();
        	combo.removeAll();
        }
     });
	}
	
	
	/**
     * Returns string in the Input Field Combo
     * @return String of the GUI field.
     */
	public String getComboText() {
		String Temp = TempString;
		Display.getDefault().asyncExec(new Runnable() {
			
	        public void run() {
	        	TempString = combo.getText();
	        }
	     });
		
		return TempString;
	}
	
	/**
     * Serializes Input into ID, then after input retrieves chosen option
     * @return String of the ID.
     */
	public String input2id(List<String> options) {
		int i = 0;
		// We list all options to input
		for (String s : options) 
		{ 
		    i++;
		    print("-> \t" + s + "\t[" + i + "]");
		    printOption(s, i-1);
		    
		    try {			    	
				Thread.sleep(500);
			
		    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		print(Utils.div());
		
		boolean waitInput = true;
		awaitGUI = true;
		String Input = null;

		
		while(waitInput) {
			
			// Standby for Input
			
			print("-> \t");
			
			// Await for GUI Thread to confirm
			
			buttonSetEnabled(true);
			
			// Utils.print(Thread.getAllStackTraces());
			
			boolean tempInputLegit = true;
			
			while(awaitGUI)
			{		
				
				// Check if input legit?
				
				if(getComboText().compareTo("") == 0 && tempInputLegit)
				{	
						// Utils.print("false");
						buttonSetEnabled(false);
						tempInputLegit = false;
				} else if (getComboText().compareTo("") != 0 && !tempInputLegit) {
						
						// Utils.print("true");
						buttonSetEnabled(true);
						tempInputLegit = true;	
				}
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			
			awaitGUI = true;
			
			buttonSetEnabled(false);
			
			
			Input = getComboText();
			
			printCol(Input);
			
			resetOptions();
			
			// IF HELP
			if(Input.toLowerCase().compareTo("/help") == 0)
			{
				WriteHelpDialog();
				print(Utils.div());
				
			// Write Options
				i = 0;
				for (String s : options) 
				{ 
				    i++;
				    print("-> \t" + s + "\t[" + i + "]");
				    
				    try {			    	
						Thread.sleep(500);
				    } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			// ELSE	
			} else {
				
				// We need to detect whether we are using a number or string to describe chosen value
				
				if(Input.length() <= 1) { // Check for char length below one, this is probably a number
				
					try {
						if(Integer.parseInt(Input) <= options.size() && Integer.parseInt(Input) > 0)
						{
							waitInput = false;
							return Input;

						} else {
							
						print("Èíslo " + Input + " není ani jedna z možností");
						WriteHelpDialog();
						
						}
					} catch(Exception e) {
						print("Napište /help pro pomoc pøi zadávání.");
					}
					
			} else {

				// Input detected to A: Not be a valid number, or B: An actual input string.
				
				i = 0;
				//TODO Check this and compare to the IDs above, we need consistency
				for (String s : options) 
				{ 
				    i++;
				    if(s.compareTo(Input) == 0)
				    {
				    	return Integer.toString(i);
				    }
				}
				
			}

			
		}
			
		}
		
		if (Input == null)
			Utils.print("Bruh");
		
		return Input;
	
		
		
		
		
	}
	
	
	// Mirrored from FrontEnd, Contains story-reading and input logic
	
	/**
     * Initializes the Story loop.
     * 
     */
	public void runStory()
	{
		
		isBattleTime = false;
		sc = new Scanner(System.in);
		end = false;
		ID = 0;
		

		print(Utils.div());
		print("Vždy mùžete napsat /help pro zobrazení pøíkazù");
		print(Utils.div());
		
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
			
		
		
		// Execute Commands		
		
		if(D_Nodes[ID].Args != null)
		{
			Utils.print("ARGS DETECTED");
			SpecialCommand(D_Nodes[ID].Args);
		}
		
			
	// Write Node Start
			
			for (String s : D_Nodes[ID].WriteText) 
			{ 
			    print(s);
			    try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	
	// Check if the game has not ended already, if so we dont want any input from the nodes to be displayed.
			if(end != true)
			{
				
	// Write Options - MOVED TO input2id logic loop
			int i = 0;
			List<String> Options = new ArrayList<String>();
			for (DialogOption s : D_Nodes[ID].Options) 
			{ 
			    i++;
			    
			    
			    boolean Pass = true;
			    Integer ConPass = 0;
			    
			    if(s.conditions != null)	{
				    for(String str : s.conditions) {
				    	
				    	if(GetCondition(str)) {
				    	ConPass ++;
				    	}
				    }
				    
				    if(ConPass == s.conditions.length) {
				    
				    	Pass = true;
				    	Utils.print("[DEBUG]\tALL CONDITIONS TRUE");
				    } else  {
				    	Utils.print("[DEBUG]\tSOME CONDITIONS FALSE");
				    	
				    	Pass = false;
				    	
				    }
			    
			    }
			    
			    if(Pass) {
			    	
			    Options.add(s.write);
			    
			    }


			}
			
			
			print(Utils.div());

			
			boolean waitInput = true;
			String Input = "";
			
	//INPUT PHASE
			
			Input = input2id(Options);
			
			// END OF LOOP
			
			// SELECT FROM ARRAY
			
			
			
			//print(Input + " I2 " + " ID THIS: " + ID );
			String nextNodeID;
			
			//Select ID
			nextNodeID = D_Nodes[ID].Options[Integer.parseInt(Input) - 1].gotoID;
			
			//Pront
			if(D_Nodes[ID].Options[Integer.parseInt(Input) - 1].commands != null)
			SpecialCommand(D_Nodes[ID].Options[Integer.parseInt(Input) - 1].commands);
			
			//Check if its battle type ID
			isBattleTime = D_Nodes[ID].Options[Integer.parseInt(Input) - 1].isBattle;
			
			
			nodeID = nextNodeID;	
			//print(nodeID);
			
			
			SaveCurrent();
			
			
			
		//End of check if game already ended.	
		}
			
			print(Utils.div());
			
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
			print("AN CRITICAL ERROR HAS OCCURED WHILE READING STORY, TARGET NODE APPEARS TO BE MISSING.");
		}
		
		ID = ez;
					
		Await = true;
		
		
		SaveCurrent();
		

		Battle(B_Nodes[ID]);
		
		while(Await)
		{
		//SLEEEEEEEEEEEEEEEEEP?.	
		}
		
		isBattleTime = false;
	
	}
			
		}
			
		
	}
	
	/**
     * Shortcut for help dialog. (will later move to be read from a file?).
     * 
     */
	public void WriteHelpDialog()
	{	
		print("Napište èíslo aby jste zvolili výše zobrazenou možnost.");
		//print("/inv \t pro zobrazení inventáøe");
	}
	
	
	/**
     * Load Saved Data
     * 
     */
	public void LoadFromSave()
	{	
		
		stats.Health = S_Data.Health;
		stats.activeWeapon = S_Data.activeWeapon;
		stats.Defense = S_Data.Defense;
		stats.maxHealth = S_Data.maxHealth;
		stats.healthPacks = S_Data.healthPacks;
		stats.isAlive = S_Data.isAlive;
		isBattleTime = S_Data.isBattle;
		
		nodeID = S_Data.lastNodeID;
		
		Utils.print("SAVEFILE LOADED.");
	}
	
	/**
     * Save Current Data
     * 
     */
	public void SaveCurrent()
	{	
		
		S_Data.Health = stats.Health;
		S_Data.activeWeapon = stats.activeWeapon;
		S_Data.Defense = stats.Defense;
		S_Data.maxHealth = stats.maxHealth;
		S_Data.healthPacks = stats.healthPacks;
		S_Data.isAlive = stats.isAlive;
		S_Data.isBattle = isBattleTime;
		
		S_Data.lastNodeID = nodeID;
		
		if(Save.exists())
		{
			
			try {
				
				Files.write(Save.toPath(), gson.toJson(S_Data).getBytes());
				
				
			} catch (Exception e) {
			
			
			}
			
		} else {
		
			Utils.print("[SAVE]\tCannot save because the save file is missing, the system will now create a new file.");
			
			try {
				Save.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
     * Story loop for battle sequences, when done switches back to main story - run()
     * @param node - Battle Node to be passed for the sequence event.
     */
	public void Battle(BattleNode node)
	{
		
		
		boolean IsBattling = true;
		
		//BATTLE LOGIC HERE
		while(IsBattling) //ADD IS ALIVE HERE :EYES:
		{
			
		List<String> Options = new ArrayList<String>();
		print(" - - - - - " + stats.healthPacks + " - - - - - ");
			
		print("->\t(Vaše životy: " + stats.Health+")" + "\t(" + node.name + " životy" + ": " + node.health + ")");
		
			// Ask what to do
		if(stats.activeWeapon.id.compareTo("0") != 0)
		{
			Options.add("Zaútoèit pomocí " + stats.activeWeapon.name);
		} else {
			Options.add("Zaútoèit");
		}
			Options.add("Bránit se");
			
		if(stats.healthPacks >= 1)
		{
			Options.add("Použít Lékárnièku. (" + stats.healthPacks + ")");
		}
		
		print(Utils.div());	
		
		
		
		
		// Get input
		boolean inpwait = true;
		int inp = 0;
		while(inpwait)	{
			try {
				
				printCol("-> \t");
					
				
				Integer Input = Integer.parseInt(input2id(Options));
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
						
						print("Nelze použít Lékárnièku, protože žádná nezbývá v inventáøi.");
						inpwait = true;
					
					} else {
						
						if(inp == 3)
						{
							print("->\tPoužili jste Lékárnièku.");
							stats.useHealthPack(Utils.Math.randInt(50, 100));	
							inpwait = true;
						}
						
					}
					
				}

					
				} else {
				
					print("Akce " + Input + " neexistuje. Zkuste to znovu nebo použijte /help.");
				}	
			
			} catch(Exception E) {
				print("Zadejte prosím èíslo pro zvolení akce nebo /help pro pomoc");
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
			
			print(DefenceFactor);
			
			print("->\tZaútoèil jste pomocí " + stats.activeWeapon.name);
			node.health -= Utils.Math.randInt(stats.activeWeapon.damage,stats.activeWeapon.damage + 10) * (1 - (node.Defense/100));
		}
		
		if(inp != 2 && node.health > 0)
			stats.damage(Utils.Math.randInt(node.Damage,node.Damage + 10) * DefenceFactor);
		
		if(inp == 2 && node.health > 0) {
			DefenceFactor = DefenceFactor /2 ;
			
			print(DefenceFactor);
			
			if(DefenceFactor > 1)
				DefenceFactor = 1;
			
			stats.damage(Utils.Math.randInt(node.Damage,node.Damage + 10) * DefenceFactor);
		}
		
		//print(node.Damage + "//" + stats.Defense);
		//print(stats.activeWeapon.damage + "//" + node.Defense);
		
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
	

	/**
     * Parses special commands called from the story, or possibly later by console.
     * @param Commands - Commands to process and execute.
     */
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
				print("Cannot execute setHealth: " + e.toString());
				}
			}
			
			
			//set damage
			if (Args[0].toLowerCase().compareTo("damage") == 0 && Args.length == 2)
			{
				try
				{
					stats.damage(Integer.parseInt(Args[1]));
				} catch (Exception e) {
				print("Cannot execute setHealth: " + e.toString());
				}
			}
			
			
			//set heal
			if (Args[0].toLowerCase().compareTo("heal") == 0 && Args.length == 2)
			{

				try
				{
					stats.heal(Integer.parseInt(Args[1]));
				} catch (Exception e) {
				print("Cannot execute setHealth: " + e.toString());
				}
			}
			
			//set Delay
			if (Args[0].toLowerCase().compareTo("setdelay") == 0 && Args.length == 2)
			{
				
				try
				{
					
					waitTime = Integer.parseInt(Args[1]);
				} catch (Exception e) {
				print("Cannot execute setDelay: " + e.toString());
				}
			}
			
			
			//set heal
			if (Args[0].toLowerCase().compareTo("end") == 0 && Args.length == 1)
			{
					end = true;
					print("Game ended.");
					stats = new PlayerStats();
			}
			
			//set token
			if (Args[0].toLowerCase().compareTo("settoken") == 0 && Args.length == 3)
			{
					S_Data.addSaveToken(Args[1],Args[2]);
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
			setDelay <amount>		// Sets the delay used in Dialog Listing in milliseconds.
			
			setToken <ID> <VALUE>	// Schedules save token.
			removeToken <ID>		// Schedules save token.			
			
			end						// Ends the game, indicates the last node in branch.
			
		*/
		
	}
	
	
	/**
     * Parses various Conditions
     * @param Condition - Condition to return
     * @return boolean - Condition
     */
	public boolean GetCondition(String Condition) {
				
			String[] Args = Utils.inputSerialize(Condition, " ");
			
			
			//load story saveID
			if (Args[0].toLowerCase().compareTo("gettoken") == 0 && Args.length == 2)
			{

					// Loop for desired id
					for(SaveToken x : S_Data.savedValues)
					{
						if(x.saveID == Args[1])
						{
							Utils.print("[SAVE] Token with ID " + x.saveID + " has been found.");
							return true;
						}
					}

			}
			
			//load token negation
			if (Args[0].toLowerCase().compareTo("getinvertedtoken") == 0 && Args.length == 2)
			{

					// Loop for desired id
					for(SaveToken x : S_Data.savedValues)
					{
						if(x.saveID == Args[1])
						{
							Utils.print("[SAVE] Token with ID " + x.saveID + " has been found.");
							return false;
						} else
						{
							return true;
						}
					}

			}
				
			
			
			return false;
			
			
		}
		
		
		//List of returns:
		
		/*
			getToken <ID> <VALUE>		// Schedules save token.			
			
		*/
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
