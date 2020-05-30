/**
 * Contains necessary data to organize Stories.
 * 
 * @author Prokop Svaèina
 */
public class StoryInfo {

	String name;
	String author;
	String description;
	
	String id;
	
	Double storyVersion = 1.0;
	Double appVersion = 1.6;
	
	
	public StoryInfo(String name, String author, String description, String id, Double storyVersion, Double appVersion) {
		super();
		this.name = name;
		this.author = author;
		this.description = description;
		this.id = id;
		this.storyVersion = storyVersion;
		this.appVersion = appVersion;
	}

	
	
	
}
