package fr.upem.dmchecker;

import java.nio.file.Path;

public class Data {
	private final int number;
	private final String directory;
	private final String nomPrenoms;
	private Path executablePath;
	
	
	
	public Data(int number, String directory, String nomPrenoms) {

		this.number = number;
		this.directory = directory;
		this.nomPrenoms = nomPrenoms;
			
	}


	public int getNumber() {
		return number;
	}

	public String getDirectory() {
		return directory;
	}

	public String getNomPrenoms() {
		return nomPrenoms;
	}


	public Path getExecutablePath() {
		return executablePath;
	}


	public void setExecutablePath(Path executablePath) {
		this.executablePath = executablePath;
	}
	
	
	
	
	

}
