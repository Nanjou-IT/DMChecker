package fr.upem.dmchecker;

public class Criteria {
	
	private final int number;
	private final String title;
	private final String description;
	private final  int barem;
	
	
	public Criteria(int number, String title, String description, int barem) {

		this.number = number;
		this.title = title;
		this.description = description;
		this.barem = barem;
	}


	public int getNumber() {
		return number;
	}


	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public int getBarem() {
		return barem;
	}
	

	
	
	
}
