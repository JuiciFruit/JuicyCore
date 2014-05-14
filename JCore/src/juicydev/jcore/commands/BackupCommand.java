package juicydev.jcore.commands;

import juicydev.jcore.JCMessageManager;
import juicydev.jcore.Perms;
import juicydev.jcore.backup.JCBackup;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BackupCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (!Perms.BACKUP.has(sender))
			return true;

		JCMessageManager.getInstance().infoSender(sender, "Running a backup.");
		JCBackup.getInstance().runBackup();

		return true;
	}
}
