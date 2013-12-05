package fr.upem.dmchecker;

import java.io.File;

import static java.nio.file.StandardCopyOption.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import fr.upem.dmchecker.Options.MyColor;

public class Archive {
//	private final Logger logger;
	private EnumSet<MyColor> condition;
	private final boolean verboseMode;
	
	private final static int NO_ERROR = 0;
	private final static int FORCE_ERROR = 1;
	
	public Archive(EnumSet<MyColor> condition) {
		this.condition = condition;
//		this.logger = Objects.requireNonNull(logger); 
//		this.condition = EnumSet.noneOf(MyColor.class);
//
//		MyColor m = MyColor.FORCE_ENDS_WITH;
//		m.setValue(".class");
//		
////		MyColor m1 = MyColor.FORCE_ONE_TOP;
////		m1.setValue("top");
//		
//		MyColor m1 = MyColor.VERBOSE;
//		m1.setValue("top");
//		
//		MyColor m2 = MyColor.EXIST;
//		m2.setValue("hu");
//		
//		MyColor m3 = MyColor.DESTINATION;
//		m3.setValue("ProjetsEleves");
//		
//		condition.add(m);
//		condition.add(m1);
//		condition.add(m2);
//		condition.add(m3);
	
		if (condition.contains(MyColor.VERBOSE)) {
			verboseMode = true;
		} else {
			verboseMode = false;
		}
	}
	
	
	/**
	 * A task that provide a way to handle the extraction of a particular zip archive.
	 * This may throw an exception which is handled to inform the user.
	 */
	@FunctionalInterface
	interface ExtractionTask {
		void create(ZipInputStream zipinputstream, ZipEntry zipentry) throws ZipException, FileNotFoundException, SecurityException, NullPointerException, IOException;
	}
	
	/**
	 * Testing whether the archive could be openned as an archive.
	 * 
	 * @param file is the archive.
	 * @return {@code true} or {@code false} depending on openned status and format.
	 */
	private boolean isValidZipFormat(Path archiveName) {
		if (archiveName == null) {
			System.err.println("Error : The archive doesn't exist.");
			return false;
		}
		
		File archive = new File(archiveName.toUri());
		try (ZipFile zipfile = new ZipFile(archive)) {
			return true;
		} catch (ZipException e) {
			System.err.println("Error : An error has occurred on the provided zip file, the file "
					+ "may have a corrupted format.");
			return false;
		} catch (IOException e) {
			System.err.println("Error : An error has occurred on the provided zip file, the file "
					+ "may be corrupted cause it can't be openned.");
			return false;
		}
	}

	/**
	 * This method permit to test only one archive. <br>
	 * Returned value is a boolean indictating the accepted status. <br>
	 * Note : It justs test the validity, and it doesn't extract anything. <br>
	 * 
	 * @param archiveName name of the zip file
	 * @return {@code 0} if the archive is accepted.<br>
	 *         {@code 1} if a problem occured, a message will describe the
	 *         problem on System.err.
	 */
	public int validateArchive(String archiveName) {
		Path archivePath = Paths.get(archiveName);
		if (!isValidZipFormat(archivePath)) {
			System.err.println("Error : This kind of file cannot be validate.");
			return 1;
		}
		
		// TODO : Probably more stuff to do there.
		return extract1(archivePath);
	}

	/**
	 * This method permit to test one archive {@code archiveName} and to extract 
	 * 	it in specified {@code destinationName}. <br>
	 * Returned value is an int indictating the accepted status. <br>
	 * 
	 * @param archiveName name of the zip file
	 * @param destinationName name of the directory destination
	 * @return {@code 0} if the archive is accepted.<br>
	 *         {@code 1} if a problem occured, a message will describe the
	 *         problem on System.err.
	 */
	public int extractZipFile(String archiveName) {
		String destinationName = MyColor.DESTINATION.getValue()[0];
		Path archiveNamePath = Paths.get(archiveName);
		
		if (!isValidZipFormat(archiveNamePath) || destinationName == null) { 
			System.err.println("Cannot proceed to the extraction of: [" + archiveName + "] into "+ destinationName +" directory.");
			return 1;
		}
		
		// If we passed previous tests, <filename> :
		//   - is non-null
		//	 - has a good format
		//	 - can be openned
		return extract2(archiveNamePath, destinationName);
	}

