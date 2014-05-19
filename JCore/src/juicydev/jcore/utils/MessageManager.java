package juicydev.jcore.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import juicydev.jcore.JCore;
import juicydev.jcore.Perms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Messaging utility class
 * 
 * @author JuicyDev
 */
public class MessageManager {
	private String prefix = "[%name%] ";

	private Logger logger;

	private static String name;

	public MessageManager(String str) {
		name = str;
		prefix = prefix.replace("%name%", name);
		logger = Logger.getLogger("Minecraft.JuicyDev." + name);
	}

	public static MessageManager getInstance(String name) {
		return new MessageManager(name);
	}

	public void info(String str) {
		logger.info(prefix + str);
	}

	public void fine(String str) {
		logger.fine(prefix + str);
	}

	public void log(Level level, String str) {
		logger.log(level, prefix + str);
	}

	public void severe(String str) {
		logger.severe(prefix + str);
	}

	public void error(Exception e) {
		severe("An error has occured: \"" + e.getCause().toString() + "\"");
		e.printStackTrace();
	}

	public void error(CommandSender sender, Exception e) {
		sender.sendMessage(prefix + "An error has occured.");
		error(e);
	}

	public void msgSender(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.BLUE + prefix + ChatColor.GRAY + msg);
	}

	public void infoSender(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.BLUE + prefix + ChatColor.YELLOW + msg);
	}

	public void errSender(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.BLUE + prefix + ChatColor.RED + msg);
	}

	public void noPermSender(CommandSender sender) {
		sender.sendMessage(ChatColor.BLUE + prefix + ChatColor.RED
				+ "You do not have permission to do this.");
	}

	public void debug(String msg) {
		if (JCore.debugEnabled)
			info("[Debug] " + msg);
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (Perms.DEBUG.hasSilent(player)
					&& JCore.debug.contains(player.getUniqueId()))
				player.sendMessage(ChatColor.BLUE + prefix + "[Debug] "
						+ ChatColor.YELLOW + msg);
		}
	}
}
