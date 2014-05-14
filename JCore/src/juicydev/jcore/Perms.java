package juicydev.jcore;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public enum Perms {
	PLAYDISC(newPerm("playdisc", "Allows the player to play music discs.",
			PermissionDefault.OP)),
	PLAYMIDI(newPerm("playmidi", "Allows the player to play midi files.",
			PermissionDefault.OP)),
	BACKUP(newPerm("backup", "Allows the player to run backups.",
			PermissionDefault.OP)),
	DEBUG(newPerm("debug", "Allows the player to use the debugging command.",
			PermissionDefault.OP));

	private Permission perm;
	private String name;

	private Perms(Permission perm) {
		this.perm = perm;
		this.name = perm.getName();
	}

	/**
	 * Check if a sender has the permission and send the sender a message
	 * telling them they do not have permission for the action.
	 * 
	 * @param player
	 * @return
	 */
	public boolean has(CommandSender sender) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (!sender.hasPermission(perm)) {
			JCMessageManager.getInstance().noPermSender(sender);
			return false;
		}
		return true;
	}

	/**
	 * Check if a sender has the permission.
	 * 
	 * @param player
	 * @return
	 */
	public boolean hasSilent(CommandSender sender) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (!sender.hasPermission(perm))
			return false;
		return true;
	}

	/**
	 * Gets the permission.
	 */
	public Permission getPerm() {
		return perm;
	}

	/**
	 * Gets the string permission node for the permission.
	 */
	public String toString() {
		return name;
	}

	private static Permission newPerm(String name, String description,
			PermissionDefault defaultValue) {
		return new Permission(JCore.getInstance().getName().toLowerCase()
				+ "." + name, description, defaultValue);
	}
}