	/**
	 * Does the extraction work, defined by a certain {@code action} which is an {@link ExtractionTask}.
	 * 
	 * @param filename
	 * @param destinationname
	 * @param action  functionnal interface {@link ExtractionTask} handling extraction specifities.
	 * @return {@code boolean} representing the acceptation status
	 */
	private int extract1(Path filenamePath) {
		HashSet<String> files = new HashSet<>();
		int errorState = NO_ERROR;
		
		if (verboseMode) {
			System.out.println("\nWorking on : " + filenamePath);
		}
		try (ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(filenamePath.toFile()), Charset.forName("Cp437")) ) {
			ZipEntry zipentry = zipinputstream.getNextEntry();
			
			StringBuilder projectName = new StringBuilder();
			int oneTopResult = oneTop(filenamePath.toString(), projectName);
			if (oneTopResult == 1) {
				System.out.println(Messages.getOutputString(MyColor.ONE_TOP.shortFlag+"", MyColor.ONE_TOP.getValue()[0]));
				errorState = FORCE_ERROR;
			} else if (oneTopResult == 2) {
				System.err.println(Messages.getOutputString(MyColor.FORCE_ONE_TOP.shortFlag+"", MyColor.FORCE_ONE_TOP.getValue()[0]));
				errorState = FORCE_ERROR;
			}
			
			while (zipentry != null) {
				// For each entry to be consulted
				String entryName = zipentry.getName();
				files.add(entryName);
				if (verboseMode) {
					System.out.println("	Entry : "+entryName);
				}
				// If returns a FORCE Error 
				if (checkFileProperties(entryName) == FORCE_ERROR) {
					errorState = FORCE_ERROR;
				}
				
				zipinputstream.closeEntry();
				zipentry = zipinputstream.getNextEntry();
			}
			
			if (exists(files) > 0) {
				errorState = FORCE_ERROR;
			}
			
			zipinputstream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error : The file was not found !");
			return 1;
		} catch (SecurityException se) {
			System.out.println("Error : There is a problem of rights in the current directory.");
			return 1;
		} catch (ZipException zipe) {
			System.out.println("Error : Failed during zip extraction..");
			return 1;
		} catch (IOException eio) {
			System.out.println("Error : Failed on writing/reading file..");
			return 1;
		}
		
