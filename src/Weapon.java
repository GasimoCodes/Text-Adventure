
public class Weapon extends Item{
	
	Integer damage = null;
	
	public Weapon(String ItemName, String ItemID, boolean ItemCanStack, int ItemCount, String ItemDescription, int ItemDamage)
	{
		
		super.name = ItemName;
		super.id = ItemID;
		super.description = ItemDescription;
		this.damage = ItemDamage;
		
	}
	
    
}
