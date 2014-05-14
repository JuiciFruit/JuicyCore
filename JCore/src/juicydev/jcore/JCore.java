package juicydev.jcore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import juicydev.jcore.backup.JCBackup;
import juicydev.jcore.chatlogger.ChatLogger;
import juicydev.jcore.commands.BackupCommand;
import juicydev.jcore.commands.DebugCommand;
import juicydev.jcore.commands.PlayDiscCommand;
import juicydev.jcore.commands.PlayMidiCommand;
import juicydev.jcore.utils.MessageManager;
import juicydev.jcore.utils.player.PlayerDataManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Core plugin for my other plugins, includes useful methods for debugging,
 * logging, reflection and more
 * 
 * @author JuicyDev
 */
public class JCore extends JavaPlugin {

	private static JCore instance;

	public static boolean debugEnabled = false;
	public static ArrayList<UUID> debug = new ArrayList<UUID>();

	private PluginManager pm = Bukkit.getPluginManager();
	private PlayerDataManager playerLoader;
	private MessageManager mm;

	private String versionString;
	private int version;

	public void onEnable() {
		if (!verCheck())
			pm.disablePlugin(this);

		instance = this;
		playerLoader = new PlayerDataManager(this);
		mm = JCMessageManager.getInstance();
		
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			mm.error(e);
		}

		getConfig().options().copyDefaults(true);
		saveConfig();

		getDataFolder().mkdirs();
		midiSetup();

		getCommand("playdisc").setExecutor(new PlayDiscCommand());
		getCommand("playmidi").setExecutor(new PlayMidiCommand());
		getCommand("backup").setExecutor(new BackupCommand());
		getCommand("debug").setExecutor(new DebugCommand());

		JCBackup.getInstance().setup();
		debugEnabled = getConfig().getBoolean("debug");

		if (getConfig().getBoolean("logging-enabled"))
			pm.registerEvents(new ChatLogger(), this);
		info();
	}

	public void onDisable() {
		getConfig().set("debug", debugEnabled);
		saveConfig();
	}

	public static JCore getInstance() {
		return instance;
	}

	public PlayerDataManager getPlayerLoader() {
		return playerLoader;
	}

	private void info() {
		JCMessageManager
				.getInstance()
				.info("Logging is "
						+ (getConfig().getBoolean("logging-enabled") ? "enabled"
								: "disabled") + ".");
		JCMessageManager.getInstance().info(
				"Debugging is " + (debugEnabled ? "on" : "off") + ".");
	}

	private boolean verCheck() {
		this.versionString = getServer()
				.getClass()
				.getPackage()
				.getName()
				.substring(
						getServer().getClass().getPackage().getName()
								.lastIndexOf('.') + 1);
		try {
			this.version = Integer.valueOf(versionString.replace("v", "")
					.replace("_", "").replace("R", ""));
		} catch (NumberFormatException e) { // Fallback
			this.version = 173;
		}
		if (version < 173) {
			mm.severe("This JCore build is for minecraft 1.7.9 and higher");
			mm.severe("Please obtain the correct version from the bukkit dev page.");
			return false;
		}
		return true;
	}

	private void midiSetup() {
		File midiDir = new File(getDataFolder().getAbsolutePath()
				+ File.separator + "midi");
		midiDir.mkdirs();
	}
}
