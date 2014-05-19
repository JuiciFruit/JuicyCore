package juicydev.jcore.utils;

import juicydev.jcore.JCMessageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permission;

/**
 * Permissions utility class
 * 
 * @author JuicyDev
 */
public class PermUtil {

	/**
	 * Checks if the player has the permission specified, and sends them a
	 * message if they don't.
	 * 
	 * @param sender
	 *            CommandSender to test
	 * @param perm
	 *            Permission to test
	 * @return True if the sender has the permission
	 */
	public static boolean testPerm(CommandSender sender, Permission perm) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (!sender.hasPermission(perm)) {
			JCMessageManager.getInstance().noPermSender(sender);
			return false;
		}
		return true;
	}

	/**
	 * Checks if the player has the permission specified.
	 * 
	 * @param sender
	 *            CommandSender to test
	 * @param perm
	 *            Permission to test
	 * @return True if the sender has the permission
	 */
	public static boolean testPermSilent(CommandSender sender, Permission perm) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (!sender.hasPermission(perm)) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the player has the permission specified, and sends them a msg
	 * if they don't.
	 * 
	 * @param sender
	 *            CommandSender to test
	 * @param node
	 *            Permission node to test
	 * @return True if the sender has the permission
	 */
	public static boolean testPerm(CommandSender sender, String node) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (!sender.hasPermission(node)) {
			JCMessageManager.getInstance().noPermSender(sender);
			return false;
		}
		return true;
	}

	/**
	 * Checks if the player has the permission specified.
	 * 
	 * @param sender
	 *            CommandSender to test
	 * @param node
	 *            Permission node to test
	 * @return True if the sender has the permission
	 */
	public static boolean testPermSilent(CommandSender sender, String node) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (!sender.hasPermission(node)) {
			return false;
		}
		return true;
	}
}
