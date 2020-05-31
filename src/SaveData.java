import java.util.ArrayList;
import java.util.List;

/**
 * Contains necessary data for Save Data
 * 
 * @author Prokop Svaèina
 */
public class SaveData {
	
	// META
	
	
	// REST
	

	public List<SaveToken> savedValues = new ArrayList();
	public String lastNodeID = "0"; 
	public Boolean isBattle = false;
	
	Integer Health = 100;
	Integer maxHealth = 200;

	public Integer Defense = 0;
	
	public Weapon activeWeapon = new Weapon("Generic Test", "GEN_01", false, 1, "GENERIC", 10);
	
	public int healthPacks = 1;
	
	boolean isAlive = true;
	
	
	/**
     * Adds a value to be saved under an id, duplicate IDs will get rewritten.
     * @param id - ID to be saved under
     * @param value - Value to be saved
     */
	public void addSaveToken(String id, String value) {
		
		int i = 0;
		// Check for duplicates
		for(SaveToken x : savedValues)
		{
			if(x.saveID == id)
			{
				Utils.print("[SAVE] WARNING, CONDITION WITH ID " + x.saveID + " ALREADY EXISTS AND WILL BE OVERWRITTEN");
				savedValues.remove(i);
			}
			
			i++;
		}
		
		
		savedValues.add(new SaveToken(value, id));
	}
	
	
	/**
     * Removes a token under an id.
     * @param id - ID to be removed
     */
	public void removeToken(String id) {
		
		int i = 0;
		// Loop for desired id
		for(SaveToken x : savedValues)
		{
			if(x.saveID == id)
			{
				Utils.print("[SAVE] Token with ID " + x.saveID + " has been terminated.");
				savedValues.remove(i);
			}
			i++;
		}

	}


	public SaveData(List<SaveToken> savedValues, String lastNodeID, Integer health, Integer maxHealth, Integer defense,
		
		Weapon activeWeapon, int healthPacks, boolean isAlive) {
		super();
		this.savedValues = savedValues;
		this.lastNodeID = lastNodeID;
		Health = health;
		this.maxHealth = maxHealth;
		Defense = defense;
		this.activeWeapon = activeWeapon;
		this.healthPacks = healthPacks;
		this.isAlive = isAlive;
	}


	public SaveData() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}