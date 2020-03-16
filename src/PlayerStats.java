
public class PlayerStats extends Character{

	Integer Health = 100;
	Integer maxHealth = 100;
	
	public Integer ActiveWeaponID = 0;
	Integer ActiveMaskID = 0;
	
	private Weapon wpn;
	
	boolean isAlive = true;
	
	public PlayerStats(Integer health, Integer activeWeaponID, Integer activeMaskID) {
		super();
		Health = health;
		ActiveWeaponID = activeWeaponID;
		ActiveMaskID = activeMaskID;
		
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
	
	
	
	public Integer getActiveWeaponID() {
		return ActiveWeaponID;
	}



	public void setActiveWeaponID(Integer activeWeaponID) {
		ActiveWeaponID = activeWeaponID;
	}



	public Integer getActiveMaskID() {
		return ActiveMaskID;
	}



	public void setActiveMaskID(Integer activeMaskID) {
		ActiveMaskID = activeMaskID;
	}



	public PlayerStats() {
		super();
	}
	
}
