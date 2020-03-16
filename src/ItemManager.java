
public class ItemManager {
	
	Weapon[] Weapons;
	Item[] Items;
	int Heals;
	
	
	
	public void Init()
	{
		
	}
	
	public Item getItem(String ID)
	{
		boolean exists = false;
		Item it = null;
		for(Item i : Items)
		{
			if(i.id.compareTo(ID) == 0) {
				exists = true;
				it = i;
			}
		}
		if(!exists)
		{
			Utils.print("ITEM WITH ID: " + ID + " HAS NOT BEEN FOUND IN THE ARRAY.");
			return null;
		} else
		return it;
		
	}
	
}
