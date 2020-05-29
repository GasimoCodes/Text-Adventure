/**
 * Contains necessary data for Save Data
 * 
 * @author Prokop Svaèina
 */
public class SaveData {
	
	// META
	
	
	// REST
	

	public SaveValue[] savedValues = new SaveValue[1];
	public String lastNodeID = "0"; 
	public Boolean isBattle = false;
	
	Integer Health = 100;
	Integer maxHealth = 200;

	public Integer Defense = 0;
	
	public Weapon activeWeapon = new Weapon("Generic Test", "GEN_01", false, 1, "GENERIC", 10);
	
	public int healthPacks = 1;
	
	boolean isAlive = true;
	
	


	public SaveData(SaveValue[] savedValues, String lastNodeID, Integer health, Integer maxHealth, Integer defense,
		
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