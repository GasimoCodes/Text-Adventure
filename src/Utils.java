import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Utilities commonly used by me by default.
 * 
 * @author Prokop Svaèina
 */
public class Utils {

	// []
	
	/** 
	Prints an object into console in line.
	@param msg - Object convertible to string which will be printed.
	*/
	public static void print(Object msg)
	{
		System.out.println(msg.toString());
	}
	
	/** 
	Prints an object into console in column.
	@param msg - Object convertible to string which will be printed.
	*/
	public static void printCol(Object msg)
	{
		System.out.print(msg.toString());
	}
	
	
	/** 
	Serializes input into array by divider.
	@param 	Input - Object convertible to string which will be divided.
	@param	Divider - Object convertible to string posing as the divider.
	@return Array of strings.
	*/
	public static String[] inputSerialize(Object Input, String Divider)
	{
		try {
		String[] TempCheck = Input.toString().split(Divider);
		return TempCheck;
		} catch(Exception e) {
			print(e.toString());
			return null;
		}
	}
	
	/** 
	Returns constant divider.
	@return Divider string.
	*/
	public static String div()
	{
		return ("- - - - - - - - - - - - - - - - -");
	}
	
	
	/** 
	Class for processing some quicc maths.
	*/
	static class Math {

	public static Double findMin(List<Double> list) 
    { 
  
        // check list is empty or not 
        if (list == null || list.size() == 0) { 
            return Double.MAX_VALUE; 
        } 
  
        List<Double> sortedlist = new ArrayList<>(list); 
  
        // sort
        Collections.sort(sortedlist); 
  
        return sortedlist.get(0); 
    } 
  
    public static Double findMax(List<Double> list) 
    { 
        // check list is empty or not 
        if (list == null || list.size() == 0) { 
            return Double.MIN_VALUE; 
        } 
        
        List<Double> sortedlist = new ArrayList<>(list); 
        Collections.sort(sortedlist); 
        print(sortedlist.size());
        return sortedlist.get(sortedlist.size() - 1); 

    } 
  
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
	
    
	}
}

	class UtilsException extends Exception
	{
	  public UtilsException(String message)
	  {
	    super(message);
	  }
	}
