import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.google.gson.Gson;

/**
 * Manager used to work with items and their serialization.
 * 
 * @author Prokop Svaèina
 */
public class ItemManager {
	
	Weapon[] Weapons;
	Item[] Items;
	int Heals;
	
	Gson gson = new Gson();
	Window End;
	
	public void Init()
	{
		
		
		
		String TempString = "";
		Scanner sc;
		File file = new File(System.getProperty("user.dir")+"/"+"ITEMS.nodes");

		//READ THE ITEMS
				try {
					sc = new Scanner(file, "UTF-8");
					sc.useDelimiter("\\Z"); 
					TempString = sc.next();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Items = null;
				Items = gson.fromJson(TempString, Item[].class);
				
				if (Items == null) {
					Utils.print("An Error occured while reading: " + file + ", which cannot be correctly parsed.\n The operation will now retry.");
					Init();
				}

				
				//READ THE WEAPONS
				
				
				file = new File(System.getProperty("user.dir")+"/"+"WEAPONS.nodes");
				
				try {
					sc = new Scanner(file, "UTF-8");
					sc.useDelimiter("\\Z"); 
					TempString = sc.next();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Weapons = null;
				Weapons = gson.fromJson(TempString, Weapon[].class);
				
				if (Weapons == null) {
					Utils.print("An Error occured while reading: " + file + ", which cannot be correctly parsed.\n The operation will now retry.");
					Init();
				}
		
				
	}
	
	
	
	/** 
	Gets Item Object by ID
	@param ID - ID of the desired Item.
	*/
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
	
	/** 
	Gets Item Object by ID
	@param ID - ID of the desired Item.
	*/
	public Weapon getWeapon(String ID)
	{
		if(Weapons == null)
			Utils.print("WEAPONS LOADING ERROR");
		
		boolean exists = false;
		Weapon it = null;
		for(Weapon i : Weapons)
		{
			if(i.id.compareTo(ID) == 0) {
				exists = true;
				it = i;
			}
		}
		if(!exists)
		{
			Utils.print("WEAPON WITH ID: " + ID + " HAS NOT BEEN FOUND IN THE ARRAY.");
			return null;
		} else
		return it;
		
	}
	
}
