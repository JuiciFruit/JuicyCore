package juicydev.jcore.utils;

import java.io.File;

/**
 * @author JuicyDev
 */
public class FileUtil {

	/**
	 * @param name
	 * @param dir
	 * @return The file with the specified name from the specified directory -
	 *         null if no file exists
	 */
	public static File getFile(String name, File dir) {
		if (!dir.isDirectory())
			return null;
		if (dir.listFiles().length == 0)
			return null;
		for (File f : dir.listFiles()) {
			if (f.getName().equalsIgnoreCase(name))
				return f;
			if (f.getName().substring(0, f.getName().lastIndexOf("."))
					.equalsIgnoreCase(name))
				return f;
		}
		return null;
	}

	/**
	 * @param name
	 * @param path
	 * @return The file with the specified name from the specified path - null
	 *         if no file exists
	 */
	public static File getFile(String name, String path) {
		File dir = new File(path);
		if (!dir.exists())
			return null;
		if (!dir.isDirectory())
			return null;
		if (dir.listFiles().length == 0)
			return null;
		for (File f : dir.listFiles()) {
			if (f.getName().equalsIgnoreCase(name))
				return f;
			if (f.getName().substring(0, f.getName().lastIndexOf("."))
					.equalsIgnoreCase(name))
				return f;
		}
		return null;
	}

	public static String getExtension(File file) {
		return file.getName().substring(file.getName().lastIndexOf("."),
				file.getName().length());
	}

	/**
	 * @param file
	 * @return File name without the extension
	 */
	public static String getName(File file) {
		return file.getName().substring(0, file.getName().lastIndexOf("."));
	}
}
