package juicydev.jcore.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import juicydev.jcore.JCMessageManager;
import juicydev.jcore.JCore;
import juicydev.jcore.Perms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class JCBackup {

	private static JCBackup instance = new JCBackup();

	private JCBackup() {
	}

	public static JCBackup getInstance() {
		return instance;
	}

	private File backupFolder;
	private File serverFolder;

	private int backupDelayInt;
	private long backupDelay;

	private boolean enabled;

	public void setup() {
		enabled = JCore.getInstance().getConfig()
				.getBoolean("backup-enabled");

		backupFolder = new File(JCore.getInstance().getDataFolder()
				.getAbsolutePath()
				+ File.separator + "backups");
		backupFolder.mkdirs();

		String jcPluginFolder = JCore.getInstance().getDataFolder()
				.getAbsolutePath();
		String serverFolderStr1 = jcPluginFolder.substring(0,
				jcPluginFolder.lastIndexOf(File.separatorChar));
		String serverFolderStr = serverFolderStr1.substring(0,
				serverFolderStr1.lastIndexOf(File.separatorChar));
		serverFolder = new File(serverFolderStr);

		backupDelayInt = JCore.getInstance().getConfig()
				.getInt("backup-delay");
		backupDelay = backupDelayInt * 60 * 20;

		JCMessageManager.getInstance().info("Auto backup is " + (enabled ? "enabled" : "disabled") + ".");
		if (enabled) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(
					JCore.getInstance(), new Runnable() {
						public void run() {
							runBackup();
						}
					}, backupDelay, backupDelay);
			JCMessageManager.getInstance().info(
					"Backup delay set at: " + backupDelayInt + " minutes.");
		}
	}

	public void runBackup() {
		Bukkit.broadcast(
				ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "["
						+ JCore.getInstance().getName() + ": "
						+ ChatColor.GREEN + "Running backup."
						+ ChatColor.GRAY.toString()
						+ ChatColor.ITALIC.toString() + "]",
				Perms.BACKUP.toString());
		Bukkit.getConsoleSender().sendMessage(
				JCore.getInstance().getName() + ": " + ChatColor.GREEN
						+ "Running backup.");

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-off");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");

		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = new SimpleDateFormat("MMM").format(cal.getTime());
		int dateInt = cal.get(Calendar.DATE);
		String date = String.valueOf(dateInt);
		if (dateInt < 10) {
			date = "0" + String.valueOf(dateInt);
		}
		String fileName = new SimpleDateFormat("hh-mm-ss")
				.format(cal.getTime());

		String zipLocation = backupFolder + File.separator + year
				+ File.separator + month + File.separator + date;
		(new File(zipLocation)).mkdirs();

		try {
			FileOutputStream fileWriter = new FileOutputStream(zipLocation
					+ File.separator + fileName + ".zip");
			ZipOutputStream zip = new ZipOutputStream(fileWriter);
			for (String file : serverFolder.list()) {
				if (!(file.equalsIgnoreCase("logs") || file.endsWith(".jar")))
					addFileToZip("", serverFolder.getAbsolutePath()
							+ File.separator + file, zip);
			}
			zip.flush();
			zip.close();

			Bukkit.broadcast(
					ChatColor.GRAY.toString() + ChatColor.ITALIC.toString()
							+ "[" + JCore.getInstance().getName() + ": "
							+ ChatColor.GREEN + "Finished backup."
							+ ChatColor.GRAY.toString()
							+ ChatColor.ITALIC.toString() + "]",
					Perms.BACKUP.toString());
			Bukkit.getConsoleSender().sendMessage(
					JCore.getInstance().getName() + ": " + ChatColor.GREEN
							+ "Finished backup.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-on");
	}

	private void addFileToZip(String path, String srcFile, ZipOutputStream zip)
			throws IOException {
		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			if (folder.getName().equals("plugins"))
				addPluginFolderToZip(path, srcFile, zip);
			else
				addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			if (path.equals("")) {
				zip.putNextEntry(new ZipEntry(folder.getName()));
			} else {
				zip.putNextEntry(new ZipEntry(path + File.separator
						+ folder.getName()));
			}
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
			zip.closeEntry();
			zip.flush();
			in.close();
		}
	}

	private void addFolderToZip(String path, String srcFolder,
			ZipOutputStream zip) throws IOException {
		File folder = new File(srcFolder);
		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + File.separator
						+ fileName, zip);
			} else {
				addFileToZip(path + File.separator + folder.getName(),
						srcFolder + File.separator + fileName, zip);
			}
		}
	}

	private void addPluginFolderToZip(String path, String srcFolder,
			ZipOutputStream zip) throws IOException {
		File folder = new File(srcFolder);
		for (String fileName : folder.list()) {
			if (!(fileName.equals(JCore.getInstance().getName()) || fileName
					.endsWith(".jar"))) {
				if (path.equals("")) {
					addFileToZip(folder.getName(), srcFolder + File.separator
							+ fileName, zip);
				} else {
					addFileToZip(path + File.separator + folder.getName(),
							srcFolder + File.separator + fileName, zip);
				}
			}
		}
	}
}
