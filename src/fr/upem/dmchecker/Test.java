package fr.upem.dmchecker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Test {

	/*
	 * IL FAUT LIBERER LES CLASSES
	 * 
	 * Regarder si ca marche quand l'étudiant a des classes de meme nom que nous
	 * 
	 * 
	 * voir pour mettre la partie test dans la fonction boucle
	 * regarder tous les throw exceptions
	 */

	public static void main(String[] args) {
		Test main = new Test();
		//main.test(null);
		main.launchOfTests("C:/temporaire2/","./resultat","C:/temporaire/hash/");
	}
	

	public void launchOfTests(String mainDirectory, String resultFile, String testDirectory){

		File mainDirectoryFile = new File(mainDirectory);

		if(!mainDirectoryFile.isDirectory()){
			//TODO
			System.out.println("ce n'est pas un directory");
		}
		

		try(BufferedWriter buffer= new BufferedWriter(new FileWriter(resultFile))){

			buffer.append("<dmchecker>\n");		

			for(File projectDirectory : mainDirectoryFile.listFiles()){
				if(projectDirectory.isDirectory()){
					String nomPrenoms = projectDirectory.getName().replaceAll("_", " ");
					buffer.append("<soft name=\""+projectDirectory.getName()+"\" student=\""+nomPrenoms+"\">\n");
					launchOfTest(buffer,projectDirectory.getPath(),testDirectory);
					buffer.append("</soft>\n");
				}
			}			
			
			buffer.append("</dmchecker>\n");
			buffer.flush();			

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	public void launchOfTest(BufferedWriter buffer, String projectDirectory, String testDirectory) throws IOException, ClassNotFoundException {


		//transformer file en path

		List<File> srcList = new LinkedList<>();
		LinkedList<File> testList = new LinkedList<>();
		File projectDirectoryFile = new File(projectDirectory);
		File src = null;
		File test = new File(testDirectory);

		if(!test.isDirectory()){
			// TODO error
			System.out.println("pas un directory");
		}


		//Fin  the directory src
		for(File aFile : projectDirectoryFile.listFiles()){
			if(aFile.getName().equals("src") && aFile.isDirectory()){
				src = aFile;
			}				
		}
		if(src == null){
			// TODO error
			System.out.println("Le répertoire source du projet n'a pas été trouvé");
			System.exit(0);
		}

		// TODO May it have a good solution ?
		findFiles(src,srcList,".java");


		JUnitCore runner = new JUnitCore();
		runner.addListener(new TextListener(System.out));


		// Le dossier des test sert de référence
		// Les packages des fichiers de tests devront exister la dedans
		URL urlTest = test.toURI().toURL();
		URL urlSrc = src.toURI().toURL();

		URL[] urls = new URL[]{urlTest,urlSrc};
		ClassLoader cl = new URLClassLoader(urls);

		//LinkedList<Class> classList = new LinkedList<>();


		// Compile the files of the source project
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if(compiler==null){
			System.out.println("The function ToolProvider.getSystemJavaCompiler() return null");
			System.out.println("You need to use a JDK and not a JRE to run this project");
			// TODO ERROR
		}
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(srcList); 
		compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();
		fileManager.close();



		// Test part
		findFiles(test,testList,null);			
		for(File aTest : testList){
			
			String relativize =test.toPath().relativize(aTest.toPath()).toString();
			relativize=relativize.replace('\\','.');

			int index = relativize.lastIndexOf(".class");
			if(index<0){
				continue;
			}
			relativize = relativize.substring(0, index );
			System.out.println("Relativize : "+relativize);
			Class cls = cl.loadClass(relativize);

			Result result = runner.run(cls); 
			boolean wasSuccessful = result.wasSuccessful();
			buffer.append("<test name="+aTest.getName()+" result="+wasSuccessful +">\n");
			for(Failure failure :result.getFailures()){
				buffer.append(failure.toString());
			}
			buffer.append("</test>\n");

		}		
	
	}

	//Add all files found in the list
	public void findFiles(File file, List<File> aList, String filter){

		if(file.isDirectory()){
			for(File aFile : file.listFiles()){
				findFiles(aFile,aList, filter);
			}
		}
		else{
			if(filter == null || file.getName().contains(filter)){
				aList.add(file);
			}
		}
	}
	
}
