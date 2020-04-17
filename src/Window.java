import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.google.gson.*;


public class Window {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	
	// - - - -
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
	// - - - - 
	
	
	
	public static void main(String[] args) {
		
		try {
			Window window = new Window();
			Init();
			window.open();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void Init()
	{
		
		stats.Init();
		File file = new File(System.getProperty("user.dir")+"/"+"DIALOG.nodes");
		
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
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setMinimumSize(new Point(430, 360));
		shell.setSize(581, 590);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblWreee = new Label(sashForm, SWT.NONE);
		lblWreee.setText("WREEE");
		sashForm.setWeights(new int[] {1});
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite_2 = new Composite(scrolledComposite, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		
		StyledText styledText = new StyledText(composite_2, SWT.BORDER);
		styledText.setText("gvksuzhasihgedg\r\ndt\r\nhtf\r\njhrtfj\r\ndtzkjtfuk\r\nzuk\r\n\r\ndj\r\ntf\r\njdjfgjsfgjg\r\nfgjh\r\nfgj\r\nfgjsfjsjhsgjsfjf");
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setContent(composite_2);
		scrolledComposite.setMinSize(composite_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_1.setLayout(new GridLayout(2, false));
		
		Combo combo = new Combo(composite_1, SWT.NONE);
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 284;
		combo.setLayoutData(gd_combo);
		combo.setBounds(0, 0, 91, 23);
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setText("New Button");

	}
	
	public void WriteHelpDialog()
	{	
		Utils.print("Napište èíslo aby jste zvolili výše zobrazenou možnost.");
		//Utils.print("/inv \t pro zobrazení inventáøe");
	}
	
	

}