		return errorState;
	}

	private int extract2(Path filenamePath, String  destinationName) {
		int errorState = NO_ERROR;
		
		// CREATE THE ARCHIVE PROJECTS DIR
		String destination = Paths.get(destinationName).toAbsolutePath().toString() + File.separator;
		if (verboseMode) {
			System.out.println("Destination archive : " + destination + " for " + filenamePath); 
		}
		new File(destination).mkdirs();
		
		try (ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(filenamePath.toFile()), Charset.forName("Cp437")) ) {
			ZipEntry zipentry = zipinputstream.getNextEntry();
			
			byte[] buf = new byte[1024];
			while (zipentry != null) {
				String entryName = zipentry.getName();
				File newFile = new File(entryName);
				
				// Creating the parent directories
				String directory = newFile.getParent();
				if (directory == null) {
					if (newFile.isDirectory()) {
						break;
					}
				} else {
					new File(destination + directory).mkdirs();
				}
				
				// Creating zip files into dir
				int n;
				FileOutputStream fileoutputstream;
				if (!zipentry.isDirectory()) {
					
					// #1 : Extract zip project
					fileoutputstream = new FileOutputStream(destination	+ entryName);
					while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
						fileoutputstream.write(buf, 0, n);
					}
					fileoutputstream.close();
					
					
					// #2 : Analyze zip project + extraction
					if (verboseMode) {
						System.out.println("\nWorking oneTop on : " + destination + entryName);
					}
					StringBuilder projectNameBuilder = new StringBuilder();
		            int oneTopResult = oneTop(destination + entryName, projectNameBuilder);
		            if (oneTopResult != NO_ERROR) {
		            	if (oneTopResult == 1) {
		            		System.out.println(Messages.getOutputString(MyColor.ONE_TOP.shortFlag+"", MyColor.ONE_TOP.getValue()[0]));
		            	} else if (oneTopResult == 2) {
		            		System.err.println(Messages.getOutputString(MyColor.FORCE_ONE_TOP.shortFlag+"", MyColor.FORCE_ONE_TOP.getValue()[0]));
		            	}
		            	errorState = FORCE_ERROR;
		            	
		            	// #3 : Delete extracted files.
		            	File oldZip = new File(destination	+ entryName);
		            	oldZip.delete();
		            	
		            	zipinputstream.closeEntry();
		            	zipentry = zipinputstream.getNextEntry();
			    		continue;
		            }
		            String projectName = projectNameBuilder.toString();
		            
		            HashSet<String> files = new HashSet<>();
		            
		            try (ZipInputStream zipinputstreamProj = new ZipInputStream(new FileInputStream(destination	+ entryName), Charset.forName("Cp437")) ) {
		    			ZipEntry zipentryProj = zipinputstreamProj.getNextEntry();
		                
		    			byte[] buf1 = new byte[1024];
		    			while (zipentryProj != null) {
		    				String entryNameProj = zipentryProj.getName();
		    				File newFileProj = new File(entryNameProj);
		    				files.add(entryNameProj);
		    				if (verboseMode) {
		    					System.out.println("	Entry : " + entryNameProj);
		    				}
		    				if (checkFileProperties(entryNameProj) == FORCE_ERROR) {
		    					errorState = FORCE_ERROR;
		    					
		    					zipinputstreamProj.closeEntry();
			    				zipentryProj = zipinputstreamProj.getNextEntry();
			    				continue;
		    				}

		    				// Creating the parent directories
		    				String directoryProj = newFileProj.getParent();
		    				if (directoryProj == null) {
		    					if (newFileProj.isDirectory()) {
		    						break;
		    					}
		    				} else {
		    					new File(destination + directoryProj).mkdirs();
		    				}
		    				
		    				// Creating zip files into dir
		    				int n1;
		    				FileOutputStream fileoutputstreamProj;
		    				if (!zipentryProj.isDirectory()) {
		    					fileoutputstreamProj = new FileOutputStream(destination	+ entryNameProj);
		    					while ((n1 = zipinputstreamProj.read(buf1, 0, 1024)) > -1) {
		    						fileoutputstreamProj.write(buf1, 0, n1);
		    					}
		    					fileoutputstreamProj.close();
		    				}
		    				
		    				zipinputstreamProj.closeEntry();
		    				zipentryProj = zipinputstreamProj.getNextEntry();
		    			}
		    			
			    		// RENAME PROJECT DIR FROM ZIPNAME
						String efficientProjectName = entryName;
						Pattern patternSpaces = Pattern.compile("^\\s+", Pattern.DOTALL); 
			            Matcher matcherSpaces = patternSpaces.matcher(efficientProjectName); 
			            efficientProjectName = matcherSpaces.replaceFirst("");
						Pattern pattern = Pattern.compile("_.*$", Pattern.DOTALL); 
			            Matcher matcher = pattern.matcher(efficientProjectName); 
			            efficientProjectName = matcher.replaceFirst("").replace(" ", "_");
			            
			            Path dirSource = Paths.get(destination + projectName);
			            Path dirTarget = Paths.get(destination + efficientProjectName);
			            FileUtils.deleteRecursiveIfExists(dirTarget);
			            // Move doesn't work enough as expected.
			            // Files.move(dirSource, dirTarget, REPLACE_EXISTING);
			            // Copy doesn't work enough as expected.
			            // Files.copy(dirSource, dirTarget, COPY_ATTRIBUTES );
			            FileUtils.copyFolder(dirSource, dirTarget);
			            FileUtils.deleteRecursiveIfExists(dirSource);
			            
		    			if (exists(files) > NO_ERROR) {
		    				errorState = FORCE_ERROR;
		    			}
		    		}
		            // #3 : Delete extracted files.
	            	File oldZip = new File(destination	+ entryName);
	            	oldZip.delete();
				}
				
				zipinputstream.closeEntry();
				zipentry = zipinputstream.getNextEntry();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error : The file was not found !");
			return 1;
		} catch (SecurityException se) {
			System.out.println("Error : There is a problem of rights in the current directory.");
			return 1;
		} catch (ZipException zipe) {
			System.out.println("Error : Failed during zip extraction..");
			return 1;
		} catch (IOException eio) {
			System.out.println("Error : Failed on writing/reading file..");
			return 1;
		}
		
		return errorState;
	}
	
	/**
	 * Checks if the filename {@code endWith}, {@code beginWith} the given argument option or 
	 * 	if the file is {@code Forbiden}.
	 * 
	 * @return 0 if no FORCE option were found. <br>
	 * 		   1 if a FORCE option occured.
	 */
	private int checkFileProperties(String entryName) {
		if (endsWith(entryName) > NO_ERROR || beginWith(entryName) > NO_ERROR 
				|| forbids(entryName) > NO_ERROR || forbids(entryName) > NO_ERROR) {
			return FORCE_ERROR;
		}
		
		return NO_ERROR;
	}
	
	/**
	 * Work out if there is a unique directory at the top of archive. 
	 * 		Note: that there are multi levels of errors 3 by default, 2 on FORCE, 1 on normal level.
	 * @return 1/2/3 if one directory at top, 0 if not (NO_ERROR).
	 */
	private int oneTop(String zipFile, StringBuilder projectName) throws IOException {
		int errorLevel = 3; // No options but search for oneTop
		
		String oneTop = null;
		if (condition.contains(MyColor.FORCE_ONE_TOP)) {
			errorLevel = 2;
			oneTop = MyColor.FORCE_ONE_TOP.getValue()[0];
		} else if (condition.contains(MyColor.ONE_TOP)) {
			oneTop = MyColor.ONE_TOP.getValue()[0];
			errorLevel = 1;
		}
		
		File fileZip = new File(zipFile);
		try (ZipFile zip = new ZipFile(fileZip, Charset.forName("Cp437"))) {
		    Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();
		    LinkedList<String> fileList = new LinkedList<>();
		    
		    while (zipFileEntries.hasMoreElements()) {
		    	ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
		    	
		    	String currentEntry = entry.getName();
		    	fileList.add(currentEntry);
		    }
		    
		    String oneTopDirectory = null;
		    int index;
		    for (int i = 0; i < fileList.size(); i++) {
		    	String currentFile = fileList.get(i);
		    	
		    	if (i == 0) {
		    		index = currentFile.indexOf("/");
		    		if (index == -1) {
		    			// first element is a file
		    			return errorLevel;
		    		} else {
		    			oneTopDirectory = currentFile.substring(0, index);
		    		}
		    	} else {
		    		index = currentFile.indexOf("/");
		    		String secondDirectory = currentFile.substring(0, index);
		    		
		    		if (!oneTopDirectory.equals(secondDirectory)) {
		    			return errorLevel;
		    		}
		    	}
		    }
		    
		    if (oneTop != null && !oneTopDirectory.equals(oneTop)) {
    			return errorLevel;
    		}
		    projectName.append(oneTopDirectory);
		}
		
		return NO_ERROR;
	}
	
	/**
	 * Test if the string endsWith a given argument option.
	 * 
	 * @return 0 if no suffix was found <br>
	 * 		   1 if a suffix was found on NORMAL level. <br>
	 * 		   2 if a suffix was found on FORCE level.
	 */
	private int endsWith(String entryName) {
		int errorState = NO_ERROR;
		if (condition.contains(MyColor.FORCE_ENDS_WITH)) {
			String[] forceEndsWithValues = MyColor.FORCE_ENDS_WITH.getValue();
			for (String str : forceEndsWithValues) {
				if (entryName.endsWith(str)) {
					System.err.println(Messages.getOutputString(MyColor.FORCE_ENDS_WITH.shortFlag+"", str));
					errorState = FORCE_ERROR;
				}
			}
		}
		if (condition.contains(MyColor.ENDS_WITH)) {
			String[] endsWithValues = MyColor.ENDS_WITH.getValue();
			for (String str : endsWithValues) {
				if (entryName.endsWith(str)) {
					System.out.println(Messages.getOutputString(MyColor.ENDS_WITH.shortFlag+"", str));
					errorState = 2;
				}
			}
		}
		
		return errorState;
	}
	
	/**
	 * Test if the string beginWith a given argument option.
	 * 
	 * @return 0 if no prefix was found <br>
	 * 		   1 if a prefix was found on NORMAL level. <br>
	 * 		   2 if a prefix was found on FORCE level.
	 */
	private int beginWith(String entryName) {
		int errorState = NO_ERROR;
		if (condition.contains(MyColor.FORCE_BEGIN_WITH)) {
			String[] forceBeginWithValues = MyColor.FORCE_BEGIN_WITH.getValue();
			for (String str : forceBeginWithValues) {
				if (entryName.startsWith(str)) {
					System.err.println(Messages.getOutputString(MyColor.FORCE_BEGIN_WITH.shortFlag+"", str));
					errorState = FORCE_ERROR;
				}
			}
		}
		if (condition.contains(MyColor.BEGIN_WITH)) {
			String[] beginWithValues = MyColor.BEGIN_WITH.getValue();
			for (String str : beginWithValues) {
				if (entryName.startsWith(str)) {
					System.out.println(Messages.getOutputString(MyColor.BEGIN_WITH.shortFlag+"", str));
					errorState = 2;
				}
			}
		}
		
		return errorState;
	}
	
	/**
	 * Test if the string is equal to a given argument option.
	 * 
	 * @return 0 if no prefix was found <br>
	 * 		   1 if a prefix was found on NORMAL level. <br>
	 * 		   2 if a prefix was found on FORCE level.
	 */
	private int forbids(String entryName) {
		int errorState = NO_ERROR;
		if (condition.contains(MyColor.FORCE_FORBIDS)) {
			String[] forceForbidsValues = MyColor.FORCE_FORBIDS.getValue();
			for (String str : forceForbidsValues) {
				if (entryName.equals(str)) {
					System.err.println(Messages.getOutputString(MyColor.FORCE_FORBIDS.shortFlag+"", str));
					errorState = FORCE_ERROR;
				}
			}
		}
		if (condition.contains(MyColor.FORBIDS)) {
			String[] forbidsValues = MyColor.FORBIDS.getValue();
			for (String str : forbidsValues) {
				if (entryName.equals(str)) {
					System.out.println(Messages.getOutputString(MyColor.FORBIDS.shortFlag+"", str));
					errorState = 2;
				}
			}
		}
		
		return errorState;
	}
	
	/**
	 * Test if the needed string is in the given zip project.
	 * 
	 * @return 0 if the file was found <br>
	 * 		   1 if the file was not found on FORCE level. <br>
	 * 		   2 if the file was not found on NORMAL level.
	 */
	private int exists(Set<String> files) {
		int errorState = NO_ERROR;
		if (condition.contains(MyColor.FORCE_EXIST)) {
			String[] forceExist = MyColor.FORCE_EXIST.getValue();
			for (String str : forceExist) {
				if (!files.contains(str)) {
					System.err.println(Messages.getOutputString(MyColor.FORCE_EXIST.shortFlag+"", str));
					errorState = FORCE_ERROR;
				}
			}
		}
		if (condition.contains(MyColor.EXIST)) {
			String[] exist = MyColor.EXIST.getValue();
			for (String str : exist) {
				if (!files.contains(str)) {
					System.out.println(Messages.getOutputString(MyColor.EXIST.shortFlag+"", str));
					errorState = 2;
				}
			}
		}
		
		return errorState;
	}

	/**
	 * Start function, note that this function also terminate the program exectution 
	 * 	and exit with the needed status.<br>
	 * 	<pre>
	 * 	- {@code exit(1)} : if an error occured (Force or even Normal).
	 * 	- {@code exit(0)} : if no errors where detected.
	 */
	public void start() {
		if (condition.contains(MyColor.OPTION_ONE)) {
			String[] garbageOptions = MyColor.OPTION_ONE.getValue();
			
			int status = validateArchive(garbageOptions[0]);
			System.exit(status);
		} else if (condition.contains(MyColor.OPTION_TWO)) {
			String[] garbageOptions = MyColor.OPTION_TWO.getValue();
			
			int status = extractZipFile(garbageOptions[0]);
			System.exit(status);
		} else {
			System.out.println("Option not recognized.");
			System.exit(1);
		}
	}
	
	
	
	
	
	
	
	
	
}