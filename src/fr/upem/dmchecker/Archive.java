package fr.upem.dmchecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Archive {
	private final Logger logger;
	
	public Archive(Logger logger) {
		this.logger = Objects.requireNonNull(logger); 
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
	private boolean isValidFormat(String archiveName) {
		if (archiveName == null) {
			System.err.println("Error : The archive doesn't exist.");
			return false;
		}
		
		File archive = new File(archiveName);
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
	 * @return {@code true} if the archive is accepted.<br>
	 *         {@code false} if a problem occured, a message will describe the
	 *         problem on System.err.
	 */
	public boolean validateArchive(String archiveName) {
		if (!isValidFormat(archiveName)) {
			System.err.println("Error : This kind of file cannot be validate.");
			return false;
		}
		
		// TODO : Probably more stuff to do there.
		return extract(archiveName,	(zipinputstream, zipentry) -> {});
	}

	/**
	 * This method permit to test one archive {@code archiveName} and to extract 
	 * 	it in specified {@code destinationName}. <br>
	 * Returned value is a boolean indictating the accepted status. <br>
	 * 
	 * @param archiveName name of the zip file
	 * @param destinationName name of the directory destination
	 * @return {@code true} if the archive is accepted.<br>
	 *         {@code false} if a problem occured, a message will describe the
	 *         problem on System.err.
	 */
	public boolean extractZipFile(String archiveName, String destinationName) {
		// #TODO : Choice, redo part of the stuff or -> isValidFormat(archiveName) ? 
		if (!validateArchive(archiveName) || destinationName == null) { 
			System.err.println("Cannot proceed to the extraction of: [" + archiveName + "] into "+ destinationName +" directory.");
			return false;
		}
		
		// If we passed previous tests, <filename> :
		//   - is non-null
		//	 - has a good format
		//	 - can be openned
		return extract(archiveName, 
				(zipinputstream, zipentry) -> {
					byte[] buf = new byte[1024];
					while (zipentry != null) {
						// for each entry to be extracted
						String entryName = zipentry.getName();
						File newFile = new File(entryName);
						
						String directory = newFile.getParent();
						System.out.println("DIR > "+directory); 

						// Creating the parent directories
						if (directory == null) {
							if (newFile.isDirectory()) {
								break;
							}
						} else {
							new File(destinationName + directory).mkdirs();
						}
						
						// Creating files into dirs
						int n;
						FileOutputStream fileoutputstream;
						if (!zipentry.isDirectory()) {
							System.out.println("File to be extracted....." + entryName);
							fileoutputstream = new FileOutputStream(destinationName	+ entryName);
							
							while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
								fileoutputstream.write(buf, 0, n);
							}
							
							fileoutputstream.close();
						}
						
						zipinputstream.closeEntry();
						zipentry = zipinputstream.getNextEntry();
					}
				}
			);
	}

	/**
	 * Does the extraction work, defined by a certain {@code action} which is an {@link ExtractionTask}.
	 * 
	 * @param filename
	 * @param destinationname
	 * @param action  functionnal interface {@link ExtractionTask} handling extraction specifities.
	 * @return {@code boolean} representing the acceptation status
	 */
	private boolean extract(String filename, ExtractionTask action) {
		// #TODO: NOTE : Créer une factory de ExtractConditions représentative des propriétés formelle sur un zip ??
		try {
			ZipInputStream zipinputstream = null;
			ZipEntry zipentry;
			zipinputstream = new ZipInputStream(new FileInputStream(filename));
			zipentry = zipinputstream.getNextEntry();

			action.create(zipinputstream, zipentry);
			
			zipinputstream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error : The file was not found !");
			return false;
		} catch (SecurityException se) {
			System.out.println("Error : There is a problem of rights in the current directory.");
			return false;
		} catch (ZipException zipe) {
			System.out.println("Error : Failed during zip extraction..");
			return false;
		} catch (IOException eio) {
			System.out.println("Error : Failed on writing/reading file..");
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println("Hello world, bonjour monde.");
		
		Logger log = Logger.getLogger("fr.upem.dmchecker");
		Logger.getLogger("").setLevel(Level.ALL);
		log.setLevel(Level.INFO);
		
		Archive archive = new Archive(log);
		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
		
		archive.validateArchive("TestingFiles\\IR2_Java_Avancé_TP5[LATKOVIC].zip");
//		archive.extractZipFile("TestingFiles\\IR2_Java_Avancé_TP5[LATKOVIC].zip", destinationname);
		System.out.println("DONE.\n");
//		archive.extractZipFiles("TestingFiles\\IR2_Java_Avancé_TP5.rar", destinationname);
//		System.out.println("DONE.\n");
//		archive.extractZipFile("TestingFiles\\TD03.tar.gz", destinationname);
//		System.out.println("DONE.\n");
	}
}
