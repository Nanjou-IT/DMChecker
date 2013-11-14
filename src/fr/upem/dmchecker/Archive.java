package fr.upem.dmchecker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Archive {

	public static void extractZipFiles(String filename) {
		try {
			// destination folder to extract the contents
			String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\";

			byte[] buf = new byte[1024];
			ZipInputStream zipinputstream = null;
			ZipEntry zipentry;
			zipinputstream = new ZipInputStream(new FileInputStream(filename));
			zipentry = zipinputstream.getNextEntry();

			System.out.println(zipentry.getName());

			while (zipentry != null) {

				// for each entry to be extracted
				String entryName = zipentry.getName();

				int n;
				FileOutputStream fileoutputstream;
				File newFile = new File(entryName);

				String directory = newFile.getParent();

				// to creating the parent directories
				if (directory == null) {
					if (newFile.isDirectory()) {
						break;
					}
				} else {
					new File(destinationname + directory).mkdirs();
				}

				if (!zipentry.isDirectory()) {
					System.out.println("File to be extracted....." + entryName);
					fileoutputstream = new FileOutputStream(destinationname
							+ entryName);
					while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
						fileoutputstream.write(buf, 0, n);
					}
					fileoutputstream.close();
				}

				zipinputstream.closeEntry();
				zipentry = zipinputstream.getNextEntry();
			}// while

			zipinputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getZipFiles(String zipFile, String destFolder) {
		BufferedOutputStream dest = null;
		ZipInputStream zis = null;

		try {
			zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
			ZipEntry entry;
			
			while ((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting: " + entry.getName());
				int count;
				byte data[] = new byte[1024];

				if (entry.isDirectory()) {
					new File(destFolder + "/" + entry.getName()).mkdirs();
					continue;
				} else {
					int di = entry.getName().lastIndexOf('/');
					if (di != -1) {
						new File(destFolder + "/"
								+ entry.getName().substring(0, di)).mkdirs();
					}
				}

				FileOutputStream fos = new FileOutputStream(destFolder + "/"
						+ entry.getName());
				dest = new BufferedOutputStream(fos);

				while ((count = zis.read(data)) != -1) {
					dest.write(data, 0, count);
				}

				dest.flush();
				dest.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Hello world, bonjour monde.");

		String destinationname = "C:\\Users\\Nanjou\\Documents\\GitHub\\DMChecker\\";
		// extractZipFiles("IR2_Java_Avancé_TP5[LATKOVIC].zip");
		// System.out.println("DONE.\n");
		getZipFiles("CRE_REMI_DESIGN_PATTERN_TD1.rar", destinationname);
		System.out.println("DONE.\n");
		getZipFiles("TD03.tar.gz", destinationname);
		System.out.println("DONE.\n");
	}
}
