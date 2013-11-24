package fr.upem.dmchecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	@Test(expected = NullPointerException.class )
	public void testArchiveLoggerTest() throws IOException {
		new Archive(null);
	}

	@Test
	public void testExtractArchiveTARGZ() throws IOException {
		Archive archive = new Archive(log);
		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
		String filename = "TestingFiles\\RawTarGzArchive.tar.gz";
		assertFalse(archive.extractZipFile(filename, destinationname));
	}

	@Test
	public void testExtractArchiveZip() throws IOException {
		Archive archive = new Archive(log);
		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
		String filename = "TestingFiles\\RawZipArchive.zip";
		assertTrue(archive.extractZipFile(filename, destinationname));
	}
	
	@Test
	public void testExtractArchiveZipIntoNull() throws IOException {
		Archive archive = new Archive(log);
		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
		String filename = "TestingFiles\\RawZipArchive.zip";
		assertFalse(archive.extractZipFile(filename, null));
	}
	
	@Test
	public void testExtractArchiveZipRar() throws IOException {
		Archive archive = new Archive(log);
		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
		// This archive is a zip that was renamed rar
		String filename = "TestingFiles\\FakeRarArchive.rar";
		assertTrue(archive.extractZipFile(filename, destinationname));
	}
	
	@Test
	public void testExtractArchiveRar() throws IOException {
		Archive archive = new Archive(log);
		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
		String filename = "TestingFiles\\RawRarArchive.rar";
		assertFalse(archive.extractZipFile(filename, destinationname)); 
	}
	
	@Test
	public void testExtractWhatever() throws IOException {
		Archive archive = new Archive(log);
		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
		String filename = "TestingFiles\\dd2.pdf";
		assertFalse(archive.extractZipFile(filename, destinationname)); 
	}
	
	// How to make a test with a real corrupted zip :
	// 	 - Testing with incomplete files - that's the way most corrupt files are created, by interrupted downloads.
	//   - Another suggestion is to open the file and insert/delete random bytes.
	@Test
	public void testExtractArchiveZipCorrupted() throws IOException {
		Archive archive = new Archive(log);
		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\TestingFiles\\";
		String filename = "TestingFiles\\RawCorruptZipArchive.zip";
		assertFalse(archive.extractZipFile(filename, destinationname));
	}
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
