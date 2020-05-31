/**
 * Contains necessary data for Settings
 * 
 * @author Prokop Svaèina
 */
public class SettingsData {

	static double version = 1.7;
	
	public enum vTypes {RELEASE, RELEASECANDIDATE, BETA, ALPHA, DEBUG}
	
	public vTypes releaseType = vTypes.ALPHA;
	
	
	public String getTypeLetter()	
	{
		switch(releaseType)
		{
			case ALPHA:
				return "a";
			case BETA:
				return "b";
			case RELEASECANDIDATE:
				return "rc";
			case RELEASE:
				return "";
			case DEBUG:
				return "d";
			default:
				return "";
		}
	}
	
	public String getVersionText()
	{
		return version + getTypeLetter();
	}
	

}
