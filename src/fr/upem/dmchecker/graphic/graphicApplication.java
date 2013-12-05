package fr.upem.dmchecker.graphic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import fr.upem.dmchecker.Criteria;
import fr.upem.dmchecker.Data;




public class graphicApplication {



	public static ArrayList<Data> read(String mainDirectory, String dataFile, String criteriaFile, String executable){

		ArrayList<Data> dataList  = new ArrayList<>();
		try(BufferedReader dataBuffer= new BufferedReader(new FileReader(dataFile));
				BufferedReader criteriaBuffer= new BufferedReader(new FileReader(criteriaFile))){

			LinkedList<Criteria> criteriaList = new LinkedList<>();
			
			File mainDirectoryFile = new File(mainDirectory);


			// Read the criteria file
			criteriaBuffer.readLine(); // drop the first line
			String line;
			while((line = criteriaBuffer.readLine()) != null){
				String args[] =line.split(":");
				if(args.length!=4){
					continue;
				}
				Criteria criteria = new Criteria(Integer.parseInt(args[0]),args[1],args[2],Integer.parseInt(args[3]));
				criteriaList.add(criteria);				
			}

			// Read the data file
			dataBuffer.readLine(); // drop the first line			
			while((line = dataBuffer.readLine()) != null){
				String args[] =line.split(":");
				if(args.length!=3){
					continue;
				}
				Data data = new Data(Integer.parseInt(args[0]), args[1],args[2]);				
				dataList.add(data);				
			}


			//Search the executable new
			for(Data data : dataList){
				File directory = new File(data.getDirectory());
				if(!directory.isDirectory()){
					continue;
				}
				for(File file : directory.listFiles()){
					if(file.getName().equals(executable)){
						data.setExecutablePath(file.toPath()); // Stock the path of the executable in the class
					}
				}				
			}

			// Search the executable
			/*for(Data data : dataList){
				for(File projectDirectory : mainDirectoryFile.listFiles()){
					if(projectDirectory.isDirectory()){					
						// do we need that ?
						String nomPrenoms = projectDirectory.getName().replaceAll("_", " ");
						// It's the good team
						if(nomPrenoms.equals(data.getNomPrenoms())){
							for(File file : projectDirectory.listFiles()){
								 if(file.getName().equals(executable)){ 
									 data.setExecutablePath(file.toPath()); // Stock the path of the executable in the class
								 }
							}
						}
					}
				}	
			}*/


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return dataList;
	}


	public static void main(String[] args) {

		String mainDirectory ="C:/temporaire2/";
		String dataFile = "C:/temporaire/dataFile.txt";
		String criteriaFile = "C:/temporaire/criteriaFile.txt";
		String executable = "Minecraft.exe";
		;
		
		Window window = new Window(read(mainDirectory, dataFile, criteriaFile, executable));
		window.setVisible(true);

	}



}
