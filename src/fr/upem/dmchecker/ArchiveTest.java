package fr.upem.dmchecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArchiveTest {

	private Path tmpDir;
	private Logger log;

	@Before
	public void before() throws IOException {
		tmpDir = Files.createTempDirectory("path-monitor-test");
		
		log = Logger.getLogger("fr.upem.dmchecker");
		Logger.getLogger("").setLevel(Level.ALL);
		log.setLevel(Level.INFO);
	}

	@After
	public void after() {
		tmpDir = null;
	}

//	@Test
//	public void testValidateArchiveZipNotOneTop() throws IOException {
//		Archive archive = new Archive();
//		String filename = "TestingFiles\\lezipdezip.zip";
//		System.out.println("\n");
//		assertEquals(0, archive.extractZipFile(filename));
//	}
	
//	@Test
//	public void testValidateArchiveZipPlusieursAuTop() throws IOException {
//		Archive archive = new Archive(log);
//		String filename = "TestingFiles\\plusieursautop.zip";
//		System.out.println("\n");
//		assertEquals(1, archive.validateArchive(filename));
//	}
//	
//	
//	@Test
	
//	public void testValidateArchiveZipPlusieursDirAuTop() throws IOException {
//		Archive archive = new Archive(log);
//		String filename = "TestingFiles\\plusieursDirAuTop.zip";
//		System.out.println("\n");
//		assertTrue(archive.validateArchive(filename));
//	}
//	
//	@Test
//	public void testValidateArchiveZipOneTop() throws IOException {
//		Archive archive = new Archive(log);
//		String filename = "TestingFiles\\testZipOrder1.zip";
//		System.out.println("\n");
//		assertTrue(archive.validateArchive(filename));
//	}
	
//	
//	@Test
//	public void testValidateArchiveZip() throws IOException {
//		Archive archive = new Archive(log);
//		String filename = "TestingFiles\\RawZipArchive.zip";
//		assertFalse(archive.validateArchive(filename));
//	}
	
	
//	@Test
//	public void testZipOrder() throws Exception {
//	    File file = new File("TestingFiles\\testZipOrder1.zip");
//	    ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
//	    ZipEntry entry = null;
//	    System.out.println("\ntestZipOrder:");
//	    int i = 0;
//	    while ((entry = zis.getNextEntry()) != null) {
//	    	System.out.println(i + " == " + entry.getName());
////	    	assertEquals(i+"", entry.getName());
//	    }
//	    zis.close();
//	}
//	
//	@Test
//	public void testZipOrder1() throws Exception {
//	    File file = new File("TestingFiles\\testZipOrder2.zip");
//	    ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
//	    ZipEntry entry = null;
//	    System.out.println("\ntestZipOrder2:");
//	    int i = 0;
//	    while ((entry = zis.getNextEntry()) != null) {
//	    	System.out.println(i + " == " + entry.getName());
////	    	assertEquals(i+"", entry.getName());
//	    }
//	    zis.close();
//	}
//	
//	@Test
//	public void testZipOrder2() throws Exception {
//	    File file = new File("TestingFiles\\testZipOrder3.zip");
//	    ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
//	    ZipEntry entry = null;
//	    System.out.println("\ntestZipOrder3:");
//	    int i = 0;
//	    while ((entry = zis.getNextEntry()) != null) {
//	    	System.out.println(i + " == " + entry.getName());
////	    	assertEquals(i+"", entry.getName());
//	    }
//	    zis.close();
//	}
//	
//	@Test
//	public void testZipOrder3() throws Exception {
//	    File file = new File("TestingFiles\\testZipOrder4.zip");
//	    ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
//	    ZipEntry entry = null;
//	    System.out.println("\ntestZipOrder4:");
//	    int i = 0;
//	    while ((entry = zis.getNextEntry()) != null) {
//	    	System.out.println(i + " == " + entry.getName());
////	    	assertEquals(i+"", entry.getName());
//	    }
//	    zis.close();
//	}
//	
//	@Test
//	public void testZipOrder5() throws Exception {
//	    File file = new File("TestingFiles\\plusieursDirAuTop.zip");
//	    ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
//	    ZipEntry entry = null;
//	    System.out.println("\ntestZipOrder5:");
//	    int i = 0;
//	    while ((entry = zis.getNextEntry()) != null) {
//	    	System.out.println(i + " == " + entry.getName());
////	    	assertEquals(i+"", entry.getName());
//	    }
//	    zis.close();
//	}
	
//	@Test
//	public void testZipOrder2() throws Exception {
//	    File file = new File("TestingFiles\\testZipOrder4.zip");
//	    System.out.println("\n\n"); 
//	    
//	    ZipFile zip = new ZipFile(file, Charset.forName("Cp437"));
//	    Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();
//	    
//	    int i = 0;
//	    while (zipFileEntries.hasMoreElements()) {
//	    	ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
//			System.out.println(i + " == " + entry.getName());
//	    }
//	    System.out.println("\n\n");
//	}
	
	
	
	
	
		
//	@Test(expected = NullPointerException.class )
//	public void testArchiveLoggerTest() throws IOException {
//		new Archive(null);
//	}
//
//	@Test
//	public void testExtractArchiveTARGZ() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\RawTarGzArchive.tar.gz";
//		assertFalse(archive.extractZipFile(filename, destinationname));
//	}
//
//	@Test
//	public void testExtractArchiveZip() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\RawZipArchive.zip";
//		assertTrue(archive.extractZipFile(filename, destinationname));
//	}
//	
//	@Test
//	public void testExtractArchiveZipNotOneTop() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\plusieursautop.zip";
//		System.out.println("\n");
//		assertTrue(archive.extractZipFile(filename, destinationname));
//	}
//	
//	@Test
//	public void testExtractArchiveZipPlusieursAuTop() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\notop.zip";
//		System.out.println("\n");
//		assertTrue(archive.extractZipFile(filename, destinationname));
//	}
//	
//	@Test
//	public void testExtractArchiveZipIntoNull() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\RawZipArchive.zip";
//		assertFalse(archive.extractZipFile(filename, null));
//	}
//	
//	@Test
//	public void testExtractArchiveZipRar() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		// This archive is a zip that was renamed rar
//		String filename = "TestingFiles\\FakeRarArchive.rar";
//		assertTrue(archive.extractZipFile(filename, destinationname));
//	}
//	
//	@Test
//	public void testExtractArchiveRar() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\RawRarArchive.rar";
//		assertFalse(archive.extractZipFile(filename, destinationname)); 
//	}
//	
//	@Test
//	public void testExtractWhatever() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\dd2.pdf";
//		assertFalse(archive.extractZipFile(filename, destinationname)); 
//	}
//	
//	// How to make a test with a real corrupted zip :
//	// 	 - Testing with incomplete files - that's the way most corrupt files are created, by interrupted downloads.
//	//   - Another suggestion is to open the file and insert/delete random bytes.
//	@Test
//	public void testExtractArchiveZipCorrupted() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\RawCorruptZipArchive.zip";
//		assertFalse(archive.extractZipFile(filename, destinationname));
//	}
//	
//	@Test
//	public void testExtractArchiveOfArchives() throws IOException {
//		Archive archive = new Archive(log);
//		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
//		String filename = "TestingFiles\\RawZipArchiveOfArchive.zip";
//		assertFalse(archive.extractZipFile(filename, destinationname)); 
//	}
//	
//	@Test
//	public void testEmptyListener() throws IOException {
//		Path directory = Files.createTempDirectory(tmpDir, null);
//
//		class EmptyPathChangeListener implements PathStateChangeListener {
//			@Override
//			public void pathChanged(Path path) {
//				// do nothing
//			}
//		}
//
//		PushPathMonitor pathMonitor = new PushPathMonitor(directory,
//				new EmptyPathChangeListener());
//	}
//
//	public static class JumpError extends Error {
//		private static final long serialVersionUID = 7102897292057358411L;
//	}
//
//	@Test(timeout = 300)
//	public void testAdded() throws IOException, InterruptedException {
//		final Path directory = Files.createTempDirectory(tmpDir, null);
//		final ArrayList<Path> pathList = new ArrayList<>();
//		PushPathMonitor pathMonitor = new PushPathMonitor(directory,
//				new PathStateChangeListener() {
//					@Override
//					public void pathChanged(Path path) {
//						pathList.add(path);
//						throw new JumpError();
//					}
//				});
//		final Path[] files = new Path[1];
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(100);
//					Path tmpFile = Files.createTempFile(directory, null, null);
//					files[0] = tmpFile;
//				} catch (InterruptedException | IOException e) {
//					throw new AssertionError("error in swpaned thread", e);
//				}
//			}
//		});
//		thread.start();
//		try {
//			pathMonitor.monitor();
//			fail();
//		} catch (JumpError e) {
//			// ok
//		}
//		thread.join();
//		assertEquals(files[0].toAbsolutePath(), pathList.get(0)
//				.toAbsolutePath());
//	}
//
//	@Test(timeout = 300)
//	public void testDeleted() throws IOException, InterruptedException {
//		final Path directory = Files.createTempDirectory(tmpDir, null);
//		final Path tmpFile = Files.createTempFile(directory, null, null);
//		PushPathMonitor pathMonitor = new PushPathMonitor(directory,
//				new PathStateChangeListener() {
//					@Override
//					public void pathChanged(Path path) {
//						assertEquals(tmpFile.toAbsolutePath(),
//								path.toAbsolutePath());
//						throw new JumpError();
//					}
//				});
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(100);
//					Files.delete(tmpFile);
//				} catch (InterruptedException | IOException e) {
//					throw new AssertionError("error in swpaned thread", e);
//				}
//			}
//		});
//		thread.start();
//		try {
//			pathMonitor.monitor();
//			fail();
//		} catch (JumpError e) {
//			// ok
//		}
//		thread.join();
//	}
//
//	@Test(timeout = 1500)
//	public void testInterrupt() throws IOException {
//		final Path directory = Files.createTempDirectory(tmpDir, null);
//		PushPathMonitor pathMonitor = new PushPathMonitor(directory,
//				new PathStateChangeListener() {
//					@Override
//					public void pathChanged(Path path) {
//						fail();
//					}
//				});
//
//		final Thread mainThread = Thread.currentThread();
//		for (int i = 0; i < 10; i++) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						throw new AssertionError("spawn thread interrupted ??",
//								e);
//					}
//					mainThread.interrupt();
//				}
//			}).start();
//			try {
//				pathMonitor.monitor();
//			} catch (InterruptedException e) {
//				return;
//			}
//			if (mainThread.isInterrupted()) {
//				fail("interrupt status should be cleared when exiting monitor()");
//			}
//		}
//	}

}
