/**
 * Contains necessary data for Settings
 * 
 * @author Prokop Svaèina
 */
public class SettingsData {

	public double version = 1.5;
	
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
				return "a";
			case RELEASE:
				return "a";
			case DEBUG:
				return "a";
			default:
				return "u";
		}
	}
	
	public String getVersionText()
	{
		return version + getTypeLetter();
	}
	

}
