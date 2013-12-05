package fr.upem.dmchecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
	/**
	 * By default deletion fails for non-empty directories, it works like "rm".
	 * We need something a little more brutual - this does the equivalent of
	 * "rm -r"
	 * 
	 * @param path
	 *            Root File Path
	 * @return true if the file and all sub files/directories have been removed<br>
	 *         false if the file/directory was not found
	 * @throws IOException
	 */
	public static boolean deleteRecursiveIfExists(Path path) throws IOException {
		if (!Files.exists(path)) {
			return false;
		}
		boolean ret = true;
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
				for (Path p : ds) {
					ret = ret && FileUtils.deleteRecursiveIfExists(p);
				}
			}
		}
		boolean deletion = false;
		deletion = Files.deleteIfExists(path);

		return ret && deletion;
	}
	
	
	public static void copyFolder(Path src, Path dest) throws IOException {
		if (Files.isDirectory(src)) {
			// if directory not exists, create it
			if (!Files.exists(dest)) {
				Files.createDirectory(dest);
			}

			try (DirectoryStream<Path> ds = Files.newDirectoryStream(src)) {
				for (Path p : ds) {
					// construct the src and dest file structure
					Path srcFile = Paths.get(src + File.separator + p.getFileName());
					Path destFile = Paths.get(dest + File.separator + p.getFileName());
					
					// recursive copy
					copyFolder(srcFile, destFile);
				}
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src.toString());
			OutputStream out = new FileOutputStream(dest.toString());

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
		}
	}
}