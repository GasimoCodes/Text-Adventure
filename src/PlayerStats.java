/**
 * Contains Player Statistics and tools to operate with them. 
 * 
 * @author Prokop Svaèina
 */
public class PlayerStats{

	Integer Health = 100;
	Integer maxHealth = 100;

	public Integer Defense = 50;
	
	public Weapon activeWeapon;
	
	
	public int healthPacks = 1;
	
	
	public ItemManager Mgr = new ItemManager();
	
	
	
	
	
	boolean isAlive = true;
	
	public void Init()
	{
		Mgr.Init();
	}
	
	public PlayerStats(Integer health, String activeWeaponID, String activeMaskID) {
		super();
		Mgr.Init();
		Health = health;
		
		activeWeapon = Mgr.getWeapon(activeWeaponID);
		//ActiveMaskID = activeMaskID;
		
		//GET WEAPON BY ID BY ITEMMANAGER
	}
	
	
	public void heal (int Amount)
	{
		if(Amount < 0)
		{
			Amount = -Amount;
		}
		
		Health += Amount;
		if(Health > maxHealth)
			Health = maxHealth;
	}
	
	public void useHealthPack (int Amount)
	{
		if(Amount < 0)
		{
			Amount = -Amount;
		}
		
		healthPacks -= 1;
		
		Health += Amount;
		if(Health > maxHealth)
			Health = maxHealth;
	}
	
	public void damage (Double Amount)
	{
		if(Amount < 0)
		{
			Amount = -Amount;
		}
		
		Health -= Amount.intValue();
		if(Health <= 0)
		{
			isAlive = false;
		}
	}
	
	
	public void damage (int Amount)
	{
		if(Amount < 0)
		{
			Amount = -Amount;
		}
		
		Health -= Amount;
		if(Health <= 0)
		{
			isAlive = false;
		}
	}
	
	public void setHealth (int Amount)
	{
		if(Amount > maxHealth)
		{
			Amount = maxHealth;
		}
		
		Health = Amount;
		
		if(Health <= 0)
		{
			isAlive = false;
		}
	}
	
	
	
	public String getActiveWeaponID() {
		return activeWeapon.id;
	}



	public void setActiveWeaponID(String activeWeaponID) {
		activeWeapon = Mgr.getWeapon(activeWeaponID);
	}


/*
	public String getActiveMaskID() {
		return activeMaskID;
	}

*/
/*
	public void setActiveMaskID(String activeMaskID) {
		ActiveMaskID = activeMaskID;
	}
*/


	public PlayerStats() {
		super();
	}
	
}
